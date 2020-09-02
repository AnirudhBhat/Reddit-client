package com.abhat.feed.ui.state

/**
 * Created by Anirudh Uppunda on 02,September,2020
 */
data class TrendingSubredditViewState(
    val isLoading: Boolean = false,
    val trendingSubredditList: List<String>? = null,
    val error: Throwable? = null
)