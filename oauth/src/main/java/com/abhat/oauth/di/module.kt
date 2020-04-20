package com.abhat.oauth.di

import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.RedditApi
import com.abhat.oauth.repository.AccessTokenFetcherImpl
import com.abhat.oauth.ui.OauthViewModel
import org.koin.dsl.module.module

/**
 * Created by Anirudh Uppunda on 12,March,2020
 */

val oauthModule = module {
    factory { provideAccessTokenFetcher(get()) }
    factory { provideOauthViewModel(get(), get()) }
}

private fun provideOauthViewModel(accessTokenFetcher: AccessTokenFetcherImpl,
                                  contextProvider: CoroutineContextProvider
) = OauthViewModel(accessTokenFetcher, contextProvider)

private fun provideAccessTokenFetcher(redditApi: RedditApi) = AccessTokenFetcherImpl(redditApi)