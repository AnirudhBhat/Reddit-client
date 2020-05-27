package com.abhat.oauth

import com.abhat.core.model.TokenResponse
import com.abhat.oauth.repository.AccessTokenFetcher

/**
 * Created by Anirudh Uppunda on 27,May,2020
 */
class FakeAccessTokenFetcher: AccessTokenFetcher {
    override suspend fun getAccessToken(
        headers: HashMap<String, String>,
        fields: HashMap<String, String>
    ): TokenResponse? {
        return generateTokenResponse()
    }

    private fun generateTokenResponse(): TokenResponse {
        return TokenResponse(
            "1234",
            1,
            "1234",
            "1234",
            "1234",
            "1"
        )
    }
}