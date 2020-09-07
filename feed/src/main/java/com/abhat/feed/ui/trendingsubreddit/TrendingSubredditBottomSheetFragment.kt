package com.abhat.feed.ui.trendingsubreddit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhat.core.SortType.SortType
import com.abhat.feed.R
import com.abhat.feed.ui.FeedFragment
import com.abhat.feed.ui.constants.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_trending_subreddit_bottomsheet_layout.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by Anirudh Uppunda on 12,July,2020
 */
class TrendingSubredditBottomSheetFragment: BottomSheetDialogFragment() {

    var feedFragment: FeedFragment? = null
    var sortType: SortType? = null
    private val trendingSubredditViewModel: TrendingSubredditViewModel by viewModel()
    private var trendingSubredditsList: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readBundle()
        setStyle(STYLE_NORMAL, R.style.AppRoundedCornerBottomSheetDialogTheme)
    }

    private fun readBundle() {
        val bundle = arguments
        bundle?.let { bundle ->
            if (bundle.containsKey(Constants.TRENDING_SUBREDDIT)) {
                trendingSubredditsList = bundle.getStringArrayList(Constants.TRENDING_SUBREDDIT)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trending_subreddit_bottomsheet_layout, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupRecyclerView()
        trendingSubredditViewModel.getTrendingSubreddits()
    }

    private fun observeViewModel() {
        trendingSubredditViewModel.trendingSubredditViewState.observe(viewLifecycleOwner, Observer {
            handleProgressBarVisibility(it.isLoading)
            it.trendingSubredditList?.let {
                (rv_trending_subreddit.adapter as? TrendingSubredditAdapter)?.updateTrendingSubredditsList(it)
            }
        })
    }

    private fun handleProgressBarVisibility(shouldShowProgressBar: Boolean) {
        if (shouldShowProgressBar) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }

    fun setupRecyclerView() {
        rv_trending_subreddit.layoutManager = LinearLayoutManager(activity)
        rv_trending_subreddit.adapter = TrendingSubredditAdapter(trendingSubredditsList, feedFragment)
        (rv_trending_subreddit.adapter as TrendingSubredditAdapter).stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    companion object {
        fun newInstance(trendingSubredditsList: List<String>?): TrendingSubredditBottomSheetFragment {
            val bundle = Bundle()
            trendingSubredditsList?.let {
                bundle.putStringArrayList(Constants.TRENDING_SUBREDDIT, ArrayList(it))
            }
            val trendingSubredditBottomSheetFragment = TrendingSubredditBottomSheetFragment()
            trendingSubredditBottomSheetFragment.arguments = bundle
            return trendingSubredditBottomSheetFragment
        }
    }
}