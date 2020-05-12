package com.abhat.feed.data

import com.abhat.core.RedditApi
import com.abhat.feed.ui.state.FeedViewResult
import retrofit2.HttpException

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
open class FeedRepositoryImpl(val redditApi: RedditApi): FeedRepository {

    override suspend fun getFeed(subreddit: String, after: String): FeedViewResult? {
        try {
            val response = redditApi.getRedditList(subreddit, after).await()
            return FeedViewResult.Success(response)
        } catch (e: Exception) {
            return if (e is HttpException) {
                when (e.code()) {
                    403 -> {
                        FeedViewResult.Error.AuthorizationError(Throwable(e))
                    }

                    else -> {
                        FeedViewResult.Error.NetworkError(Throwable(e))
                    }
                }
            } else {
                FeedViewResult.Error.NetworkError(Throwable(e))
            }
        }
        return null
    }
}