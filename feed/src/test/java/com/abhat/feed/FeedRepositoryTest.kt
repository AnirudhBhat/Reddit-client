package com.abhat.feed

import com.abhat.core.RedditApi
import com.abhat.core.SortType.SortType
import com.abhat.feed.data.FeedRepositoryImpl
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Created by Anirudh Uppunda on 12,July,2020
 */
class FeedRepositoryTest {

    @Test
    fun `when sort type is empty and subreddit is either empty or null, call reddit's frontpage`() {
        runBlocking {
            val fakeRedditApi: RedditApi = mock()
            val feedRepository = FeedRepositoryImpl(fakeRedditApi)

            feedRepository.getFeed("", "", SortType.empty)
            verify(fakeRedditApi).getRedditFrontPage("", "")
        }
    }

    @Test
    fun `when sort type is best and subreddit is either empty or null, call reddit's frontpage with proper sorttype`() {
        runBlocking {
            val fakeRedditApi: RedditApi = mock()
            val feedRepository = FeedRepositoryImpl(fakeRedditApi)

            feedRepository.getFeed("", "", SortType.best)
            verify(fakeRedditApi).getRedditFrontPage("best", "")
        }
    }

    @Test
    fun `when sort type is best and subreddit is NOT empty or null, call reddit's subreddit list api`() {
        runBlocking {
            val fakeRedditApi: RedditApi = mock()
            val feedRepository = FeedRepositoryImpl(fakeRedditApi)

            feedRepository.getFeed("all", "", SortType.best)
            verify(fakeRedditApi).getRedditList("all", SortType.best)
        }
    }

    @Test
    fun `when sort type is rising and subreddit is NOT empty or null, call reddit's subreddit list api`() {
        runBlocking {
            val fakeRedditApi: RedditApi = mock()
            val feedRepository = FeedRepositoryImpl(fakeRedditApi)

            feedRepository.getFeed("all", "", SortType.rising)
            verify(fakeRedditApi).getRedditList("all", SortType.rising)
        }
    }
}