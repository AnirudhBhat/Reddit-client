package com.abhat.oauth.ui

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.abhat.core.BuildConfig
import com.abhat.core.common.PreferenceHelper
import com.abhat.oauth.R
import com.abhat.core.extensions.encodeBase64ToString
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.android.ext.android.inject
import java.util.*

/**
 * Created by Anirudh Uppunda on 04,August,2020
 */
class OauthFragment: BottomSheetDialogFragment() {

    val state = UUID.randomUUID().toString()
    val url = "https://www.reddit.com/api/v1/authorize.compact?" +
            "client_id=${BuildConfig.CLIENT_ID}" +
            "&duration=permanent" +
            "&response_type=code" +
            "&state=$state" +
            "&redirect_uri=https://github.com/anirudhbhat" +
            "&scope=identity edit flair history modconfig modflair modlog modposts modwiki mysubreddits privatemessages read report save submit subscribe vote wikiedit wikiread"

    private val oauthViewModel: OauthViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppRoundedCornerBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_profile, container, false)
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
        (dialog as BottomSheetDialog).behavior.peekHeight = 500
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        oauthViewModel.showProgressBar()
        startOauthWebView()
        observeViewModel()
    }

    private fun observeViewModel() {
        oauthViewModel.viewState.observe(activity!!, androidx.lifecycle.Observer { viewState ->
            setProgressBarVisibility(viewState)
            viewState?.success?.let {
                storeAccessToken(viewState)
            }

            viewState?.error?.let {
                showErrorToast("oops, something went wrong!")
            }
//            viewState.error?.let { throwable ->
//                showErrorToast("oops, something went wrong!")
//            } ?: run {
//                storeAccessToken(viewState)
//            }
        })
    }

    private fun setProgressBarVisibility(viewState: OauthViewModel.ViewState) {
        if (viewState.isLoading) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    private fun storeAccessToken(viewState: OauthViewModel.ViewState) {
        when (viewState.success) {
            is OauthViewModel.Success.GotTokenResponse -> {
                viewState.success.tokenResponse?.let { tokenResponse ->
                    oauthViewModel.getTokenEntity(
                        tokenResponse,
                        oauthViewModel.mapTokenResponseExpiryToCalendarObject(tokenResponse.expiresIn, Calendar.getInstance())
                    )
                }
            }

            is OauthViewModel.Success.GotTokenEntity -> {
                // store token entity into shared preference
                PreferenceHelper.storeToken(activity!!, viewState.success.tokenEntity)
            }
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
                        Log.d("TAG", "url: " + url)
                        val authCode = uri.getQueryParameter("code")
                        val headersAndFields = populateHeadersAndFields(
                            authCode!!,
                            BuildConfig.CLIENT_ID,
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
        Log.d("TAG", "authcode: " + authCode)
        Log.d("TAG", "clientID: " + clientId)
        Log.d("TAG", "redirectUri: " + redirectUri)
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