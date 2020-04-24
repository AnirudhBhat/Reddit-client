package com.abhat.feed.data

import com.abhat.core.RedditApi
import com.abhat.feed.ui.state.FeedViewResult
import retrofit2.HttpException

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
class FeedRepositoryImpl(val redditApi: RedditApi): FeedRepository {

    override suspend fun getFeed(subreddit: String): FeedViewResult? {
        try {
            val response = redditApi.getRedditList(subreddit).await()
            return FeedViewResult.Success(response.data)
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