package com.abhat.oauth.repository

import com.abhat.core.model.TokenResponse

/**
 * Created by Anirudh Uppunda on 12,March,2020
 */
interface AccessTokenFetcher {
    suspend fun getAccessToken(headers: HashMap<String, String>, fields: HashMap<String, String>): TokenResponse?
}