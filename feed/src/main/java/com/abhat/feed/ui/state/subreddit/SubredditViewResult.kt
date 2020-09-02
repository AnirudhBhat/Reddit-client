package com.abhat.feed.ui.state.subreddit

import com.abhat.core.model.RedditResponse

/**
 * Created by Anirudh Uppunda on 01,September,2020
 */
sealed class SubredditViewResult {
    data class LoadingState(val isLoading: Boolean) : SubredditViewResult()
    data class Success(val subredditList: RedditResponse?) : SubredditViewResult()
    data class Error(val throwable: Throwable?) : SubredditViewResult()
}