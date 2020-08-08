package com.abhat.feed.data

import com.abhat.core.RedditApi
import com.abhat.core.SortType.SortType
import com.abhat.core.model.RedditResponse
import com.abhat.core.model.TokenResponse
import com.abhat.core.network.HostSelectionInterceptor
import com.abhat.feed.ui.state.FeedViewResult
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import retrofit2.HttpException

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
open class FeedRepositoryImpl(val redditApi: RedditApi): FeedRepository, KoinComponent {

    private val hostSelectionInterceptor: HostSelectionInterceptor by inject()

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

    override suspend fun getFeedOauth(
        headers: Map<String, String>,
        subreddit: String,
        after: String,
        sortType: SortType
    ): FeedViewResult? {
        hostSelectionInterceptor.host = "www.oauth.reddit.com"
        try {
            val response: RedditResponse = if ( subreddit.isNullOrEmpty() || subreddit.equals("frontpage", ignoreCase = true)) {
                when (sortType) {
                    SortType.empty -> {
                        redditApi.getRedditFrontPageOauth(headers, sortType = "", after = after).await()
                    }

                    SortType.hot, SortType.best, SortType.rising, SortType.new -> {
                        redditApi.getRedditFrontPageOauth(headers, sortType.name, after).await()
                    }
                }
            } else {
                redditApi.getRedditListPost(headers, subreddit, sortType, after).await()
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

    override suspend fun getAccessToken(
        headers: HashMap<String, String>,
        fields: HashMap<String, String>
    ): TokenResponse? {
        val response = redditApi.getAccessToken(headers, fields).await()
        return response.body()
    }
}