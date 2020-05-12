package com.abhat.feed

import android.widget.RelativeLayout
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.model.Children
import com.abhat.core.model.ChildrenData
import com.abhat.core.model.Data
import com.abhat.core.model.RedditResponse
import com.abhat.feed.data.FeedRepositoryImpl
import com.abhat.feed.ui.FeedViewModel
import com.abhat.feed.ui.state.FeedViewResult
import com.abhat.feed.ui.state.FeedViewState
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.internal.matchers.Any

/**
 * Created by Anirudh Uppunda on 09,May,2020
 */
class FeedViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var feedViewModel: FeedViewModel
    private lateinit var feedRepositoryImpl: FeedRepositoryImpl
    private lateinit var observer: Observer<Pair<Boolean, RelativeLayout?>>

    @Before
    fun setup() {
        feedRepositoryImpl = mock()
        observer = mock()
        feedViewModel = FeedViewModel(feedRepositoryImpl, TestContextProvider())
    }

    @Test
    fun `calling get feed api should fire NSFW livedata for whichever post is over 18`() {
        runBlocking {
            whenever(feedRepositoryImpl.getFeed("all", "")).thenReturn(FeedViewResult.Success(returnData()))
            feedViewModel.isNsfwLiveData.observeForever(observer)
            feedViewModel.getFeed("all", "")
            verify(observer).onChanged(Pair(true, any()))
        }
    }



    class TestContextProvider : CoroutineContextProvider() {
        override val Main: CoroutineDispatcher = Dispatchers.Unconfined
        override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    }

    private fun returnData(): Data {
        return Data(
            null,
            null,
            mutableListOf<Children>(
                Children(
                    prepareAndReturnData(),
                    "",
                    1
                )
            ),
            "",
            ""
        )
    }

    private fun returnRedditResponse(): RedditResponse {
        return  RedditResponse(
            kind = "",
            data = Data(
                null,
                null,
                mutableListOf<Children>(
                    Children(
                    prepareAndReturnData(),
                        "",
                        1
                )
                ),
                "",
                ""
            )
        )
    }

    private fun prepareAndReturnData(): ChildrenData {

        return ChildrenData(false,
                "",
                "",
                true,
                listOf(),
                false,
                true,
                false,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                listOf(),
                0,
                0,
                0,
                "",
                false,
                "",
                0,
                "",
                null,
                0,
                "",
                "",
                "",
        null,
        null,
        null)
    }

}