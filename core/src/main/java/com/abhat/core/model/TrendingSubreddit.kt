package com.abhat.core.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Anirudh Uppunda on 01,September,2020
 */
data class TrendingSubreddit(
    @SerializedName("subreddit_names")
    val subredditNames: List<String>
)