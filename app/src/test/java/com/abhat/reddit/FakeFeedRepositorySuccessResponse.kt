package com.abhat.reddit

import com.abhat.feed.data.FeedRepository
import com.abhat.feed.ui.state.FeedViewResult

/**
 * Created by Anirudh Uppunda on 25,May,2020
 */
class FakeFeedRepositorySuccessResponse: FeedRepository {
    override suspend fun getFeed(subreddit: String, after: String): FeedViewResult? {
        return FeedViewResult.Success(FakeRedditResponse.returnRedditResponse())
    }
}