package com.abhat.core.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
@Parcelize
data class Images(
    @field:Json(name="resolutions")
    val resolutions:List<Resolutions>?,
    @field:Json(name="source")
    val source: Resolutions
): Parcelable