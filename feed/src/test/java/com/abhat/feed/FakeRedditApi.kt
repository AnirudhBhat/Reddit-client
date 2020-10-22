package com.abhat.feed

import com.abhat.core.FakeRedditResponse
import com.abhat.core.RedditApi
import com.abhat.core.SortType.SortType
import com.abhat.core.model.PostDetailResponse
import com.abhat.core.model.RedditResponse
import com.abhat.core.model.TokenResponse
import com.abhat.core.model.TrendingSubreddit
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import retrofit2.Response

/**
 * Created by Anirudh Uppunda on 12,July,2020
 */
class FakeRedditApi: RedditApi {
    override fun getPostDetails(
        subreddit: String,
        article: String
    ): Deferred<List<RedditResponse>> {
        TODO("Not yet implemented")
    }

    override fun getPostDetailsPost(
        headers: Map<String, String>,
        subreddit: String,
        article: String
    ): Deferred<List<PostDetailResponse>> {
        TODO("Not yet implemented")
    }

    override fun getRedditList(
        subreddit: String,
        sortType: SortType,
        after: String
    ): Deferred<RedditResponse> {
        return CompletableDeferred(FakeRedditResponse.returnRedditResponse())
    }

    override fun getRedditFrontPage(sortType: String, after: String): Deferred<RedditResponse> {
        return CompletableDeferred(FakeRedditResponse.returnRedditResponse())
    }

    override fun getRedditFrontPageOauth(
        headers: Map<String, String>,
        sortType: String,
        after: String
    ): Deferred<RedditResponse> {
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
        return CompletableDeferred(FakeRedditResponse.returnTrendingSubredditResponse())
    }

    override fun getSubscribedSubreddits(
        headers: Map<String, String>,
        limit: Int
    ): Deferred<RedditResponse> {
        return CompletableDeferred(FakeRedditResponse.returnRedditResponse())
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