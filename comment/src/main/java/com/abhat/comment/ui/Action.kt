package com.abhat.comment.ui

/**
 * Created by Anirudh Uppunda on 02,June,2020
 */
sealed class Action {
    data class LoadPostDetails(val subreddit: String, val articleUrl: String): Action()
}