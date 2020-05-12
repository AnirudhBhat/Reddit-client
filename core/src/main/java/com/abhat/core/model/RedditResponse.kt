package com.abhat.core.model

import com.squareup.moshi.Json

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
data class RedditResponse(
    @field:Json(name = "data")
    val data: Data,
    @field:Json(name = "kind")
    val kind: String
)