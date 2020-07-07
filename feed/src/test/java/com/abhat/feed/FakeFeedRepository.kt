package com.abhat.feed

import com.abhat.feed.data.FeedRepository
import com.abhat.feed.ui.state.FeedViewResult

/**
 * Created by Anirudh Uppunda on 07,July,2020
 */
class FakeFeedRepository: FeedRepository {
    override suspend fun getFeed(subreddit: String, after: String): FeedViewResult? {
        return FeedViewResult.Success(null)
    }
}