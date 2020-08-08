package com.abhat.core.model

import java.util.*

data class TokenEntity(
        val refresh_token: String?,
        val scope: String,
        val access_token: String,
        val expiry: Calendar?,
        val active: Int
)