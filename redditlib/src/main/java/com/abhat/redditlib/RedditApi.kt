package com.abhat.redditlib

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface RedditApi {
    @GET("/r/{subreddit}/comments/{article}/.json?sort=top")
    fun getPostDetails(@Path("subreddit") subreddit: String,
                       @Path("article") article: String): Deferred<List<PostDetailResponse>>

    @GET("/r/{subreddit}/comments/{article}/.json?sort=top")
    fun getPostDetailsPost(@HeaderMap headers: Map<String, String>,
                           @Path("subreddit") subreddit: String,
                       @Path("article") article: String): Deferred<List<PostDetailResponse>>


    @GET("/r/{subreddit}/.json")
    fun getRedditList(@Path("subreddit") subreddit: String, @Query("after") after: String = ""): Deferred<PostDetailResponse>

    @GET("/r/{subreddit}/.json")
    fun getRedditListPost(@HeaderMap headers: Map<String, String>,
                          @Path("subreddit") subreddit: String,
                          @Query("after") after: String = ""): Deferred<PostDetailResponse>

    @GET("hot.json?")
    fun getRedditFrontPage(@HeaderMap headers: Map<String, String>,
                          @Query("after") after: String = ""): Deferred<PostDetailResponse>

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

    @GET("user/kernel_pan1c/saved/")
    fun getSavedPosts(@HeaderMap headers: Map<String, String>): Deferred<PostDetailResponse>

    @GET("user/kernel_pan1c/upvoted/")
    fun getUpvotedPosts(@HeaderMap headers: Map<String, String>): Deferred<PostDetailResponse>

    @GET("user/kernel_pan1c/downvoted/")
    fun getDownvotedPosts(@HeaderMap headers: Map<String, String>): Deferred<PostDetailResponse>

    @GET("user/kernel_pan1c/overview/")
    fun getOverviewPosts(@HeaderMap headers: Map<String, String>): Deferred<PostDetailResponse>
}