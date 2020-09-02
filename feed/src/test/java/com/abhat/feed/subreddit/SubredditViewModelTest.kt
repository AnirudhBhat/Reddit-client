package com.abhat.feed.subreddit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.abhat.core.FakeRedditResponse
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.feed.ui.state.TrendingSubredditViewResult
import com.abhat.feed.ui.state.subreddit.SubredditViewResult
import com.abhat.feed.ui.state.subreddit.SubredditViewState
import com.abhat.feed.ui.subreddit.SubredditViewModel
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Anirudh Uppunda on 02,September,2020
 */
class SubredditViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var observer: Observer<SubredditViewState>

    class TestContextProvider : CoroutineContextProvider() {
        override val Main: CoroutineDispatcher = Dispatchers.Unconfined
        override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    }

    @Before
    fun setup() {
        observer = mock()
    }

    @Test
    fun `call to subreddit should give success response with list of subreddits`() {
        runBlocking {
            val trendingSubreddit = FakeRedditResponse.returnRedditResponse()
            val subredditNames = listOf("androiddev", "askreddit", "kannada", "pics", "programming")
            val fakeTrendingSubredditRepository = FakeSubredditRepository(trendingSubreddit)
            val subredditViewModel = SubredditViewModel(fakeTrendingSubredditRepository, TestContextProvider())
            val expectedState = SubredditViewState(
                isLoading = false,
                subredditList = subredditNames,
                error = null
            )
            subredditViewModel.subredditViewState.observeForever(observer)

            subredditViewModel.getSubscribedSubreddits(hashMapOf(), 100)
            Assert.assertEquals(expectedState, subredditViewModel.subredditViewState.value)
        }
    }

    @Test
    fun `call to subreddit should show progress bar, handle error response and hide progress bar in case of network error`() {
        runBlocking {
            val exception = RuntimeException("Error!")
            val subredditErrorResult = SubredditViewResult.Error(Throwable(exception))
            val fakeTrendingSubredditRepository = FakeSubredditRepositoryWhichThrowsError(subredditErrorResult)
            val subredditViewModel = SubredditViewModel(fakeTrendingSubredditRepository, TestContextProvider())
            val expectedState = SubredditViewState(
                isLoading = false,
                subredditList = null,
                error = subredditErrorResult.throwable
            )
            subredditViewModel.subredditViewState.observeForever(observer)

            subredditViewModel.getSubscribedSubreddits(hashMapOf(), 100)
            Assert.assertEquals(expectedState, subredditViewModel.subredditViewState.value)
        }
    }

    @Test
    fun `call to subreddit should show loading, give success response and then hide loading with list of subreddits`() {
        runBlocking {
            val trendingSubreddit = FakeRedditResponse.returnRedditResponse()
            val subredditNames = listOf("androiddev", "askreddit", "kannada", "pics", "programming")
            val fakeTrendingSubredditRepository = FakeSubredditRepository(trendingSubreddit)
            val subredditViewModel = SubredditViewModel(fakeTrendingSubredditRepository, TestContextProvider())
            val expectedState = SubredditViewState(
                isLoading = false,
                subredditList = subredditNames,
                error = null
            )
            val loadingState = SubredditViewState(
                isLoading = true,
                subredditList = null,
                error = null
            )
            subredditViewModel.subredditViewState.observeForever(observer)

            subredditViewModel.getSubscribedSubreddits(hashMapOf(), 100)

            val inOrder = inOrder(observer)
            inOrder.verify(observer).onChanged(loadingState)
            inOrder.verify(observer).onChanged(expectedState)
        }
    }

    @Test
    fun `call to subreddit should should handle error response in case of network error`() {
        runBlocking {
            val exception = RuntimeException("Error!")
            val subredditErrorResult = SubredditViewResult.Error(Throwable(exception))
            val fakeTrendingSubredditRepository = FakeSubredditRepositoryWhichThrowsError(subredditErrorResult)
            val subredditViewModel = SubredditViewModel(fakeTrendingSubredditRepository, TestContextProvider())
            val expectedState = SubredditViewState(
                isLoading = false,
                subredditList = null,
                error = subredditErrorResult.throwable
            )
            val loadingState = SubredditViewState(
                isLoading = true,
                subredditList = null,
                error = null
            )
            subredditViewModel.subredditViewState.observeForever(observer)

            subredditViewModel.getSubscribedSubreddits(hashMapOf(), 100)
            val inOrder = inOrder(observer)
            inOrder.verify(observer).onChanged(loadingState)
            inOrder.verify(observer).onChanged(expectedState)
        }
    }
}