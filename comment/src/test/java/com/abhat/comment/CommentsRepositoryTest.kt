package com.abhat.comment

import com.abhat.comment.data.CommentsApi
import com.abhat.comment.data.CommentsRepositoryImpl
import com.abhat.core.FakeRedditResponse
import com.abhat.core.model.RedditResponse
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by Anirudh Uppunda on 02,June,2020
 */
class CommentsRepositoryTest {

    private lateinit var commentsApi: CommentsApi

    private lateinit var commentsRepository: CommentsRepositoryImpl
    @Before
    fun setup() {
        commentsApi = FakeCommentsApi()
        commentsRepository = CommentsRepositoryImpl(commentsApi)
    }

    @Test
    fun `load comments returns successfully`() {
        runBlocking {
            Assert.assertEquals(getCommentsApi(), commentsRepository.loadPostDetails("all", "article"))
        }
    }

    private fun getCommentsApi(): List<RedditResponse> {
        return listOf(FakeRedditResponse.returnRedditResponse())
    }

    private class FakeCommentsApi: CommentsApi {
        override fun getPostDetails(
            subreddit: String,
            article: String
        ): Deferred<List<RedditResponse>> {
            return CompletableDeferred(listOf(FakeRedditResponse.returnRedditResponse()))
        }

    }
}