package com.abhat.oauth

import com.abhat.core.model.TokenResponse
import com.abhat.oauth.repository.AccessTokenFetcher

/**
 * Created by Anirudh Uppunda on 27,May,2020
 */
class FakeAccessTokenFetcherWhichFails: AccessTokenFetcher {
    override suspend fun getAccessToken(
        headers: HashMap<String, String>,
        fields: HashMap<String, String>
    ): TokenResponse? {
        throw RuntimeException("oops, something went wrong!")
    }
}