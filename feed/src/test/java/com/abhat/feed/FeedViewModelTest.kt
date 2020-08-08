package com.abhat.feed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.abhat.core.FakeRedditResponse
import com.abhat.core.SortType.SortType
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.model.TokenEntity
import com.abhat.feed.data.FeedRepository
import com.abhat.feed.ui.FeedViewModel
import com.abhat.feed.ui.state.FeedViewResult
import com.abhat.feed.ui.state.FeedViewState
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.RuntimeException

/**
 * Created by Anirudh Uppunda on 07,July,2020
 */
class FeedViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var feedViewModel: FeedViewModel
    private lateinit var feedRepository: FeedRepository
    private lateinit var feedObserver: Observer<FeedViewState>

    class TestContextProvider : CoroutineContextProvider() {
        override val Main: CoroutineDispatcher = Dispatchers.Unconfined
        override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    }

    @Before
    fun setup() {
        feedObserver = mock()
    }

    @Test
    fun `show "Best" option in sort type bottom fragment when applied on frontpage`() {
        feedRepository = FakeFeedRepository()
        feedViewModel = FeedViewModel(feedRepository, TestContextProvider())

        Assert.assertTrue(feedViewModel.shouldShowBestOptionInSortList(""))
    }

    @Test
    fun `show "Best" option in sort type bottom fragment when applied on frontpage (subreddit string is 'frontpage')`() {
        feedRepository = FakeFeedRepository()
        feedViewModel = FeedViewModel(feedRepository, TestContextProvider())

        Assert.assertTrue(feedViewModel.shouldShowBestOptionInSortList("frontpage"))
    }

    @Test
    fun `show "Best" option in sort type bottom fragment when applied on frontpage (when nothing is passed as parameter)`() {
        feedRepository = FakeFeedRepository()
        feedViewModel = FeedViewModel(feedRepository, TestContextProvider())

        Assert.assertTrue(feedViewModel.shouldShowBestOptionInSortList())
    }

    @Test
    fun `do NOT show "Best" option in sort type bottom fragment when applied on any other subreddit apart from frontpage`() {
        feedRepository = FakeFeedRepository()
        feedViewModel = FeedViewModel(feedRepository, TestContextProvider())

        Assert.assertFalse(feedViewModel.shouldShowBestOptionInSortList("all"))
    }

    @Test
    fun `selecting sort type as hot should return same sort type in feed UI state after successful response`() {
        val feedRepository = FakeFeedRepositorySuccessResponse()
        val feedViewModel = FeedViewModel(feedRepository, TestContextProvider())
        val feedUIState = FeedViewState(
            isLoading = false,
            feedList =  FakeRedditResponse.returnRedditResponse(),
            sortType = SortType.hot,
            subreddit = "",
            sortList = listOf(SortType.best, SortType.hot, SortType.new, SortType.rising),
            error = null,
            authorizationError = null
        )
        feedViewModel.feedViewState.observeForever(feedObserver)
        feedViewModel.getFeed(subreddit = "", sortType = SortType.hot, after = "")
        Assert.assertEquals(feedUIState, feedViewModel.feedViewState.value)
    }

    @Test
    fun `selecting sort type as rising should return same sort type in feed UI state after successful response`() {
        val feedRepository = FakeFeedRepositorySuccessResponse()
        val feedViewModel = FeedViewModel(feedRepository, TestContextProvider())
        val feedUIState = FeedViewState(
            isLoading = false,
            feedList =  FakeRedditResponse.returnRedditResponse(),
            sortType = SortType.rising,
            subreddit = "",
            sortList = listOf(SortType.best, SortType.hot, SortType.new, SortType.rising),
            error = null,
            authorizationError = null
        )
        feedViewModel.feedViewState.observeForever(feedObserver)
        feedViewModel.getFeed(subreddit = "", sortType = SortType.rising, after = "")
        Assert.assertEquals(feedUIState, feedViewModel.feedViewState.value)
    }

    @Test
    fun `selecting subreddit as anything other than frontpage should hide 'best' in feed UI state after successful response`() {
        val feedRepository = FakeFeedRepositorySuccessResponse()
        val feedViewModel = FeedViewModel(feedRepository, TestContextProvider())
        val feedUIState = FeedViewState(
            isLoading = false,
            feedList =  FakeRedditResponse.returnRedditResponse(),
            sortType = SortType.rising,
            subreddit = "androiddev",
            sortList = listOf(SortType.hot, SortType.new, SortType.rising),
            error = null,
            authorizationError = null
        )
        feedViewModel.feedViewState.observeForever(feedObserver)
        feedViewModel.getFeed(subreddit = "androiddev", sortType = SortType.rising, after = "")
        Assert.assertEquals(feedUIState, feedViewModel.feedViewState.value)
    }

    @Test
    fun `selecting subreddit as frontpage should show 'best' in feed UI state after successful response`() {
        val feedRepository = FakeFeedRepositorySuccessResponse()
        val feedViewModel = FeedViewModel(feedRepository, TestContextProvider())
        val feedUIState = FeedViewState(
            isLoading = false,
            feedList =  FakeRedditResponse.returnRedditResponse(),
            sortType = SortType.rising,
            subreddit = "",
            sortList = listOf(SortType.best, SortType.hot, SortType.new, SortType.rising),
            error = null,
            authorizationError = null
        )
        feedViewModel.feedViewState.observeForever(feedObserver)
        feedViewModel.getFeed(subreddit = "", sortType = SortType.rising, after = "")
        Assert.assertEquals(feedUIState, feedViewModel.feedViewState.value)
    }

    @Test
    fun `selecting subreddit as androiddev should return same subreddit type in feed UI state after successful response`() {
        val feedRepository = FakeFeedRepositorySuccessResponse()
        val feedViewModel = FeedViewModel(feedRepository, TestContextProvider())
        val feedUIState = FeedViewState(
            isLoading = false,
            feedList =  FakeRedditResponse.returnRedditResponse(),
            sortType = SortType.rising,
            subreddit = "androiddev",
            sortList = listOf(SortType.hot, SortType.new, SortType.rising),
            error = null,
            authorizationError = null
        )
        feedViewModel.feedViewState.observeForever(feedObserver)
        feedViewModel.getFeed(subreddit = "androiddev", sortType = SortType.rising, after = "")
        Assert.assertEquals(feedUIState, feedViewModel.feedViewState.value)
    }

    @Test
    fun `getFeed should show loading, and after returning successfully should hide loading`() {
        val feedRepository = FakeFeedRepositorySuccessResponse()
        val feedViewModel = FeedViewModel(feedRepository, TestContextProvider())
        val loadingState = FeedViewState(
            isLoading = true,
            subreddit = "all"
        )

        val successState = FeedViewState(
            isLoading = false,
            feedList =  FakeRedditResponse.returnRedditResponse(),
            sortType = SortType.rising,
            subreddit = "androiddev",
            sortList = listOf(SortType.hot, SortType.new, SortType.rising),
            error = null,
            authorizationError = null
        )
        feedViewModel.feedViewState.observeForever(feedObserver)
        feedViewModel.getFeed(subreddit = "androiddev", sortType = SortType.rising, after = "")

        val inOrder = inOrder(feedObserver)
        inOrder.verify(feedObserver).onChanged(loadingState)
        inOrder.verify(feedObserver).onChanged(successState)
    }

    @Test
    fun `getFeed should show loading, and after after failing, return error and  should hide loading`() {
        val feedViewResult = FeedViewResult.Error.NetworkError(Throwable(RuntimeException("Error!")))
        val feedRepository = FakeFeedRepositoryWhichThrowsError(feedViewResult)
        val feedViewModel = FeedViewModel(feedRepository, TestContextProvider())
        val loadingState = FeedViewState(
            isLoading = true,
            subreddit = "all"
        )

        val errorState = FeedViewState(
            isLoading = false,
            subreddit = "all",
            error = feedViewResult.throwable
        )
        feedViewModel.feedViewState.observeForever(feedObserver)
        feedViewModel.getFeed(subreddit = "all", after = "", sortType = SortType.hot)

        val inOrder = inOrder(feedObserver)
        inOrder.verify(feedObserver).onChanged(loadingState)
        inOrder.verify(feedObserver).onChanged(errorState)
    }

    @Test
    fun `Return sort type as 'best' when sort type is empty and subreddit is frontpage`() {
        val feedRepository = FakeFeedRepositorySuccessResponse()
        val feedViewModel = FeedViewModel(feedRepository, TestContextProvider())

        Assert.assertEquals(SortType.best, feedViewModel.returnSortType(SortType.empty, "frontpage"))
    }

    @Test
    fun `Return sort type as 'best' when sort type is empty and subreddit is empty`() {
        val feedRepository = FakeFeedRepositorySuccessResponse()
        val feedViewModel = FeedViewModel(feedRepository, TestContextProvider())

        Assert.assertEquals(SortType.best, feedViewModel.returnSortType(SortType.empty, ""))
    }

    @Test
    fun `Return sort type as 'hot' when given sort type is hot and subreddit is anything other than frontpage`() {
        val feedRepository = FakeFeedRepositorySuccessResponse()
        val feedViewModel = FeedViewModel(feedRepository, TestContextProvider())

        Assert.assertEquals(SortType.hot, feedViewModel.returnSortType(SortType.hot, "all"))
    }

    @Test
    fun `Return sort type as 'rising' when given sort type is rising and subreddit is anything other than frontpage`() {
        val feedRepository = FakeFeedRepositorySuccessResponse()
        val feedViewModel = FeedViewModel(feedRepository, TestContextProvider())

        Assert.assertEquals(SortType.rising, feedViewModel.returnSortType(SortType.rising, "all"))
    }

    @Test
    fun `when token is null, return constant to indicate that user has not logged in`() {
        val tokenEntity = null
        val feedRepository = FakeFeedRepositorySuccessResponse()
        val feedViewModel = FeedViewModel(feedRepository, TestContextProvider())
        Assert.assertEquals(FeedViewModel.FetchTokenFrom.USER_NOT_LOGGED_IN, feedViewModel.howShouldWeFetchTheToken(tokenEntity))
    }

    @Test
    fun `when token is expired, return constant to indicate that we have to make request to fetch refresh token`() {
        val feedRepository = FakeFeedRepositorySuccessResponse()
        val feedViewModel = FeedViewModel(feedRepository, TestContextProvider())
        val expiredTokenTime = Calendar.getInstance()
        expiredTokenTime.add(Calendar.MINUTE, -5)
        val tokenEntity = getFakeNewTokenEntity(expiredTokenTime)
        Assert.assertEquals(FeedViewModel.FetchTokenFrom.REFRESH_TOKEN_API, feedViewModel.howShouldWeFetchTheToken(tokenEntity))
    }

    @Test
    fun `when token is NOT expired, return constant to indicate that we have to fetch it from shared preference`() {
        val feedRepository = FakeFeedRepositorySuccessResponse()
        val feedViewModel = FeedViewModel(feedRepository, TestContextProvider())
        val expiredTokenTime = Calendar.getInstance()
        expiredTokenTime.add(Calendar.MINUTE, 5)
        val tokenEntity = getFakeNewTokenEntity(expiredTokenTime)
        Assert.assertEquals(FeedViewModel.FetchTokenFrom.SHARED_PREFERENCE, feedViewModel.howShouldWeFetchTheToken(tokenEntity))
    }

    private fun getFakeNewTokenEntity(expiry: Calendar): TokenEntity {
        return TokenEntity(
            refresh_token = "",
            scope = "",
            access_token = "",
            expiry = expiry,
            active = 0
        )
    }
}