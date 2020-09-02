package com.abhat.feed.ui.subreddit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.model.RedditResponse
import com.abhat.feed.data.SubredditRepository
import com.abhat.feed.ui.state.subreddit.SubredditViewResult
import com.abhat.feed.ui.state.subreddit.SubredditViewState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Anirudh Uppunda on 02,September,2020
 */
class SubredditViewModel(private val subredditRepository: SubredditRepository,
                         private val contextProvider: CoroutineContextProvider): ViewModel() {

    val subredditViewState: MutableLiveData<SubredditViewState> = MutableLiveData()

    private var currentViewState = SubredditViewState()
        set(value) {
            field = value
            subredditViewState.postValue(value)
        }

    fun getSubscribedSubreddits(headers: HashMap<String, String>, limit: Int = 100) {
        reducer(SubredditViewResult.LoadingState(isLoading = true))
        viewModelScope.launch(contextProvider.Main) {
            val trendingSubredditViewResult = withContext(viewModelScope.coroutineContext + contextProvider.IO) {
                subredditRepository.getSubscribedSubreddits(headers, limit)
            }
            reducer(SubredditViewResult.LoadingState(isLoading = false))
            reducer(trendingSubredditViewResult)
        }
    }

    private fun reducer(subredditViewResult: SubredditViewResult) {
        currentViewState = when (subredditViewResult) {
            is SubredditViewResult.LoadingState -> {
                currentViewState.copy(isLoading = subredditViewResult.isLoading)
            }

            is SubredditViewResult.Success -> {
                currentViewState.copy(subredditList = mapSubredditResponseToSubredditList(subredditViewResult.subredditList))
            }

            is SubredditViewResult.Error -> {
                currentViewState.copy(error = subredditViewResult.throwable)
            }
        }
    }

    private fun mapSubredditResponseToSubredditList(subredditResponse: RedditResponse?): List<String> {
        val subredditsList = mutableListOf<String>()
        subredditResponse?.data?.children?.forEach {
            subredditsList.add(it.data.displayName ?: "")
        }
        return subredditsList
    }

}