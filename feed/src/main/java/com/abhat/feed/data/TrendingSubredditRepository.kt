package com.abhat.feed.data

import com.abhat.feed.ui.state.TrendingSubredditViewResult

/**
 * Created by Anirudh Uppunda on 01,September,2020
 */
interface TrendingSubredditRepository {
    suspend fun getTrendingSubreddit(): TrendingSubredditViewResult
}