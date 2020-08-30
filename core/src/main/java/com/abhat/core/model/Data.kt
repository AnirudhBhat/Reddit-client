package com.abhat.core.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
@Parcelize
data class Data(
    @field:Json(name = "after")
    val after: String?,
    @field:Json(name = "children")
    val children: MutableList<Children>?,
    @field:Json(name = "modhash")
    val modhash: String
): Parcelable