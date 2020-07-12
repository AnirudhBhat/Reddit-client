package com.abhat.feed.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.abhat.core.SortType.SortType
import com.abhat.feed.R

/**
 * Created by Anirudh Uppunda on 12,July,2020
 */
class SubredditFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_subreddit, container, false)
        return view
    }

//    fun openSubredditBottomSheet() {
//        val subredditBottomSheetFragment = SubredditBottomSheetFragment()
//        subredditBottomSheetFragment.subredditFragment = this
//        subredditBottomSheetFragment.sortType = SortType.hot
//        activity?.supportFragmentManager?.let {
//            subredditBottomSheetFragment.show(it, "subreddit_bottom_sheet")
//        }
//    }
}