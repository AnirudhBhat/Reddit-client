package com.abhat.core.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
@Parcelize
data class Children(
    @field:Json(name = "data")
    val data: ChildrenData,
    @field:Json(name = "kind")
    val kind: String,
    var indent: Int,
    var isParentComment: Boolean = false,
    var childrenIndex: Int = 0
): Parcelable