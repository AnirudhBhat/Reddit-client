package com.abhat.comment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.abhat.comment.data.CommentsRepository
import com.abhat.comment.ui.Action
import com.abhat.comment.ui.CommentsViewModel
import com.abhat.comment.ui.UIState
import com.abhat.core.FakeRedditResponse
import com.abhat.core.common.CoroutineContextProvider
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Anirudh Uppunda on 02,June,2020
 */
class CommentsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var commentRepository: CommentsRepository
    private lateinit var postDetailsObserver: Observer<UIState>
    private lateinit var commentsViewModel: CommentsViewModel

    @Before
    fun setup() {
        commentRepository = FakeCommentsRepository()
        postDetailsObserver = mock()
        commentsViewModel = CommentsViewModel(commentRepository, TestContextProvider())
    }

    class TestContextProvider : CoroutineContextProvider() {
        override val Main: CoroutineDispatcher = Dispatchers.Unconfined
        override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    }

    @Test
    fun `loadPostDetails should show loading, and after returning successfully should hide loading`() {
        runBlocking {
            commentsViewModel.getUIState().observeForever(postDetailsObserver)
            val loadingTrueState = UIState(isLoading = true, success = null, error = null)
            val successState = UIState(isLoading = false, success = FakeRedditResponse.returnRedditPostDetailResponse()[1], error = null)

            commentsViewModel.onAction(Action.LoadPostDetails("subreddit", "article"))

            val inOrder  = inOrder(postDetailsObserver)
            inOrder.verify(postDetailsObserver).onChanged(loadingTrueState)
            inOrder.verify(postDetailsObserver).onChanged(successState)
        }
    }

    @Test
    fun `loadPostDetails should show loading, and after failing, return error and  should hide loading`() {
        runBlocking {
            val fakeCommentsRepository = FakeCommentsRepository(true)
            val commentsViewModel = CommentsViewModel(fakeCommentsRepository, TestContextProvider())
            commentsViewModel.getUIState().observeForever(postDetailsObserver)

            val loadingTrueState = UIState(isLoading = true, success = null, error = null)
            val failureState = UIState(isLoading = false, success = null, error = RuntimeException("Error!"))

            commentsViewModel.onAction(Action.LoadPostDetails("subreddit", "article"))

            verify(postDetailsObserver).onChanged(loadingTrueState)
            Assert.assertEquals(failureState.error?.message, commentsViewModel.getUIState()?.value?.error?.message)
            Assert.assertEquals(failureState.isLoading, commentsViewModel.getUIState()?.value?.isLoading)
            Assert.assertEquals(failureState.success, commentsViewModel.getUIState()?.value?.success)
        }
    }

    @Test
    fun `when current position is 0, get the next parent comment's position correctly`() {
        runBlocking {
            val fakeCommentsRepository = FakeCommentsRepository()
            val commentsViewModel = CommentsViewModel(fakeCommentsRepository, TestContextProvider())
            Assert.assertEquals(2, commentsViewModel.getNextParentCommentPosition(0, FakeRedditResponse.returnChildrens()))
        }
    }

    @Test
    fun `when current position is 2, get the next parent comment's position correctly`() {
        runBlocking {
            val fakeCommentsRepository = FakeCommentsRepository()
            val commentsViewModel = CommentsViewModel(fakeCommentsRepository, TestContextProvider())
            Assert.assertEquals(4, commentsViewModel.getNextParentCommentPosition(2, FakeRedditResponse.returnChildrens()))
        }
    }

    @Test
    fun `when current position is 4, get the next parent comment's position correctly`() {
        runBlocking {
            val fakeCommentsRepository = FakeCommentsRepository()
            val commentsViewModel = CommentsViewModel(fakeCommentsRepository, TestContextProvider())
            Assert.assertEquals(10, commentsViewModel.getNextParentCommentPosition(4, FakeRedditResponse.returnChildrens()))
        }
    }
}