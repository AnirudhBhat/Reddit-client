package com.abhat.oauth.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.oauth.repository.AccessTokenFetcher
import com.abhat.core.model.TokenEntity
import com.abhat.core.model.TokenResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import org.threeten.bp.OffsetDateTime

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

    fun getTokenEntity(tokenResponse: TokenResponse, expiry: OffsetDateTime?) {
        val token = mapToEntity(tokenResponse, expiry)
        currentViewState = currentViewState.copy(
            isLoading = false,
            success = Success.GotTokenEntity(token),
            error = null
        )
    }

    private fun mapToEntity(tokenResponse: TokenResponse, expiry: OffsetDateTime?): TokenEntity {
        return TokenEntity(
            tokenResponse.refreshToken,
            tokenResponse.scope,
            tokenResponse.accessToken,
            expiry,
            1
        )
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
}