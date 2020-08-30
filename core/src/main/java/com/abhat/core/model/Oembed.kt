package com.abhat.core.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
@Parcelize
data class Oembed(
    @field:Json(name="thumbnail_url")
    val thumbnailUrl:String?
): Parcelable