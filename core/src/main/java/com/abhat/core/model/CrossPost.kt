package com.abhat.core.model

import com.squareup.moshi.Json

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
data class CrossPost(
    @field:Json(name = "secure_media")
    val secureMedia: SecureMedia?
)