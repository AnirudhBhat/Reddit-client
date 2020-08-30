package com.abhat.core.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
@Parcelize
data class SecureMedia(
    @field:Json(name = "reddit_video")
    val redditVideo: RedditVideo?,
    @field:Json(name="oembed")
    val oembed: Oembed?,
    @field:Json(name = "type")
    val type: String?
): Parcelable