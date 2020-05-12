package com.abhat.core.model

import com.squareup.moshi.Json

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
data class RedditVideo(
    @field:Json(name="fallback_url")
    val fallbackUrl:String?,
    @field:Json(name="dash_url")
    val dashUrl:String?,
    @field:Json(name="scrubber_media_url")
    val scrubberMediaUrl:String?,
    @field:Json(name="hls_url")
    val hlsUrl:String?,
    @field:Json(name="is_gif")
    val isGif:Boolean?
)