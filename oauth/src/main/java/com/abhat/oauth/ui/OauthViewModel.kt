package com.abhat.oauth.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhat.core.BuildConfig.CLIENT_ID
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.model.TokenEntity
import com.abhat.core.model.TokenResponse
import com.abhat.core.extensions.encodeBase64ToString
import com.abhat.oauth.repository.AccessTokenFetcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Created by Anirudh Uppunda on 12,March,2020
 */
class OauthViewModel(
    private val accessTokenFetcher: AccessTokenFetcher,
    private val contextProvider: CoroutineContextProvider
) : ViewModel() {

    val viewState: MutableLiveData<ViewState> = MutableLiveData()

    private var currentViewState = ViewState()
        set(value) {
            field = value
            viewState.postValue(value)
        }

    fun isValid(url: String?): Boolean {
        url?.let { url ->
            if (url.contains("&code=") || url.contains("?code=")) {
                return true
            }
            return false
        } ?: run {
            return false
        }
    }

    fun showProgressBar() {
        currentViewState = currentViewState.copy(isLoading = true, success = null, error = null)
    }

    fun hideProgressBar() {
        currentViewState = currentViewState.copy(isLoading = false, success = null, error = null)
    }

    fun retrieveAccessToken(headers: HashMap<String, String>, fields: HashMap<String, String>) {
        currentViewState = currentViewState.copy(isLoading = true)
        viewModelScope.launch(contextProvider.Main) {
            supervisorScope {
                try {
                    val response =
                        withContext(viewModelScope.coroutineContext + contextProvider.IO) {
                            accessTokenFetcher.getAccessToken(
                                headers,
                                fields
                            )
                        }
                    response?.let { tokenResponse ->
                        currentViewState = currentViewState.copy(
                            isLoading = false,
                            success = Success.GotTokenResponse(
                                tokenResponse
                            ),
                            error = null
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    currentViewState =
                        currentViewState.copy(isLoading = false, success = null, error = e.cause)
                }
            }
        }
    }

//    private fun retrieveRefreshToken(
//        headers: HashMap<String, String>,
//        fields: HashMap<String, String>
//    ): TokenResponse? {
//
//        viewModelScope.launch(contextProvider.Main) {
//            supervisorScope {
//                try {
//                    val response =
//                        withContext(viewModelScope.coroutineContext + contextProvider.IO) {
//                            accessTokenFetcher.getAccessToken(
//                                headers,
//                                fields
//                            )
//                        }
//                    response?.let { tokenResponse ->
//                        tokenResponse
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    null
//                }
//            }
//        }
//    }

    fun getTokenEntity(tokenResponse: TokenResponse, tokenExpiry: Calendar) {
        val token = mapToEntity(tokenResponse, tokenExpiry)
        currentViewState = currentViewState.copy(
            isLoading = false,
            success = Success.GotTokenEntity(token),
            error = null
        )
    }

    private fun mapToEntity(tokenResponse: TokenResponse, tokenExpiry: Calendar?): TokenEntity {
        return TokenEntity(
            tokenResponse.refreshToken,
            tokenResponse.scope,
            tokenResponse.accessToken,
            tokenExpiry,
            1
        )
    }

    fun mapTokenResponseExpiryToCalendarObject(expiresIn: Int, tokenExpireTime: Calendar): Calendar {
        val tokenExpiryDate = Date()
        tokenExpireTime.time = tokenExpiryDate
        tokenExpireTime.add(Calendar.SECOND, expiresIn)
        return tokenExpireTime
    }

//    fun getToken(tokenEntity: TokenEntity, fetchTokenFrom: FetchTokenFrom): TokenEntity? {
//        return when (fetchTokenFrom) {
//            FetchTokenFrom.USER_NOT_LOGGED_IN -> null
//            FetchTokenFrom.SHARED_PREFERENCE -> tokenEntity
//            else -> FetchTokenFrom.REFRESH_TOKEN_API
////            FetchTokenFrom.REFRESH_TOKEN_API -> {
////                tokenEntity.refresh_token?.let { refreshToken ->
////                    refreshAccessToken(refreshToken)?.let { tokenResponse ->
////                        mapToEntity(tokenResponse)
////                    }
////                }
////            }
//        }
//    }

    private fun refreshAccessToken(refreshToken: String) {
        val headers = HashMap<String, String>()
        val fields = HashMap<String, String>()
        val auth = CLIENT_ID.encodeBase64ToString()
        headers["Authorization"] = "Basic $auth"
        headers["Content-Type"] = "application/x-www-form-urlencoded"
        fields["grant_type"] = "refresh_token"
        fields["refresh_token"] = refreshToken

        retrieveAccessToken(headers, fields)
    }

    fun howShouldWeFetchTheToken(tokenEntity: TokenEntity?): FetchTokenFrom {
        return when {
            tokenEntity == null -> FetchTokenFrom.USER_NOT_LOGGED_IN

            isTokenExpired(tokenEntity.expiry) -> FetchTokenFrom.REFRESH_TOKEN_API

            else -> FetchTokenFrom.SHARED_PREFERENCE
        }
    }

    private fun isTokenExpired(tokenExpiry: Calendar?): Boolean {
        tokenExpiry?.let { tokenExpiry ->
            val now = Calendar.getInstance()
            if (tokenExpiry.after(now)) {
                return true
            }
            return false
        } ?: run {
            return false
        }
    }

    data class ViewState(
        var isLoading: Boolean = false,
        val success: Success? = null,
        val error: Throwable? = null
    )

    sealed class Success {
        data class GotTokenResponse(val tokenResponse: TokenResponse) : Success()
        data class GotTokenEntity(val tokenEntity: TokenEntity) : Success()
    }

    enum class FetchTokenFrom {
        SHARED_PREFERENCE,
        USER_NOT_LOGGED_IN,
        REFRESH_TOKEN_API
    }
}