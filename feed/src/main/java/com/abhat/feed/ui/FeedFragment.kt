package com.abhat.feed.ui

import android.os.Bundle
import android.os.Handler
import android.util.Log
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
import com.abhat.feed.ui.constants.Constants
import com.abhat.feed.ui.constants.Constants.KEY_SUBREDDIT_BOTTOM_SHEET
import com.abhat.feed.ui.state.FeedViewState
import kotlinx.android.synthetic.main.fragment_feed.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
class FeedFragment : Fragment() {
    private var feedRecyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var feedAdapter: FeedAdapter? = null
    private var after: String = ""
    private var SUBREDDIT = ""
    private var isFromSubreddit: Boolean? = null

    private var loading = false
    var pastVisiblesItems: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    private lateinit var currentFeedUiState: FeedViewState
    private val feedViewModel: FeedViewModel by viewModel()


    companion object {
        fun newInstance(subreddit: String = "frontpage"): FeedFragment {
            val bundle = Bundle()
            bundle.putString(Constants.SUBREDDIT, subreddit)
            val feedFragment = FeedFragment()
            feedFragment.arguments = bundle
            return feedFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        bundle?.let { bundle ->
            if (bundle.containsKey(Constants.SUBREDDIT)) {
                SUBREDDIT = bundle.getString(Constants.SUBREDDIT, "frontpage")
            }
        }
//        isFromSubreddit?.let {
//            if (it) {
//                openSubredditBottomSheet()
//            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_feed, container, false)
        setupRecyclerView(view)
        observeViewModel()
        feedViewModel.feedViewState.value?.let {
            bindUI(it)
            bindThisFragmentToSubredditBottomSheet()
//            if (it.isSubredditBottomSheetOpen) {
//                openSubredditBottomSheet()
//            }
            if (it.isSortBottomSheetOpen) {
                openSortBottomSheet()
            }
        } ?: run {
            feedViewModel.showProgressBar()
            feedViewModel.getFeed(SUBREDDIT, after, SortType.empty)
        }
        return view
    }

    private fun bindThisFragmentToSubredditBottomSheet() {
        var subredditFragment =
            activity?.supportFragmentManager?.findFragmentByTag(KEY_SUBREDDIT_BOTTOM_SHEET) as? SubredditBottomSheetFragment
        subredditFragment?.let {
            it.feedFragment = this
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
//        feedViewModel.subredditBottomSheetClosed()
//        feedViewModel.sortBottomSheetClosed()
        super.onSaveInstanceState(outState)
    }

    fun showProgressBar() {
        feedViewModel.showProgressBar()
    }

    fun getFeed(subreddit: String, after: String, sortType: SortType) {
        feedViewModel.sortBottomSheetClosed()
        feedViewModel.subredditBottomSheetClosed()
        feedViewModel.getFeed(subreddit, after, sortType)
    }

    private fun observeViewModel() {
        feedViewModel.feedViewState.observe(requireActivity(), Observer { feedViewState ->
            bindUI(feedViewState)
        })
    }

    private fun bindUI(feedViewState: FeedViewState) {
        currentFeedUiState = feedViewState
        setProgressBarVisibility(feedViewState)
        if (!feedViewState.isLoading) {
            if (anyError(feedViewState)) {
                loading = false
                if (isNetworkError(feedViewState)) {
                    showErrorToast("oops, something went wrong!, please check your connection")
                } else if (isAuthorizationError(feedViewState)) {
                    showErrorToast("Please sign in to use this feature")
                }
            } else {
                after = feedViewState?.feedList?.data?.after ?: ""
                if (loading) {
                    feedAdapter?.addRedditData(
                        feedViewState.feedList?.data?.children,
                        feedViewState.sortType
                    )
                } else {
                    feedAdapter?.updateRedditData(
                        feedViewState.feedList?.data?.children,
                        feedViewState.sortType
                    )
//                    feedRecyclerView?.scheduleLayoutAnimation()
                }
                loading = false
            }
        }
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
            progress_bar?.visibility = View.VISIBLE
        } else {
            progress_bar?.visibility = View.GONE
        }
    }

    private fun setupRecyclerView(itemView: View) {
        feedRecyclerView = itemView.findViewById(R.id.feed_recycler_view)
        layoutManager = LinearLayoutManager(activity)
        feedRecyclerView?.layoutManager = layoutManager
        feedAdapter = FeedAdapter(activity, this, feedViewModel, null, CoroutineContextProvider())
        feedRecyclerView?.adapter = feedAdapter
        feedAdapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

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
                            feedViewModel.getFeed(
                                currentFeedUiState.subreddit,
                                after,
                                currentFeedUiState.sortType
                            )
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
        var sortBottomSheet =
            activity?.supportFragmentManager?.findFragmentByTag(Constants.KEY_SORT_BOTTOM_SHEET) as? SortBottomSheet
        sortBottomSheet?.let { sortBottomSheet ->
            sortBottomSheet.subreddit = currentFeedUiState.subreddit
            sortBottomSheet.feedFragment = this
            sortBottomSheet.sortTypeList = currentFeedUiState.sortList
            feedViewModel.sortBottomSheetOpened()
        } ?: run {
            sortBottomSheet = SortBottomSheet()
            sortBottomSheet?.subreddit = currentFeedUiState.subreddit
            sortBottomSheet?.feedFragment = this
            sortBottomSheet?.sortTypeList = currentFeedUiState.sortList
            activity?.supportFragmentManager?.let {
                sortBottomSheet?.show(it, Constants.KEY_SORT_BOTTOM_SHEET)
            }
            feedViewModel.sortBottomSheetOpened()
        }
        Handler().postDelayed({
            sortBottomSheet?.dialog?.setOnCancelListener {
                feedViewModel.sortBottomSheetClosed()
            }
        }, 200)

    }

    fun openSubredditBottomSheet() {
        var subredditFragment =
            activity?.supportFragmentManager?.findFragmentByTag(KEY_SUBREDDIT_BOTTOM_SHEET) as? SubredditBottomSheetFragment
        subredditFragment?.let { subredditFragment ->
            subredditFragment?.feedFragment = this
            subredditFragment?.sortType = SortType.hot
            //feedViewModel.subredditBottomSheetOpened()
        } ?: run {
            subredditFragment = SubredditBottomSheetFragment()
            subredditFragment?.feedFragment = this
            subredditFragment?.sortType = SortType.hot
            activity?.supportFragmentManager?.let {
                subredditFragment?.show(it, KEY_SUBREDDIT_BOTTOM_SHEET)
            }
            //feedViewModel.subredditBottomSheetOpened()
        }
//        Handler().postDelayed({
//            subredditFragment?.dialog?.setOnCancelListener {
//                feedViewModel.subredditBottomSheetClosed()
//            }
//        }, 200)
    }
}