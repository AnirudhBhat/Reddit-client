package com.abhat.core.model

import org.threeten.bp.OffsetDateTime

data class TokenEntity(
        val refresh_token: String?,
        val scope: String,
        val access_token: String,
        val expiry: OffsetDateTime?,
        val active: Int
)