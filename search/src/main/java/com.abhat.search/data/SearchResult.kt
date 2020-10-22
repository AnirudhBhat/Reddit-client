package com.abhat.search.data

import com.abhat.core.model.RedditResponse

/**
 * Created by Anirudh Uppunda on 24,September,2020
 */
sealed class SearchResult {
    data class Loading(val isLoading: Boolean = false): SearchResult()
    data class Success(val response: RedditResponse?): SearchResult()
    data class Error(val throwable: Throwable?): SearchResult()
}