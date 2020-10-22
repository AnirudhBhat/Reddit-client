package com.abhat.feed.ui.state

/**
 * Created by Anirudh Uppunda on 07,September,2020
 */
data class FeedCardUI(
    val title: String,
    val author: String,
    val points: String,
    val comments: String,
    val createdAt: Long,
    val subreddit: String,
    val over18: Boolean
)