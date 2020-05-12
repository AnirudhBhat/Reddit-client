package com.abhat.core.model

import com.squareup.moshi.Json

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
data class Images(
    @field:Json(name="resolutions")
    val resolutions:List<Resolutions>?,
    @field:Json(name="source")
    val source: Resolutions
)