package com.abhat.comment.data

import com.abhat.core.RedditApi
import com.abhat.core.model.RedditResponse
import com.abhat.core.network.HostSelectionInterceptor
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class CommentsRepositoryImpl(private val commentsApi: RedditApi): CommentsRepository,
    KoinComponent {
    private val hostSelectionInterceptor: HostSelectionInterceptor by inject()
    override suspend fun loadPostDetails(subreddit: String, articleUrl: String): List<RedditResponse> {
        hostSelectionInterceptor.host = "reddit.com"
        return commentsApi.getPostDetails(subreddit, articleUrl).await()
    }

}
