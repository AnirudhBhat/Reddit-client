package com.abhat.reddit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhat.feed.ui.FeedViewModel
import com.abhat.feed.ui.state.FeedViewState
import com.abhat.reddit.adapter.FeedAdapter
import kotlinx.android.synthetic.main.fragment_feed.*
import org.koin.android.ext.android.inject

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
class FeedFragment : Fragment() {
    private var feedRecyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var feedAdapter: FeedAdapter? = null

    private val feedViewModel: FeedViewModel by inject()

    companion object {
        fun newInstance(): FeedFragment {
            return FeedFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_feed, container, false)
        setupRecyclerView(view)
        observeViewModel()
        feedViewModel.showProgressBar()
        feedViewModel.getFeed("all")
        return view
    }

    private fun observeViewModel() {
        feedViewModel.feedViewState.observe(this, Observer { feedViewState ->
            setProgressBarVisibility(feedViewState)
            if (anyError(feedViewState)) {
                if (isNetworkError(feedViewState)) {
                    showErrorToast("oops, something went wrong!, please check your connection")
                } else if (isAuthorizationError(feedViewState)) {
                    showErrorToast("Please sign in to use this feature")
                }
            } else {
                feedAdapter?.updateRedditData(feedViewState.feedList)
            }
//            feedViewState.error?.let { throwable ->
//                showErrorToast("oops, something went wrong!")
//            } ?: run {
//                feedAdapter?.updateRedditData(feedViewState.feedList)
//            }
        })
    }

    private fun anyError(feedViewState: FeedViewState): Boolean {
        if (feedViewState.error != null || feedViewState.authorizationError != null) {
            return true
        }
        return false
    }

    private fun isNetworkError(feedViewState: FeedViewState): Boolean {
        return feedViewState.error != null
    }

    private fun isAuthorizationError(feedViewState: FeedViewState): Boolean {
        return feedViewState.authorizationError != null
    }

    private fun setProgressBarVisibility(viewState: FeedViewState) {
        if (viewState.isLoading) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }

    private fun setupRecyclerView(itemView: View) {
        feedRecyclerView = itemView.findViewById(R.id.feed_recycler_view)
        layoutManager = LinearLayoutManager(activity)
        feedRecyclerView?.layoutManager = layoutManager
        feedAdapter = FeedAdapter(activity!!, null)
        feedRecyclerView?.adapter = feedAdapter
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }
}