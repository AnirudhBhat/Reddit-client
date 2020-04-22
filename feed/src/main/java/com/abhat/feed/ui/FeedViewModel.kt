package com.abhat.feed.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.feed.data.FeedRepository
import com.abhat.feed.ui.state.FeedViewState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Anirudh Uppunda on 20,April,2020
 */
class FeedViewModel(
    private val feedRepository: FeedRepository,
    private val contextProvider: CoroutineContextProvider
): ViewModel() {

    val feedViewState: MutableLiveData<FeedViewState> = MutableLiveData()

    private var currentViewState = FeedViewState()
        set(value) {
            field = value
            feedViewState.postValue(value)
        }

    fun showProgressBar() {
        currentViewState = currentViewState.copy(isLoading = true, feedList = null, error = null)
    }

    fun hideProgressBar() {
        currentViewState = currentViewState.copy(isLoading = false, feedList = null, error = null)
    }

    fun getFeed(subreddit: String) {
        viewModelScope.launch(contextProvider.Main) {
            try {
                val response =
                    withContext(viewModelScope.coroutineContext + contextProvider.IO) {
                        feedRepository.getFeed(subreddit)
                    }
                response?.let { response ->
                    currentViewState = currentViewState.copy(
                        isLoading = false,
                        feedList = response.data,
                        error = null
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                currentViewState =
                    currentViewState.copy(isLoading = false, feedList = null, error = e.cause)
            }
        }
    }
}