package com.abhat.feed

import com.abhat.feed.data.TrendingSubredditRepository
import com.abhat.feed.ui.state.TrendingSubredditViewResult

/**
 * Created by Anirudh Uppunda on 02,September,2020
 */
class FakeTrendingSubredditRepositoryWhichThrowsError(private val exception: TrendingSubredditViewResult): TrendingSubredditRepository {
    override suspend fun getTrendingSubreddit(): TrendingSubredditViewResult {
        return exception
    }
}