package com.abhat.feed.data

import com.abhat.core.RedditApi
import com.abhat.feed.ui.state.TrendingSubredditViewResult

/**
 * Created by Anirudh Uppunda on 01,September,2020
 */
class TrendingSubredditRepositoryImpl(private val redditApi: RedditApi): TrendingSubredditRepository {
    override suspend fun getTrendingSubreddit(): TrendingSubredditViewResult {
        return try {
            val response = redditApi.getTrendingSubreddits()
            TrendingSubredditViewResult.Success(response.await())
        } catch (e: Exception) {
            TrendingSubredditViewResult.Error(e.cause)
        }
    }
}