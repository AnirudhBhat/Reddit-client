package com.abhat.feed.data

import com.abhat.core.SortType.SortType
import com.abhat.core.model.TokenResponse
import com.abhat.feed.ui.state.FeedViewResult

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
interface FeedRepository {
    suspend fun getFeed(subreddit: String, after: String, sortType: SortType): FeedViewResult?

    suspend fun getFeedOauth(headers: Map<String, String>, subreddit: String, after: String, sortType: SortType): FeedViewResult?

    suspend fun getAccessToken(
        headers: HashMap<String, String>,
        fields: HashMap<String, String>
    ): TokenResponse?
}