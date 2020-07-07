package com.abhat.feed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.feed.data.FeedRepository
import com.abhat.feed.ui.FeedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Anirudh Uppunda on 07,July,2020
 */
class FeedViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var feedViewModel: FeedViewModel
    private lateinit var feedRepository: FeedRepository

    class TestContextProvider : CoroutineContextProvider() {
        override val Main: CoroutineDispatcher = Dispatchers.Unconfined
        override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    }

    @Test
    fun `show "Best" option in sort type bottom fragment when applied on frontpage`() {
        feedRepository = FakeFeedRepository()
        feedViewModel = FeedViewModel(feedRepository, TestContextProvider())

        Assert.assertTrue(feedViewModel.shouldShowBestOptionInSortList(""))
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
}