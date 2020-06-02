package com.abhat.comment.data

import com.abhat.core.model.RedditResponse

class CommentsRepository(private val commentsApi: CommentsApi) {
    suspend fun loadPostDetails(subreddit: String, articleUrl: String): List<RedditResponse> {
        return commentsApi.getPostDetails(subreddit, articleUrl).await()
    }

}
