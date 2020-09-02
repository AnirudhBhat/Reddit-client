package com.abhat.feed

import com.abhat.core.model.TrendingSubreddit
import com.abhat.feed.data.TrendingSubredditRepository
import com.abhat.feed.ui.state.TrendingSubredditViewResult

/**
 * Created by Anirudh Uppunda on 02,September,2020
 */
class FakeTrendingSubredditRepository(private val trendingSubredditList: TrendingSubreddit): TrendingSubredditRepository {
    override suspend fun getTrendingSubreddit(): TrendingSubredditViewResult {
        return TrendingSubredditViewResult.Success(trendingSubreddit = trendingSubredditList)
    }
}