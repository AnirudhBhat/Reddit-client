package com.abhat.comment

import com.abhat.comment.data.CommentsApi
import com.abhat.comment.data.CommentsRepositoryImpl
import com.abhat.core.FakeRedditResponse
import com.abhat.core.RedditApi
import com.abhat.core.SortType.SortType
import com.abhat.core.model.PostDetailResponse
import com.abhat.core.model.RedditResponse
import com.abhat.core.model.TokenResponse
import com.abhat.core.model.TrendingSubreddit
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.http.Path

/**
 * Created by Anirudh Uppunda on 02,June,2020
 */
class CommentsRepositoryTest {

    private lateinit var commentsApi: RedditApi

    private lateinit var commentsRepository: CommentsRepositoryImpl
    @Before
    fun setup() {
        commentsApi = FakeCommentsApi()
        commentsRepository = CommentsRepositoryImpl(commentsApi, null)
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

    private class FakeCommentsApi: RedditApi {
        override fun getPostDetails(
            subreddit: String,
            article: String
        ): Deferred<List<RedditResponse>> {
            return CompletableDeferred(listOf(FakeRedditResponse.returnRedditResponse()))
        }

        override fun getPostDetailsPost(
            headers: Map<String, String>,
            subreddit: String,
            article: String
        ): Deferred<List<PostDetailResponse>> {
            TODO("Not yet implemented")
        }

        override fun getRedditList(subreddit: String, sortType: SortType, after: String): Deferred<RedditResponse> {
            TODO("Not yet implemented")
        }

        override fun getRedditListPost(
            headers: Map<String, String>,
            subreddit: String,
            sortType: SortType,
            after: String
        ): Deferred<RedditResponse> {
            TODO("Not yet implemented")
        }

        override fun getTrendingSubreddits(): Deferred<TrendingSubreddit> {
            TODO("Not yet implemented")
        }

        override fun getSubscribedSubreddits(
            headers: Map<String, String>,
            limit: Int
        ): Deferred<RedditResponse> {
            return CompletableDeferred(FakeRedditResponse.returnRedditResponse())
        }

        override fun getRedditFrontPage(
            @Path("sortType") sortType: String,
            after: String
        ): Deferred<RedditResponse> {
            TODO("Not yet implemented")
        }

        override fun getRedditFrontPageOauth(
            headers: Map<String, String>,
            sortType: String,
            after: String
        ): Deferred<RedditResponse> {
            TODO("Not yet implemented")
        }

        override fun getAccessToken(
            headers: Map<String, String>,
            fields: Map<String, String>
        ): Deferred<Response<TokenResponse>> {
            TODO("Not yet implemented")
        }

        override fun save(
            headers: Map<String, String>,
            fields: Map<String, String>
        ): Deferred<Response<Void>> {
            TODO("Not yet implemented")
        }

        override fun unsave(
            headers: Map<String, String>,
            fields: Map<String, String>
        ): Deferred<Response<Void>> {
            TODO("Not yet implemented")
        }

        override fun vote(
            headers: Map<String, String>,
            fields: Map<String, String>
        ): Deferred<Response<Void>> {
            TODO("Not yet implemented")
        }

        override fun getSavedPosts(headers: Map<String, String>, after: String): Deferred<RedditResponse> {
            TODO("Not yet implemented")
        }

        override fun search(q: String, includeOver18: String): Deferred<RedditResponse> {
            TODO("Not yet implemented")
        }

        override fun getUpvotedPosts(headers: Map<String, String>): Deferred<PostDetailResponse> {
            TODO("Not yet implemented")
        }

        override fun getDownvotedPosts(headers: Map<String, String>): Deferred<PostDetailResponse> {
            TODO("Not yet implemented")
        }

        override fun getOverviewPosts(headers: Map<String, String>): Deferred<PostDetailResponse> {
            TODO("Not yet implemented")
        }

    }
}