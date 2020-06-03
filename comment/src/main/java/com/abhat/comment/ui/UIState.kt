package com.abhat.comment.ui

import com.abhat.core.model.RedditResponse

/**
 * Created by Anirudh Uppunda on 02,June,2020
 */
data class UIState(
    val isLoading: Boolean = false,
    val success: RedditResponse? = null,
    val error: Throwable? = null
)