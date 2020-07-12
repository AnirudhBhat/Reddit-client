package com.abhat.feed.data

import com.abhat.core.RedditApi
import com.abhat.core.SortType.SortType
import com.abhat.core.model.RedditResponse
import com.abhat.feed.ui.state.FeedViewResult
import retrofit2.HttpException

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
open class FeedRepositoryImpl(val redditApi: RedditApi): FeedRepository {

    override suspend fun getFeed(subreddit: String, after: String, sortType: SortType): FeedViewResult? {
        try {
            val response: RedditResponse = if ( subreddit.isNullOrEmpty() || subreddit.equals("frontpage", ignoreCase = true)) {
                when (sortType) {
                    SortType.empty -> {
                        redditApi.getRedditFrontPage(sortType = "", after = after).await()
                    }

                    SortType.hot, SortType.best, SortType.rising, SortType.new -> {
                        redditApi.getRedditFrontPage(sortType.name, after).await()
                    }
                }
            } else {
                redditApi.getRedditList(subreddit, sortType, after).await()
            }
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