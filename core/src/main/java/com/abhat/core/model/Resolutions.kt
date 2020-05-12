package com.abhat.core.model

import com.squareup.moshi.Json

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
data class Resolutions(
    @field:Json(name="url")
    val url:String?,
    @field:Json(name="width")
    val width:Int?,
    @field:Json(name="height")
    val height:Int?
)