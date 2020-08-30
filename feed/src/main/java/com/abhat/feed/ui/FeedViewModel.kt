package com.abhat.feed.ui

import android.widget.RelativeLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhat.core.BuildConfig
import com.abhat.core.SortType.SortType
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.extensions.encodeBase64ToString
import com.abhat.core.model.Children
import com.abhat.core.model.RedditResponse
import com.abhat.core.model.TokenEntity
import com.abhat.core.model.TokenResponse
import com.abhat.feed.data.FeedRepository
import com.abhat.feed.ui.state.FeedViewResult
import com.abhat.feed.ui.state.FeedViewState
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Created by Anirudh Uppunda on 20,April,2020
 */
open class FeedViewModel(
    private val feedRepository: FeedRepository,
    private val contextProvider: CoroutineContextProvider
) : ViewModel() {

    private lateinit var sortTypeList: List<SortType>
    val feedViewState: MutableLiveData<FeedViewState> = MutableLiveData()
    var feedList: RedditResponse? = null

    private val tokenResponseLiveData = MutableLiveData<TokenResponse>()
    fun getTokenResponseLiveData() = tokenResponseLiveData as LiveData<TokenResponse>

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

    fun subredditBottomSheetOpened() {
        currentViewState = currentViewState.copy(isSubredditBottomSheetOpen = true)
    }

    fun subredditBottomSheetClosed() {
        //currentViewState = currentViewState.copy(isSubredditBottomSheetOpen = false)
    }

    fun sortBottomSheetOpened() {
        //currentViewState = currentViewState.copy(isSortBottomSheetOpen = true)
    }

    fun sortBottomSheetClosed() {
        //currentViewState = currentViewState.copy(isSortBottomSheetOpen = false)
    }

    fun getFeed(
        headers: HashMap<String, String> = hashMapOf(),
        subreddit: String,
        after: String,
        sortType: SortType,
        isOauth: Boolean = false
    ) {
        showProgressBar()
        viewModelScope.launch(contextProvider.Main) {
            val feedViewResult =
                withContext(viewModelScope.coroutineContext + contextProvider.IO) {
                    if (isOauth) {
                        feedRepository.getFeedOauth(headers, subreddit, after, sortType)
                    } else {
                        feedRepository.getFeed(subreddit, after, sortType)
                    }
                }
            feedViewResult?.let { feedViewResult ->
                sortTypeList = if (shouldShowBestOptionInSortList(subreddit)) {
                    returnSortTypeList()
                } else {
                    returnSortTypeListWithoutBestOption()
                }
                when (feedViewResult) {
                    is FeedViewResult.LoadingState -> {
                        currentViewState.copy(
                            isLoading = true,
                            subreddit = subreddit,
                            sortType = returnSortType(sortType, subreddit),
                            sortList = sortTypeList,
                            error = null
                        )
                    }

                    is FeedViewResult.Success -> {
                        currentViewState = currentViewState.copy(
                            isLoading = false,
                            feedList = feedViewResult.feedData,
                            subreddit = subreddit,
                            sortType = returnSortType(sortType, subreddit),
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
                                sortType = returnSortType(sortType, subreddit),
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
                                sortType = returnSortType(sortType, subreddit),
                                sortList = sortTypeList,
                                authorizationError = feedViewResult.throwable
                            )
                    }
                }
            }
        }
    }

    fun retrieveAccessToken(headers: HashMap<String, String>, fields: HashMap<String, String>) {
        currentViewState = currentViewState.copy(isLoading = true)
        viewModelScope.launch(contextProvider.Main) {
            supervisorScope {
                try {
                    val response =
                        withContext(viewModelScope.coroutineContext + contextProvider.IO) {
                            feedRepository.getAccessToken(
                                headers,
                                fields
                            )
                        }
                    response?.let { tokenResponse ->
                        tokenResponseLiveData.postValue(tokenResponse)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun shouldShowBestOptionInSortList(subreddit: String? = null): Boolean {
        return subreddit == null || subreddit.isEmpty() || subreddit.equals(
            "frontpage",
            ignoreCase = true
        )
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

    fun returnSortType(sortType: SortType, subreddit: String): SortType {
        if (sortType == SortType.empty && shouldShowBestOptionInSortList(subreddit)) {
            return SortType.best
        }
        return sortType
    }

    fun howShouldWeFetchTheToken(tokenEntity: TokenEntity?): FetchTokenFrom {
        return when {
            tokenEntity == null -> FetchTokenFrom.USER_NOT_LOGGED_IN

            isTokenExpired(tokenEntity.expiry) -> FetchTokenFrom.REFRESH_TOKEN_API

            else -> FetchTokenFrom.SHARED_PREFERENCE
        }
    }

    private fun isTokenExpired(tokenExpiry: Calendar?): Boolean {
        tokenExpiry?.let { tokenExpiry ->
            val now = Calendar.getInstance()
            if (tokenExpiry.before(now)) {
                return true
            }
            return false
        } ?: run {
            return false
        }
    }

    fun refreshAccessToken(refreshToken: String) {
        val headers = HashMap<String, String>()
        val fields = HashMap<String, String>()
        val auth = BuildConfig.CLIENT_ID.encodeBase64ToString()
        headers["Authorization"] = "Basic $auth"
        headers["Content-Type"] = "application/x-www-form-urlencoded"
        fields["grant_type"] = "refresh_token"
        fields["refresh_token"] = refreshToken

        retrieveAccessToken(headers, fields)
    }

    enum class FetchTokenFrom {
        SHARED_PREFERENCE,
        USER_NOT_LOGGED_IN,
        REFRESH_TOKEN_API
    }
}