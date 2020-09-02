package com.abhat.feed.data

import com.abhat.core.RedditApi
import com.abhat.core.network.HostSelectionInterceptor
import com.abhat.feed.ui.state.TrendingSubredditViewResult
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

/**
 * Created by Anirudh Uppunda on 01,September,2020
 */
class TrendingSubredditRepositoryImpl(private val redditApi: RedditApi): TrendingSubredditRepository, KoinComponent {

    override suspend fun getTrendingSubreddit(): TrendingSubredditViewResult {
        return try {
            val response = redditApi.getTrendingSubreddits().await()
            TrendingSubredditViewResult.Success(response)
        } catch (e: Exception) {
            TrendingSubredditViewResult.Error(e.cause)
        }
    }
}