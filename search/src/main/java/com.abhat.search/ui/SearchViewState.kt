package com.abhat.search.ui

sealed class SearchViewState {
    data class Loading(val isLoading: Boolean): SearchViewState()
    data class Success(val response: List<SearchViewModel.SearchedSubreddits>?): SearchViewState()
    data class Failure(val throwable: Throwable?): SearchViewState()
}
