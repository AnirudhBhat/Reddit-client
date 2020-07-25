package com.abhat.feed.di

import com.abhat.core.RedditApi
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.feed.data.FeedRepositoryImpl
import com.abhat.feed.ui.FeedViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
val feedModule = module {
    viewModel { provideFeedViewModel(get(), get()) }
    factory { provideFeedRepository(get()) }
}

private fun provideFeedViewModel(feedRepository: FeedRepositoryImpl,
                                 contextProvider: CoroutineContextProvider) = FeedViewModel(feedRepository, contextProvider)

private fun provideFeedRepository(redditApi: RedditApi) = FeedRepositoryImpl(redditApi)