package com.abhat.comment

import com.abhat.comment.data.CommentsRepository
import com.abhat.core.FakeRedditResponse
import com.abhat.core.model.RedditResponse

/**
 * Created by Anirudh Uppunda on 02,June,2020
 */
class FakeCommentsRepository(private val shouldThrowError: Boolean = false, private val replies: RedditResponse? = null): CommentsRepository {
    override suspend fun loadPostDetails(
        subreddit: String,
        articleUrl: String
    ): List<RedditResponse> {
        if (shouldThrowError) {
            throw RuntimeException("Error!")
        } else {
            return FakeRedditResponse.returnRedditPostDetailResponse(replies = replies)
        }
    }
}