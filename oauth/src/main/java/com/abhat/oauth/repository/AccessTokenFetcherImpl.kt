package com.abhat.oauth.repository

import com.abhat.core.RedditApi
import com.abhat.core.model.TokenResponse

/**
 * Created by Anirudh Uppunda on 12,March,2020
 */
open class AccessTokenFetcherImpl(val redditApi: RedditApi):
    AccessTokenFetcher {
    override suspend fun getAccessToken(
        headers: HashMap<String, String>,
        fields: HashMap<String, String>
    ): TokenResponse? {
        val response = redditApi.getAccessToken(headers, fields).await()
        return response.body()
    }
}