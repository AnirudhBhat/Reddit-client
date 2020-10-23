package com.abhat.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.model.RedditResponse
import com.abhat.search.data.SearchRepository
import com.abhat.search.data.SearchRepositoryImpl
import com.abhat.search.data.SearchResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

/**
 * Created by Anirudh Uppunda on 24,September,2020
 */
open class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val contextProvider: CoroutineContextProvider
): ViewModel() {

    private val uiState: MutableLiveData<UIState> = MutableLiveData()
    fun getUIState() = uiState as LiveData<UIState>

    private var currentUIState = UIState()
        set(value) {
            field = value
            uiState.postValue(value)
        }


    fun search(searchQuery: String) {
        reducer(SearchViewState.Loading(true))
        viewModelScope.launch(contextProvider.Main) {
            supervisorScope {
                try {
                    val response =
                        withContext(viewModelScope.coroutineContext + contextProvider.IO) {
                            searchRepository.search(searchQuery)
                        }
                    response?.let { response ->
                        reducer(stateToResult(response))
                    }
                } catch (e: Exception) {
                    reducer(SearchViewState.Failure(throwable = e.cause))
                }
            }
        }
    }

    private fun stateToResult(searchResult: SearchResult): SearchViewState {
        return when (searchResult) {
            is SearchResult.Loading -> {
               SearchViewState.Loading(searchResult.isLoading)
            }

            is SearchResult.Success -> {
                SearchViewState.Success(mapRedditResponseToDisplayNames(searchResult.response))
            }

            is SearchResult.Error -> {
                SearchViewState.Failure(searchResult.throwable)
            }
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.Search -> {
                search(action.searchQuery)
            }
        }
    }

    private fun reducer(searchViewState: SearchViewState) {
        currentUIState = when (searchViewState) {
            is SearchViewState.Loading -> {
                currentUIState.copy(isLoading = searchViewState.isLoading)
            }

            is SearchViewState.Success -> {
                currentUIState.copy(isLoading = false, success = searchViewState.response ?: listOf())
            }

            is SearchViewState.Failure -> {
                currentUIState.copy(isLoading = false, error = searchViewState.throwable)
            }
        }
    }

    private fun mapRedditResponseToDisplayNames(redditResponse: RedditResponse?): List<String>? {
        return redditResponse?.data?.children?.map { children ->
            children.data.displayName ?: ""
        }
    }

}