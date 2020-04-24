package com.abhat.feed.ui.state

import com.abhat.core.model.PostDetailResponse

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
data class FeedViewState(
    val isLoading: Boolean = false,
    val feedList: PostDetailResponse.Data? = null,
    val error: Throwable? = null,
    val authorizationError: Throwable? = null
)