package com.abhat.feed

import com.abhat.core.SortType.SortType
import com.abhat.core.model.TokenResponse
import com.abhat.feed.data.FeedRepository
import com.abhat.feed.ui.state.FeedViewResult

/**
 * Created by Anirudh Uppunda on 07,July,2020
 */
class FakeFeedRepository: FeedRepository {
    override suspend fun getFeed(subreddit: String, after: String, sortType: SortType): FeedViewResult? {
        return FeedViewResult.Success(null)
    }

    override suspend fun getFeedOauth(
        headers: Map<String, String>,
        subreddit: String,
        after: String,
        sortType: SortType
    ): FeedViewResult? {
        return FeedViewResult.Success(null)
    }

    override suspend fun getAccessToken(
        headers: HashMap<String, String>,
        fields: HashMap<String, String>
    ): TokenResponse? {
        TODO("Not yet implemented")
    }
}