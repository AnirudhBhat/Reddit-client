package com.abhat.feed.data

import com.abhat.core.model.RedditResponse
import com.abhat.feed.ui.state.subreddit.SubredditViewResult

/**
 * Created by Anirudh Uppunda on 02,September,2020
 */
interface SubredditRepository {
    suspend fun getSubscribedSubreddits(headers: HashMap<String, String>, limit: Int = 100): SubredditViewResult
}