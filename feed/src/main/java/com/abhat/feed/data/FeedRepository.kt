package com.abhat.feed.data

import com.abhat.core.SortType.SortType
import com.abhat.feed.ui.state.FeedViewResult

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
interface FeedRepository {
    suspend fun getFeed(subreddit: String, after: String, sortType: SortType): FeedViewResult?
}