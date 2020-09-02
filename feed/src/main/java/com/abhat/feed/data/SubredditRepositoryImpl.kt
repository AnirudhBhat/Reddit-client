package com.abhat.feed.data

import com.abhat.core.RedditApi
import com.abhat.feed.ui.state.subreddit.SubredditViewResult

/**
 * Created by Anirudh Uppunda on 02,September,2020
 */
class SubredditRepositoryImpl(private val redditApi: RedditApi): SubredditRepository {
    override suspend fun getSubscribedSubreddits(
        headers: HashMap<String, String>,
        limit: Int
    ): SubredditViewResult {
        return try {
            val response = redditApi.getSubscribedSubreddits(headers, limit).await()
            SubredditViewResult.Success(response)
        } catch (e: Exception) {
            SubredditViewResult.Error(e.cause)
        }
    }
}