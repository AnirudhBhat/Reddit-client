package com.abhat.core.model

import com.squareup.moshi.Json

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
data class SecureMedia(
    @field:Json(name = "reddit_video")
    val redditVideo: RedditVideo?,
    @field:Json(name="oembed")
    val oembed: Oembed?
)