package com.abhat.feed.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhat.core.SortType.SortType
import com.abhat.feed.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_subreddit_bottomsheet_layout.*

/**
 * Created by Anirudh Uppunda on 12,July,2020
 */
class SubredditBottomSheetFragment: BottomSheetDialogFragment() {

    var feedFragment: FeedFragment? = null
    var sortType: SortType? = null
    private val subredditList = listOf<String>(
        "all",
        "Popular",
        "Art",
        "AskReddit",
        "askscience",
        "aww",
        "androiddev",
        "android_devs",
        "books",
        "creepy",
        "dataisbeautiful",
        "DIY",
        "Documentaries",
        "EarthPorn",
        "explainlikeimfive",
        "food",
        "funny",
        "GetMotivated",
        "IAMA",
        "LifeProTips",
        "movies",
        "news",
        "OldSchoolCool",
        "pics",
        "science",
        "todayilearned",
        "space",
        "worldnews",
        "videos",
        "WTF",
        "Cricket",
        "kannada",
        "Android",
        "indiegames",
        "gifs",
        "itookapicture",
        "Kotlin",
        "reactiongifs",
        "nocontextpics",
        "holdmybeer",
        "HeavySeas",
        "likeus"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subreddit_bottomsheet_layout, container, false)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    fun setupRecyclerView() {
        rv_subreddit.layoutManager = LinearLayoutManager(activity)
        rv_subreddit.adapter = SubredditAdapter(
            subredditList,
            feedFragment,
            sortType,
            this
        )
    }
}