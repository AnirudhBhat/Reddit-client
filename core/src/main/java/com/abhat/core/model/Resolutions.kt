package com.abhat.core.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
@Parcelize
data class Resolutions(
    @field:Json(name="url")
    val url:String?,
    @field:Json(name="width")
    val width:Int?,
    @field:Json(name="height")
    val height:Int?
): Parcelable