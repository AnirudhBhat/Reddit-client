package com.abhat.search.ui

import com.abhat.core.model.RedditResponse

sealed class SearchViewState {
    data class Loading(val isLoading: Boolean): SearchViewState()
    data class Success(val response: RedditResponse?): SearchViewState()
    data class Failure(val throwable: Throwable?): SearchViewState()
}
