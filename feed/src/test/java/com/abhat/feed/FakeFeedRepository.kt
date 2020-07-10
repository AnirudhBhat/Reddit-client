package com.abhat.feed

import com.abhat.core.SortType.SortType
import com.abhat.feed.data.FeedRepository
import com.abhat.feed.ui.state.FeedViewResult

/**
 * Created by Anirudh Uppunda on 07,July,2020
 */
class FakeFeedRepository: FeedRepository {
    override suspend fun getFeed(subreddit: String, after: String, sortType: SortType): FeedViewResult? {
        return FeedViewResult.Success(null)
    }
}