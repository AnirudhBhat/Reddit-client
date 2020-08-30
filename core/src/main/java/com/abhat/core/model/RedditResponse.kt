package com.abhat.core.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
@Parcelize
data class RedditResponse(
    @field:Json(name = "data")
    val data: Data,
    @field:Json(name = "kind")
    val kind: String
): Parcelable