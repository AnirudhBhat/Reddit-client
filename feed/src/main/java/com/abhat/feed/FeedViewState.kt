package com.abhat.feed

import com.abhat.core.model.PostDetailResponse

/**
 * Created by Anirudh Uppunda on 20,April,2020
 */
data class FeedViewState(
    var isLoading: Boolean = false,
    val feedList: List<PostDetailResponse.Data>? = null,
    val error: Throwable? = null
)