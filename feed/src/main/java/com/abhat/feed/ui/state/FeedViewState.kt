package com.abhat.feed.ui.state

import com.abhat.core.model.Children
import com.abhat.core.model.RedditResponse

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
data class FeedViewState(
    val isLoading: Boolean = false,
    val feedList: RedditResponse? = null,
    val error: Throwable? = null,
    val authorizationError: Throwable? = null
)