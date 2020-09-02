package com.abhat.feed.ui.trendingsubreddit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.feed.data.TrendingSubredditRepository
import com.abhat.feed.ui.state.TrendingSubredditViewResult
import com.abhat.feed.ui.state.TrendingSubredditViewState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Anirudh Uppunda on 01,September,2020
 */
class TrendingSubredditViewModel(private val trendingSubredditRepository: TrendingSubredditRepository,
                                 private val contextProvider: CoroutineContextProvider): ViewModel() {

    val trendingSubredditViewState: MutableLiveData<TrendingSubredditViewState> = MutableLiveData()

    private var currentViewState = TrendingSubredditViewState()
        set(value) {
            field = value
            trendingSubredditViewState.postValue(value)
        }

    private fun showProgressBar() {
        currentViewState = currentViewState.copy(isLoading = true)
    }

    private fun hideProgressBar() {
        currentViewState = currentViewState.copy(isLoading = false)
    }

    fun getTrendingSubreddits() {
        reducer(TrendingSubredditViewResult.LoadingState(isLoading = true))
        viewModelScope.launch(contextProvider.Main) {
            val trendingSubredditViewResult = withContext(viewModelScope.coroutineContext + contextProvider.IO) {
                trendingSubredditRepository.getTrendingSubreddit()
            }
            reducer(TrendingSubredditViewResult.LoadingState(isLoading = false))
            reducer(trendingSubredditViewResult)
        }
    }

    private fun reducer(trendingSubredditViewResult: TrendingSubredditViewResult) {
        currentViewState = when (trendingSubredditViewResult) {
            is TrendingSubredditViewResult.LoadingState -> {
                currentViewState.copy(isLoading = trendingSubredditViewResult.isLoading)
            }

            is TrendingSubredditViewResult.Success -> {
                currentViewState.copy(trendingSubredditList = trendingSubredditViewResult.trendingSubreddit?.subredditNames)
            }

            is TrendingSubredditViewResult.Error -> {
                currentViewState.copy(error = trendingSubredditViewResult.throwable)
            }
        }
    }
}