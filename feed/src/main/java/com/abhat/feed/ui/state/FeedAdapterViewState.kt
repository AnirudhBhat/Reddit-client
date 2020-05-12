package com.abhat.feed.ui.state

import android.widget.TextView

/**
 * Created by Anirudh Uppunda on 10,May,2020
 */
data class FeedAdapterViewState(
    val url: String? = null,
    val isNsfw: Boolean = false,
    val points: String = "",
    val comments: String = "",
    val pointsTextView: TextView? = null,
    val commentsTextView: TextView? = null,
    val type: Type = Type.Video
)

sealed class Type {
    object Gif: Type()
    object Video: Type()
    data class News(val newsTitle: String, val newsSourceUrl: String): Type()
}