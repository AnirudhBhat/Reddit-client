package com.abhat.feed.ui

import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.feed.data.FeedRepository
import com.abhat.feed.ui.state.FeedViewResult
import com.abhat.feed.ui.state.FeedViewState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Anirudh Uppunda on 20,April,2020
 */
open class FeedViewModel(
    private val feedRepository: FeedRepository,
    private val contextProvider: CoroutineContextProvider
) : ViewModel() {

    val feedViewState: MutableLiveData<FeedViewState> = MutableLiveData()

    private var currentViewState = FeedViewState()
        set(value) {
            field = value
            feedViewState.postValue(value)
        }

    val isNsfwLiveData: MutableLiveData<Pair<Boolean, RelativeLayout?>> = MutableLiveData()

    fun showProgressBar() {
        currentViewState = currentViewState.copy(isLoading = true, feedList = null, error = null)
    }

    fun hideProgressBar() {
        currentViewState = currentViewState.copy(isLoading = false, feedList = null, error = null)
    }

    fun getFeed(subreddit: String, after: String) {
        viewModelScope.launch(contextProvider.Main) {
            val feedViewResult =
                withContext(viewModelScope.coroutineContext + contextProvider.IO) {
                    feedRepository.getFeed(subreddit, after)
                }
            feedViewResult?.let { feedViewResult ->
                when (feedViewResult) {
                    is FeedViewResult.Success -> {
                        currentViewState = currentViewState.copy(
                            isLoading = false,
                            feedList = feedViewResult.feedData,
                            error = null
                        )
                    }

                    is FeedViewResult.Error.NetworkError -> {
                        currentViewState =
                            currentViewState.copy(
                                isLoading = false,
                                feedList = null,
                                error = feedViewResult.throwable
                            )
                    }

                    is FeedViewResult.Error.AuthorizationError -> {
                        currentViewState =
                            currentViewState.copy(
                                isLoading = false,
                                feedList = null,
                                authorizationError = feedViewResult.throwable
                            )
                    }
                }
            }
        }
    }

    fun shouldShowBestOptionInSortList(subreddit: String? = null): Boolean {
        return subreddit == null || subreddit.isEmpty()
    }
}