package com.abhat.core.model

import com.squareup.moshi.Json

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
data class Preview(
    @field:Json(name="images")
    val images:List<Images>?
)