package com.abhat.core.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

/**
 * Created by Anirudh Uppunda on 01,September,2020
 */
data class TrendingSubreddit(
    @field:Json(name = "subreddit_names")
    val subredditNames: List<String>,
    @field:Json(name = "comment_count")
    val commentCount: Int? = null,
    @field:Json(name = "comment_url")
    val commentUrl: String? = null
)