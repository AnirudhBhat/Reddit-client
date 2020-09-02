package com.abhat.feed.subreddit

import com.abhat.core.model.RedditResponse
import com.abhat.feed.data.SubredditRepository
import com.abhat.feed.ui.state.subreddit.SubredditViewResult

/**
 * Created by Anirudh Uppunda on 02,September,2020
 */
class FakeSubredditRepository(private val subredditResponse: RedditResponse): SubredditRepository {
    override suspend fun getSubscribedSubreddits(
        headers: HashMap<String, String>,
        limit: Int
    ): SubredditViewResult {
        return SubredditViewResult.Success(subredditList = subredditResponse)
    }
}