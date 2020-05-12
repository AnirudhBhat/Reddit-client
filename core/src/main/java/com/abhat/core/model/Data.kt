package com.abhat.core.model

import com.squareup.moshi.Json

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
data class Data(
    @field:Json(name = "after")
    val after: String?,
    @field:Json(name = "before")
    val before: Any?,
    @field:Json(name = "children")
    val children: MutableList<Children>,
    @field:Json(name = "dist")
    val dist: Any?,
    @field:Json(name = "modhash")
    val modhash: String
)