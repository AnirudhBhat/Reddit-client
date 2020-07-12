package com.abhat.feed.ui

import android.widget.RelativeLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhat.core.SortType.SortType
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

    private lateinit var sortTypeList: List<SortType>
    val feedViewState: MutableLiveData<FeedViewState> = MutableLiveData()

    private var currentViewState = FeedViewState(subreddit = "all")
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

    fun getFeed(subreddit: String, after: String, sortType: SortType) {
        showProgressBar()
        viewModelScope.launch(contextProvider.Main) {
            val feedViewResult =
                withContext(viewModelScope.coroutineContext + contextProvider.IO) {
                    feedRepository.getFeed(subreddit, after, sortType)
                }
            feedViewResult?.let { feedViewResult ->
                sortTypeList = if (shouldShowBestOptionInSortList(subreddit)) {
                    returnSortTypeList()
                } else {
                    returnSortTypeListWithoutBestOption()
                }
                when (feedViewResult) {
                    is FeedViewResult.Success -> {
                        currentViewState = currentViewState.copy(
                            isLoading = false,
                            feedList = feedViewResult.feedData,
                            subreddit = subreddit,
                            sortType = sortType,
                            sortList = sortTypeList,
                            error = null
                        )
                    }

                    is FeedViewResult.Error.NetworkError -> {
                        currentViewState =
                            currentViewState.copy(
                                isLoading = false,
                                feedList = null,
                                subreddit = subreddit,
                                sortType = sortType,
                                sortList = sortTypeList,
                                error = feedViewResult.throwable
                            )
                    }

                    is FeedViewResult.Error.AuthorizationError -> {
                        currentViewState =
                            currentViewState.copy(
                                isLoading = false,
                                feedList = null,
                                subreddit = subreddit,
                                sortType = sortType,
                                sortList = sortTypeList,
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

    private fun returnSortTypeListWithoutBestOption(): List<SortType> {
        return listOf(
            SortType.hot,
            SortType.new,
            SortType.rising
        )
    }

    private fun returnSortTypeList(): List<SortType> {
        return listOf(
            SortType.best,
            SortType.hot,
            SortType.new,
            SortType.rising
        )
    }
}