package com.abhat.search.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.abhat.core.FakeRedditResponse
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.search.data.SearchRepository
import com.abhat.search.fakes.FakeSearchRepository
import com.abhat.search.fakes.FakeSearchRepositoryWhichThrowsError
import com.abhat.search.ui.Action
import com.abhat.search.ui.SearchViewModel
import com.abhat.search.ui.UIState
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Anirudh Uppunda on 23,October,2020
 */
class SearchViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var searchRepository: SearchRepository
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchObserver: Observer<UIState>

    class TestContextProvider : CoroutineContextProvider() {
        override val Main: CoroutineDispatcher = Dispatchers.Unconfined
        override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    }

    @Before
    fun setup() {
        searchObserver = mock()
    }


    @Test
    fun `searching a subreddit should show loading, return proper response and hide loading`() {
        runBlocking {
            searchRepository = FakeSearchRepository()
            searchViewModel = SearchViewModel(searchRepository, TestContextProvider())
            searchViewModel.getUIState().observeForever(searchObserver)
            val loadingTrueState = UIState(isLoading = true, success = null, error = null)
            val successState = UIState(isLoading = false, success = listOf("androiddev",
                                                                            "askreddit",
                                                                            "kannada",
                                                                            "pics",
                                                                            "programming"), error = null)

            searchViewModel.onAction(Action.Search("android"))

            val inOrder  = inOrder(searchObserver)
            inOrder.verify(searchObserver).onChanged(loadingTrueState)
            inOrder.verify(searchObserver).onChanged(successState)
        }

    }


    @Test
    fun `searching a subreddit should show loading, after failing return error response and hide loading`() {
        runBlocking {
            val exception = RuntimeException()
            searchRepository = FakeSearchRepositoryWhichThrowsError(exception)
            searchViewModel = SearchViewModel(searchRepository, TestContextProvider())
            searchViewModel.getUIState().observeForever(searchObserver)
            val loadingTrueState = UIState(isLoading = true, success = null, error = null)
            val failureState = UIState(isLoading = false, success = null, error = exception)

            searchViewModel.onAction(Action.Search("android"))

            val inOrder  = inOrder(searchObserver)
            inOrder.verify(searchObserver).onChanged(loadingTrueState)
            inOrder.verify(searchObserver).onChanged(failureState)
        }

    }
}