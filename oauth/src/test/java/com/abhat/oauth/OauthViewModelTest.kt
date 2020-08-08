package com.abhat.oauth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.model.TokenEntity
import com.abhat.core.model.TokenResponse
import com.abhat.oauth.ui.OauthViewModel
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.threeten.bp.OffsetDateTime
import java.util.*
import kotlin.math.exp

/**
 * Created by Anirudh Uppunda on 12,March,2020
 */
class OauthViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var oauthViewModel: com.abhat.oauth.ui.OauthViewModel
    private lateinit var accessTokenFetcherImpl: FakeAccessTokenFetcher
    private lateinit var observer: Observer<OauthViewModel.ViewState>

    @Before
    fun setup() {
        accessTokenFetcherImpl = FakeAccessTokenFetcher()
        observer = mock()
        oauthViewModel =
            com.abhat.oauth.ui.OauthViewModel(accessTokenFetcherImpl,
                TestContextProvider()
            )
    }

    @Test
    fun `retrieve access token correctly`() {
        runBlocking {
            val headers = populateHeadersAndFields().first
            val fields = populateHeadersAndFields().second

            val successState = OauthViewModel.ViewState(isLoading = false, success = com.abhat.oauth.ui.OauthViewModel.Success.GotTokenResponse(generateTokenResponse()), error = null)

            oauthViewModel.viewState.observeForever(observer)

            oauthViewModel.retrieveAccessToken(headers, fields)

            verify(observer).onChanged(successState)
        }
    }

    @Test
    fun `show loading while retrieve access token api call`() {
        runBlocking {
            val headers = populateHeadersAndFields().first
            val fields = populateHeadersAndFields().second

            val loadingState = OauthViewModel.ViewState(isLoading = true, success = null, error = null)
            val successState = OauthViewModel.ViewState(isLoading = false, success = com.abhat.oauth.ui.OauthViewModel.Success.GotTokenResponse(generateTokenResponse()), error = null)

            oauthViewModel.viewState.observeForever(observer)

            oauthViewModel.retrieveAccessToken(headers, fields)

            val inorder = inOrder(observer)
            inorder.verify(observer).onChanged(loadingState)
            inorder.verify(observer).onChanged(successState)
        }
    }

    @Test
    fun `assert failure state on error while retrieving access token`() {
        runBlocking {
            val fakeAccessTokenFetcherWhichFails = FakeAccessTokenFetcherWhichFails()
            val oauthViewModel = OauthViewModel(fakeAccessTokenFetcherWhichFails, TestContextProvider())
            val headers = populateHeadersAndFields().first
            val fields = populateHeadersAndFields().second

            val throwable = RuntimeException("oops, something went wrong!")

            val loadingState = OauthViewModel.ViewState(isLoading = true, success = null, error = null)
            val failureState = OauthViewModel.ViewState(isLoading = false, success = null, error = throwable)

            oauthViewModel.viewState.observeForever(observer)

            oauthViewModel.retrieveAccessToken(headers, fields)

            Assert.assertEquals(failureState.error?.message, oauthViewModel.viewState.value?.error?.message)
            Assert.assertEquals(failureState.isLoading, oauthViewModel.viewState.value?.isLoading)
            Assert.assertEquals(failureState.success, oauthViewModel.viewState.value?.success)

//            val inorder = inOrder(observer)
//            inorder.verify(observer).onChanged(loadingState)
//            inorder.verify(observer).onChanged(failureState)
        }
    }

    @Test
    fun `get token entity correctly`() {
        runBlocking {
            val now = Calendar.getInstance()
            val successState = OauthViewModel.ViewState(isLoading = false, success = OauthViewModel.Success.GotTokenEntity(mapToEntity(generateTokenResponse(), now)), error = null)

            oauthViewModel.viewState.observeForever(observer)

            oauthViewModel.getTokenEntity(generateTokenResponse(), now)

            verify(observer).onChanged(successState)
        }
    }

    @Test
    fun `validate reddit sign in url return false in case of incorrect url`() {
        runBlocking {
            val url = "1234"
            Assert.assertEquals(false, oauthViewModel.isValid(url))
        }
    }

    @Test
    fun `validate reddit sign in url return true if url contains ?code=`() {
        runBlocking {
            val url = "https://www.reddit.com?code="
            Assert.assertEquals(true, oauthViewModel.isValid(url))
        }
    }

    @Test
    fun `validate reddit sign in url return true if url contains &code=`() {
        runBlocking {
            val url = "https://www.reddit.com&code="
            Assert.assertEquals(true, oauthViewModel.isValid(url))
        }
    }

    @Test
    fun `validate reddit sign in url return false if url is empty`() {
        runBlocking {
            val url = ""
            Assert.assertEquals(false, oauthViewModel.isValid(url))
        }
    }

    @Test
    fun `validate reddit sign in url return false if url is null`() {
        runBlocking {
            val url: String? = null
            Assert.assertEquals(false, oauthViewModel.isValid(url))
        }
    }


    private fun populateHeadersAndFields(): Pair<HashMap<String, String>, HashMap<String, String>> {
        val headers = HashMap<String, String>()
        val fields = HashMap<String, String>()
        val auth = "1234"
        headers["Authorization"] = "Basic $auth"
        fields["grant_type"] = "authorization_code"
        fields["code"] = "1234"
        fields["redirect_uri"] = "1234"
        return Pair(headers, fields)
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

    private fun mapToEntity(tokenResponse: TokenResponse, now: Calendar): TokenEntity {
        return TokenEntity(tokenResponse.refreshToken, tokenResponse.scope, tokenResponse.accessToken, now, 1)
    }

    class TestContextProvider : CoroutineContextProvider() {
        override val Main: CoroutineDispatcher = Unconfined
        override val IO: CoroutineDispatcher = Unconfined
    }
}