package com.abhat.feed.ui.state.subreddit

/**
 * Created by Anirudh Uppunda on 02,September,2020
 */
data class SubredditViewState(
    val isLoading: Boolean = false,
    val subredditList: List<String>? = null,
    val error: Throwable? = null
)