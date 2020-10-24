package com.abhat.core

import com.abhat.core.SortType.SortType
import com.abhat.core.model.PostDetailResponse
import com.abhat.core.model.RedditResponse
import com.abhat.core.model.TokenResponse
import com.abhat.core.model.TrendingSubreddit
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface RedditApi {
    @GET("/r/{subreddit}/comments/{article}/.json?sort=top")
    fun getPostDetails(@Path("subreddit") subreddit: String,
                       @Path("article") article: String): Deferred<List<RedditResponse>>

    @GET("/r/{subreddit}/comments/{article}/.json?sort=top")
    fun getPostDetailsPost(@HeaderMap headers: Map<String, String>,
                           @Path("subreddit") subreddit: String,
                       @Path("article") article: String): Deferred<List<PostDetailResponse>>


    @GET("/r/{subreddit}/{sortType}.json")
    fun getRedditList(@Path("subreddit") subreddit: String,
                      @Path("sortType") sortType: SortType,
                      @Query("after") after: String = ""): Deferred<RedditResponse>

    @GET("{sortType}/.json")
    fun getRedditFrontPage(
        @Path("sortType") sortType: String,
        @Query("after") after: String = ""): Deferred<RedditResponse>

    @GET("{sortType}/.json")
    fun getRedditFrontPageOauth(
        @HeaderMap headers: Map<String, String>,
        @Path("sortType") sortType: String,
        @Query("after") after: String = ""): Deferred<RedditResponse>

    @GET("/r/{subreddit}/{sortType}.json")
    fun getRedditListPost(@HeaderMap headers: Map<String, String>,
                          @Path("subreddit") subreddit: String,
                          @Path("sortType") sortType: SortType,
                          @Query("after") after: String = ""): Deferred<RedditResponse>

    @GET("/api/trending_subreddits/.json")
    fun getTrendingSubreddits(): Deferred<TrendingSubreddit>

    @GET("subreddits/mine/subscriber/")
    fun getSubscribedSubreddits(@HeaderMap headers: Map<String, String>, @Query("limit") limit: Int): Deferred<RedditResponse>

    @GET("user/kernel_pan1c/saved/")
    fun getSavedPosts(@HeaderMap headers: Map<String, String>, @Query("after") after: String = ""): Deferred<RedditResponse>

    @GET("subreddits/search.json")
    fun search(@Query("q") q: String, @Query("include_over_18") includeOver18: String = "on"): Deferred<RedditResponse>

//    @GET("hot.json?")
//    fun getRedditFrontPage(@HeaderMap headers: Map<String, String>,
//                          @Query("after") after: String = ""): Deferred<PostDetailResponse>

    @FormUrlEncoded
    @POST("api/v1/access_token/")
    fun getAccessToken(@HeaderMap headers: Map<String, String>,
                       @FieldMap fields: Map<String, String>): Deferred<Response<TokenResponse>>

    @FormUrlEncoded
    @POST("api/save/")
    fun save(@HeaderMap headers: Map<String, String>,
             @FieldMap fields: Map<String, String>): Deferred<Response<Void>>

    @FormUrlEncoded
    @POST("api/unsave/")
    fun unsave(@HeaderMap headers: Map<String, String>,
             @FieldMap fields: Map<String, String>): Deferred<Response<Void>>

    @FormUrlEncoded
    @POST("api/vote/")
    fun vote(@HeaderMap headers: Map<String, String>,
             @FieldMap fields: Map<String, String>): Deferred<Response<Void>>
    

    @GET("user/{user_name}/upvoted/")
    fun getUpvotedPosts(@HeaderMap headers: Map<String, String>): Deferred<PostDetailResponse>

    @GET("user/{user_name}/downvoted/")
    fun getDownvotedPosts(@HeaderMap headers: Map<String, String>): Deferred<PostDetailResponse>

    @GET("user/{user_name}/overview/")
    fun getOverviewPosts(@HeaderMap headers: Map<String, String>): Deferred<PostDetailResponse>
}