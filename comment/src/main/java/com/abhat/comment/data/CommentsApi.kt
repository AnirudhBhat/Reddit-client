package com.abhat.comment.data

import com.abhat.core.model.RedditResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface CommentsApi {
    @GET("/r/{subreddit}/comments/{article}/.json?sort=top")
    fun getPostDetails(@Path("subreddit") subreddit: String,
                       @Path("article") article: String): Deferred<List<RedditResponse>>
}
