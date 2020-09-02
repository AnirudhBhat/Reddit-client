package com.abhat.feed.subreddit

import com.abhat.feed.FakeRedditApi
import com.abhat.feed.data.SubredditRepositoryImpl
import com.abhat.feed.data.TrendingSubredditRepositoryImpl
import com.abhat.feed.ui.state.TrendingSubredditViewResult
import com.abhat.feed.ui.state.subreddit.SubredditViewResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

/**
 * Created by Anirudh Uppunda on 01,September,2020
 */
class SubredditRepositoryTest {

    @Test
    fun `when subreddit api returns successfully, subreddit view state should contain proper data`() {
        runBlocking {
            val redditApi = FakeRedditApi()
            val headers = hashMapOf<String, String>()
            val expectedSuccessResponse = SubredditViewResult.Success(redditApi.getSubscribedSubreddits(
                headers, 100).await())
            val subredditRepository = SubredditRepositoryImpl(redditApi)
            Assert.assertEquals(expectedSuccessResponse, subredditRepository.getSubscribedSubreddits(headers, 100))
        }
    }
}