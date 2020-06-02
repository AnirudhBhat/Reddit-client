package com.abhat.comment.ui

import com.abhat.core.model.RedditResponse

/**
 * Created by Anirudh Uppunda on 02,June,2020
 */
data class UIState(
    private val isLoading: Boolean = false,
    private val success: RedditResponse? = null,
    private val error: Throwable? = null
)