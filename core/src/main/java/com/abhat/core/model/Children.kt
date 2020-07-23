package com.abhat.core.model

import com.squareup.moshi.Json

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
data class Children(
    @field:Json(name = "data")
    val data: ChildrenData,
    @field:Json(name = "kind")
    val kind: String,
    var indent: Int,
    var isParentComment: Boolean = false,
    var childrenIndex: Int = 0
)