package com.abhat.core.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
@Parcelize
data class Preview(
    @field:Json(name="images")
    val images:List<Images>?,
    @field:Json(name = "reddit_video_preview")
    val redditVideoPreview: RedditVideo?
): Parcelable