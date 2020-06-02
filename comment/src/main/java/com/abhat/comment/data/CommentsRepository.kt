package com.abhat.comment.data

import com.abhat.core.model.RedditResponse

/**
 * Created by Anirudh Uppunda on 02,June,2020
 */
interface CommentsRepository {
    suspend fun loadPostDetails(subreddit: String, articleUrl: String): List<RedditResponse>
}