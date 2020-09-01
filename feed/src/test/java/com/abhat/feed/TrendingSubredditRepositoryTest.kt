package com.abhat.feed

import com.abhat.feed.data.TrendingSubredditRepositoryImpl
import com.abhat.feed.ui.state.TrendingSubredditViewResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

/**
 * Created by Anirudh Uppunda on 01,September,2020
 */
class TrendingSubredditRepositoryTest {

    @Test
    fun `when trending subreddit api returns successfully, trending view state should contain proper data`() {
        runBlocking {
            val redditApi = FakeRedditApi()
            val expectedSuccessResponse = TrendingSubredditViewResult.Success(redditApi.getTrendingSubreddits().await())
            val trendingSubredditRepository = TrendingSubredditRepositoryImpl(redditApi)
            Assert.assertEquals(expectedSuccessResponse, trendingSubredditRepository.getTrendingSubreddit())
        }
    }
}