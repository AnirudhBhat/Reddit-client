package com.abhat.feed.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private val subredditList = listOf(
        "frontpage",
        "all",
        "Popular",
        "Art",
        "AskReddit",
        "askscience",
        "aww",
        "androiddev",
        "android_devs",
        "books",
        "cozyplaces",
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
        "instant_regret",
        "IdiotsInCars",
        "LifeProTips",
        "movies",
        "news",
        "nextfuckinglevel",
        "OldSchoolCool",
        "pics",
        "PublicFreakout",
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppRoundedCornerBottomSheetDialogTheme)
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
}