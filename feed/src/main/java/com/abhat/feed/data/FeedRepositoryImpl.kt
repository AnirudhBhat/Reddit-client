package com.abhat.feed.data

import com.abhat.core.RedditApi
import com.abhat.core.model.PostDetailResponse

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
class FeedRepositoryImpl(val redditApi: RedditApi): FeedRepository {
    override suspend fun getFeed(subreddit: String): PostDetailResponse {
        return redditApi.getRedditList(subreddit).await()
    }
}