package com.abhat.oauth.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.abhat.oauth.PreferenceHelper
import com.abhat.oauth.R
import com.abhat.oauth.encodeBase64ToString
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.ext.android.inject
import org.threeten.bp.OffsetDateTime
import java.util.*

/**
 * Created by Anirudh Uppunda on 12,March,2020
 */
class OauthActivity : AppCompatActivity() {

    val state = UUID.randomUUID().toString()
    val url = "https://www.reddit.com/api/v1/authorize.compact?" +
            "client_id=CLIENT_ID" +
            "&duration=permanent" +
            "&response_type=code" +
            "&state=$state" +
            "&redirect_uri=https://github.com/anirudhbhat" +
            "&scope=identity edit flair history modconfig modflair modlog modposts modwiki mysubreddits privatemessages read report save submit subscribe vote wikiedit wikiread"

    private val oauthViewModel: OauthViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        oauthViewModel.showProgressBar()
        startOauthWebView()
        observeViewModel()
    }

    private fun observeViewModel() {
        oauthViewModel.viewState.observe(this, androidx.lifecycle.Observer { viewState ->
            setProgressBarVisibility(viewState)
            viewState.error?.let { throwable ->
                showErrorToast("oops, something went wrong!")
            } ?: run {
                storeAccessToken(viewState)
            }
        })
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun storeAccessToken(viewState: OauthViewModel.ViewState) {
        when (viewState.success) {
            is OauthViewModel.Success.GotTokenResponse -> {
                viewState.success.tokenResponse?.let { tokenResponse ->
                    oauthViewModel.getTokenEntity(
                        tokenResponse,
                        OffsetDateTime.now().plusSeconds(tokenResponse.expiresIn.toLong())
                    )
                }
            }

            is OauthViewModel.Success.GotTokenEntity -> {
                // store token entity into shared preference
                PreferenceHelper.storeToken(this, viewState.success.tokenEntity)
            }
        }
    }

    private fun setProgressBarVisibility(viewState: OauthViewModel.ViewState) {
        if (viewState.isLoading) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }

    private fun startOauthWebView() {
        webview_login.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String) {
                super.onPageFinished(view, url)
                oauthViewModel.hideProgressBar()
                if (oauthViewModel.isValid(url)) {
                    val uri = Uri.parse(url)
                    if (uri.getQueryParameter("state") == state) {
                        val authCode = uri.getQueryParameter("code")
                        val headersAndFields = populateHeadersAndFields(
                            authCode!!,
                            "CLIENT_ID",
                            "https://github.com/anirudhbhat"
                        )
                        oauthViewModel.retrieveAccessToken(
                            headersAndFields.first,
                            headersAndFields.second
                        )
                    }
                }
            }
        }
        webview_login.loadUrl(url)
    }

    private fun populateHeadersAndFields(
        authCode: String,
        clientId: String,
        redirectUri: String
    ): Pair<HashMap<String, String>, HashMap<String, String>> {
        val headers = HashMap<String, String>()
        val fields = HashMap<String, String>()
        val auth = clientId.encodeBase64ToString()
        headers["Authorization"] = "Basic $auth"
        fields["grant_type"] = "authorization_code"
        fields["code"] = authCode
        fields["redirect_uri"] = redirectUri
        return Pair(headers, fields)
    }
}