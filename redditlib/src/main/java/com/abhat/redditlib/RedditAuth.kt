package com.abhat.redditlib

import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.OffsetDateTime
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.HashMap

/**
 * Created by Anirudh Uppunda on 08,March,2020
 */
class RedditAuth(private val clientId: String,
                 private val state: String,
                 private val redirectUri: String) {

    private lateinit var redditApi: RedditApi
    private val RedditUrl = "https://www.reddit.com/"

    sealed class OauthCommand {
        data class RetrievedAndStoredAccessToken(val tokenEntity: TokenEntity): OauthCommand()
    }

    val command: MutableLiveData<OauthCommand> = MutableLiveData()

    init {
        val RedditUrl = "https://www.reddit.com/"
    }

    fun initRedditApi() {
        redditApi = createRedditRetrofit(RedditUrl)
    }

    private val url = "https://www.reddit.com/api/v1/authorize.compact?" +
            "client_id=$clientId" +
            "&duration=permanent" +
            "&response_type=code" +
            "&state=$state" +
            "&redirect_uri=$redirectUri" +
            "&scope=identity edit flair history modconfig modflair modlog modposts modwiki mysubreddits privatemessages read report save submit subscribe vote wikiedit wikiread"

    fun getUrl() = url

    fun retrieveAndStoreAccessToken(authCode: String) {
        GlobalScope.launch {
            try {
                val headersAndFieldsPaie = populateHeadersAndFields(authCode)
                val response = redditApi.getAccessToken(headersAndFieldsPaie.first, headersAndFieldsPaie.second).await()
                response.body()?.let { responseBody ->
                    val token = mapToEntity(responseBody)
                    command.postValue(OauthCommand.RetrievedAndStoredAccessToken(token))
                    Log.d("REDDIT", "access token is: " + token.access_token)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun mapToEntity(tokenResponse: TokenResponse): TokenEntity {
        return TokenEntity(tokenResponse.refreshToken, tokenResponse.scope, tokenResponse.accessToken, OffsetDateTime.now().plusSeconds(tokenResponse.expiresIn.toLong()), 1)
    }

    private fun populateHeadersAndFields(authCode: String): Pair<HashMap<String, String>, HashMap<String, String>> {
        val headers = HashMap<String, String>()
        val fields = HashMap<String, String>()
        val auth = Base64.encodeToString("$clientId:".toByteArray(), Base64.NO_WRAP)
        headers["Authorization"] = "Basic $auth"
        fields["grant_type"] = "authorization_code"
        Log.d("TAG", "CODE: $authCode")
        fields["code"] = authCode
        fields["redirect_uri"] = redirectUri
        return Pair(headers, fields)
    }

    fun createRedditRetrofit(baseUrl: String): RedditApi {
        val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Log.d("API", it)
        })
        logger.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
        val moshi = Moshi.Builder()
            .add(MoshiAdapter())
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RedditApi::class.java)
    }
}