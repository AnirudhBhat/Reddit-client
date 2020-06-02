package com.abhat.comment.ui

import com.abhat.core.model.RedditResponse

sealed class PostDetailState {
    data class Loading(val isLoading: Boolean): PostDetailState()
    data class Success(val response: RedditResponse?): PostDetailState()
    data class Failure(val throwable: Throwable?): PostDetailState()
}
