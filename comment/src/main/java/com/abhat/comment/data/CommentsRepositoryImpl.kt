package com.abhat.comment.data

import com.abhat.core.model.RedditResponse

class CommentsRepositoryImpl(private val commentsApi: CommentsApi): CommentsRepository {
    override suspend fun loadPostDetails(subreddit: String, articleUrl: String): List<RedditResponse> {
        return commentsApi.getPostDetails(subreddit, articleUrl).await()
    }

}
