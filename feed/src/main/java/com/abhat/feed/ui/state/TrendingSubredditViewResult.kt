package com.abhat.feed.ui.state

import com.abhat.core.model.TrendingSubreddit

/**
 * Created by Anirudh Uppunda on 01,September,2020
 */
sealed class TrendingSubredditViewResult {
    data class LoadingState(val isLoading: Boolean) : TrendingSubredditViewResult()
    data class Success(val trendingSubreddit: TrendingSubreddit?) : TrendingSubredditViewResult()
    data class Error(val throwable: Throwable?) : TrendingSubredditViewResult()
}