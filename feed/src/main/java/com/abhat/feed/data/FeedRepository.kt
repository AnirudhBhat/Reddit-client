package com.abhat.feed.data

import com.abhat.core.model.PostDetailResponse

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
interface FeedRepository {
    suspend fun getFeed(subreddit: String): PostDetailResponse
}