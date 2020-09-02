package com.abhat.feed.subreddit

import com.abhat.feed.data.SubredditRepository
import com.abhat.feed.ui.state.subreddit.SubredditViewResult

/**
 * Created by Anirudh Uppunda on 02,September,2020
 */
class FakeSubredditRepositoryWhichThrowsError(private val exception: SubredditViewResult): SubredditRepository {
    override suspend fun getSubscribedSubreddits(
        headers: HashMap<String, String>,
        limit: Int
    ): SubredditViewResult {
        return exception
    }
}