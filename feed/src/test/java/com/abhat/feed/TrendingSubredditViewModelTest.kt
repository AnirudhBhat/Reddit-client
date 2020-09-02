package com.abhat.feed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.model.TrendingSubreddit
import com.abhat.feed.ui.state.TrendingSubredditViewResult
import com.abhat.feed.ui.state.TrendingSubredditViewState
import com.abhat.feed.ui.trendingsubreddit.TrendingSubredditViewModel
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
class TrendingSubredditViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var observer: Observer<TrendingSubredditViewState>

    class TestContextProvider : CoroutineContextProvider() {
        override val Main: CoroutineDispatcher = Dispatchers.Unconfined
        override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    }

    @Before
    fun setup() {
        observer = mock()
    }

    @Test
    fun `call to trending subreddit should give success response with list of trending subreddits`() {
        runBlocking {
            val trendingSubreddit = TrendingSubreddit(subredditNames = listOf("bestofnetflix", "askreddit", "androiddev"))
            val fakeTrendingSubredditRepository = FakeTrendingSubredditRepository(trendingSubreddit)
            val trendingSubredditViewModel = TrendingSubredditViewModel(fakeTrendingSubredditRepository, TestContextProvider())
            val expectedState = TrendingSubredditViewState(
                isLoading = false,
                trendingSubredditList = trendingSubreddit.subredditNames,
                error = null
            )
            trendingSubredditViewModel.trendingSubredditViewState.observeForever(observer)

            trendingSubredditViewModel.getTrendingSubreddits()
            Assert.assertEquals(expectedState, trendingSubredditViewModel.trendingSubredditViewState.value)
        }
    }

    @Test
    fun `call to trending subreddit should handle error response in case of network error`() {
        runBlocking {
            val exception = RuntimeException("Error!")
            val trendingSubredditErrorResult = TrendingSubredditViewResult.Error(Throwable(exception))
            val fakeTrendingSubredditRepository = FakeTrendingSubredditRepositoryWhichThrowsError(trendingSubredditErrorResult)
            val trendingSubredditViewModel = TrendingSubredditViewModel(fakeTrendingSubredditRepository, TestContextProvider())
            val expectedState = TrendingSubredditViewState(
                isLoading = false,
                trendingSubredditList = null,
                error = trendingSubredditErrorResult.throwable
            )
            trendingSubredditViewModel.trendingSubredditViewState.observeForever(observer)

            trendingSubredditViewModel.getTrendingSubreddits()
            Assert.assertEquals(expectedState, trendingSubredditViewModel.trendingSubredditViewState.value)
        }
    }

    @Test
    fun `call to trending subreddit should show loading, give success response and then hide loading with list of trending subreddits`() {
        runBlocking {
            val trendingSubreddit = TrendingSubreddit(subredditNames = listOf("bestofnetflix", "askreddit", "androiddev"))
            val fakeTrendingSubredditRepository = FakeTrendingSubredditRepository(trendingSubreddit)
            val trendingSubredditViewModel = TrendingSubredditViewModel(fakeTrendingSubredditRepository, TestContextProvider())
            val expectedState = TrendingSubredditViewState(
                isLoading = false,
                trendingSubredditList = trendingSubreddit.subredditNames,
                error = null
            )
            val loadingState = TrendingSubredditViewState(
                isLoading = true,
                trendingSubredditList = null,
                error = null
            )
            trendingSubredditViewModel.trendingSubredditViewState.observeForever(observer)

            trendingSubredditViewModel.getTrendingSubreddits()

            val inOrder = inOrder(observer)
            inOrder.verify(observer).onChanged(loadingState)
            inOrder.verify(observer).onChanged(expectedState)
        }
    }

    @Test
    fun `call to trending subreddit should show progress bar, handle error response and hide progress bar in case of network error`() {
        runBlocking {
            val exception = RuntimeException("Error!")
            val trendingSubredditErrorResult = TrendingSubredditViewResult.Error(Throwable(exception))
            val fakeTrendingSubredditRepository = FakeTrendingSubredditRepositoryWhichThrowsError(trendingSubredditErrorResult)
            val trendingSubredditViewModel = TrendingSubredditViewModel(fakeTrendingSubredditRepository, TestContextProvider())
            val expectedState = TrendingSubredditViewState(
                isLoading = false,
                trendingSubredditList = null,
                error = trendingSubredditErrorResult.throwable
            )
            val loadingState = TrendingSubredditViewState(
                isLoading = true,
                trendingSubredditList = null,
                error = null
            )
            trendingSubredditViewModel.trendingSubredditViewState.observeForever(observer)

            trendingSubredditViewModel.getTrendingSubreddits()

            val inOrder = inOrder(observer)
            inOrder.verify(observer).onChanged(loadingState)
            inOrder.verify(observer).onChanged(expectedState)
        }
    }
}