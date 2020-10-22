package com.abhat.comment.data

import com.abhat.core.RedditApi
import com.abhat.core.model.RedditResponse
import com.abhat.core.network.HostSelectionInterceptor
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class CommentsRepositoryImpl(private val commentsApi: RedditApi, private val interceptor: HostSelectionInterceptor?): CommentsRepository{

    init {
        interceptor?.host = "reddit.com"
    }
    override suspend fun loadPostDetails(subreddit: String, articleUrl: String): List<RedditResponse> {
        return commentsApi.getPostDetails(subreddit, articleUrl).await()
    }

}
