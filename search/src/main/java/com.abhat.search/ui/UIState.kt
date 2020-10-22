package com.abhat.search.ui

/**
 * Created by Anirudh Uppunda on 02,June,2020
 */
data class UIState(
    val isLoading: Boolean = false,
    val success: List<String>? = null,
    val error: Throwable? = null
)