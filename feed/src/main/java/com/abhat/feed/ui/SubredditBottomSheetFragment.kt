package com.abhat.feed.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhat.core.SortType.SortType
import com.abhat.core.common.PreferenceHelper
import com.abhat.feed.R
import com.abhat.feed.ui.constants.Constants
import com.abhat.feed.ui.subreddit.SubredditViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_subreddit_bottomsheet_layout.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by Anirudh Uppunda on 12,July,2020
 */
class SubredditBottomSheetFragment: BottomSheetDialogFragment() {

    var feedFragment: FeedFragment? = null
    var sortType: SortType? = null
    private var subredditList: List<String>? = null
    private val subredditViewModel: SubredditViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readBundle()
        setStyle(STYLE_NORMAL, R.style.AppRoundedCornerBottomSheetDialogTheme)
    }

    private fun readBundle() {
        val bundle = arguments
        bundle?.let { bundle ->
            if (bundle.containsKey(Constants.SUBREDDIT)) {
                subredditList = bundle.getStringArrayList(Constants.SUBREDDIT)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subreddit_bottomsheet_layout, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        val headers = HashMap<String, String>()
        headers["Authorization"] =
            "Bearer " + PreferenceHelper.getTokenFromPrefs(requireActivity())?.access_token
        subredditViewModel.getSubscribedSubreddits(headers, 100)
    }

    private fun observeViewModel() {
        subredditViewModel.subredditViewState.observe(viewLifecycleOwner, Observer {
            handleProgressBarVisibility(it.isLoading)
            (rv_subreddit.adapter as SubredditAdapter)?.updateSubredditList(it.subredditList)
        })
    }

    private fun handleProgressBarVisibility(shouldShowProgressBar: Boolean) {
        if (shouldShowProgressBar) {
            subreddit_progress_bar.visibility = View.VISIBLE
        } else {
            subreddit_progress_bar.visibility = View.GONE
        }
    }

    fun setupRecyclerView() {
        rv_subreddit.layoutManager = LinearLayoutManager(activity)
        rv_subreddit.adapter = SubredditAdapter(
            subredditList,
            feedFragment,
            sortType,
            this
        )
        (rv_subreddit.adapter as SubredditAdapter).stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    companion object {
        fun newInstance(subredditsList: List<String>?): SubredditBottomSheetFragment {
            val bundle = Bundle()
            subredditsList?.let {
                bundle.putStringArrayList(Constants.SUBREDDIT, ArrayList(it))
            }
            val subredditBottomSheetFragment = SubredditBottomSheetFragment()
            subredditBottomSheetFragment.arguments = bundle
            return subredditBottomSheetFragment
        }
    }
}