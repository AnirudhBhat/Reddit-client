package com.abhat.feed.ui.state

import com.abhat.core.model.PostDetailResponse

/**
 * Created by Anirudh Uppunda on 20,April,2020
 */
sealed class FeedViewResult {
    data class LoadingState(val isLoading: Boolean) : FeedViewResult()
    data class Success(val feedData: PostDetailResponse.Data?) : FeedViewResult()
    sealed class Error: FeedViewResult() {
        data class AuthorizationError(val throwable: Throwable?) : Error()
        data class NetworkError(val throwable: Throwable?) : Error()
    }
}