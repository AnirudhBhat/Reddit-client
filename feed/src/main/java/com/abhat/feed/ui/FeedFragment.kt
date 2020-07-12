package com.abhat.feed.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhat.core.SortType.SortType
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.feed.R
import com.abhat.feed.ui.state.FeedViewState
import kotlinx.android.synthetic.main.fragment_feed.*
import org.koin.android.ext.android.inject

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
class FeedFragment : Fragment() {
    private var feedRecyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var feedAdapter: FeedAdapter? = null
    private var after: String = ""
    private val SUBREDDIT = "all"
    private var isFromSubreddit: Boolean? = null

    private var loading = false
    var pastVisiblesItems: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    private lateinit var currentFeedUiState: FeedViewState
    private val feedViewModel: FeedViewModel by inject()


    companion object {
        fun newInstance(fromSubreddit: Boolean = false): FeedFragment {
            val bundle = Bundle()
            bundle.putBoolean("is_from_subreddit", fromSubreddit)
            val feedFragment = FeedFragment()
            feedFragment.arguments = bundle
            return feedFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        bundle?.let { bundle ->
            if (bundle.containsKey("is_from_subreddit")) {
                isFromSubreddit = bundle.getBoolean("is_from_subreddit")
            }
        }
        isFromSubreddit?.let {
            if (it) {
                openSubredditBottomSheet()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_feed, container, false)
//        if (isFromSubreddit == null || isFromSubreddit == false) {
            setupRecyclerView(view)
            observeViewModel()
            feedViewModel.showProgressBar()
            feedViewModel.getFeed(SUBREDDIT, after)
//        }
        return view
    }

    fun showProgressBar() {
        feedViewModel.showProgressBar()
    }

    fun getFeed(subreddit: String, after: String, sortType: SortType) {
        feedViewModel.getFeed(subreddit, after, sortType)
    }

    private fun observeViewModel() {
        feedViewModel.feedViewState.observe(requireActivity(), Observer { feedViewState ->
            currentFeedUiState = feedViewState
            setProgressBarVisibility(feedViewState)
            if (!feedViewState.isLoading) {
                if (anyError(feedViewState)) {
                    if (isNetworkError(feedViewState)) {
                        showErrorToast("oops, something went wrong!, please check your connection")
                    } else if (isAuthorizationError(feedViewState)) {
                        showErrorToast("Please sign in to use this feature")
                    }
                } else {
                    after = feedViewState?.feedList?.data?.after ?: ""
                    if (loading) {
                        feedAdapter?.addRedditData(feedViewState.feedList?.data?.children, feedViewState.sortType)
                    } else {
                        feedAdapter?.updateRedditData(feedViewState.feedList?.data?.children, feedViewState.sortType)
                        feedRecyclerView?.scheduleLayoutAnimation()
                    }
                    loading = false
                }
            }
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
        feedAdapter = FeedAdapter(activity, this, feedViewModel, null, CoroutineContextProvider())
        feedRecyclerView?.adapter = feedAdapter

        feedRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    visibleItemCount = (layoutManager as LinearLayoutManager).childCount
                    totalItemCount = (layoutManager as LinearLayoutManager).itemCount
                    pastVisiblesItems =
                        (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            feedViewModel.showProgressBar()
                            loading = true
                            feedViewModel.getFeed(currentFeedUiState.subreddit, after, currentFeedUiState.sortType)
                        }
                    }
                }
            }
        })
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    fun openSortBottomSheet() {
        val sortBottomSheet = SortBottomSheet()
        sortBottomSheet.subreddit = currentFeedUiState.subreddit
        sortBottomSheet.feedFragment = this
        sortBottomSheet.sortTypeList = currentFeedUiState.sortList
        activity?.supportFragmentManager?.let {
            sortBottomSheet.show(it, "sort_bottom_sheet")
        }
    }

    fun openSubredditBottomSheet() {
        val subredditBottomSheetFragment = SubredditBottomSheetFragment()
        subredditBottomSheetFragment.feedFragment = this
        subredditBottomSheetFragment.sortType = SortType.hot
        activity?.supportFragmentManager?.let {
            subredditBottomSheetFragment.show(it, "subreddit_bottom_sheet")
        }
    }
}