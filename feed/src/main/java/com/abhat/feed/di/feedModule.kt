package com.abhat.feed.di

import com.abhat.core.RedditApi
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.feed.data.*
import com.abhat.feed.ui.FeedViewModel
import com.abhat.feed.ui.subreddit.SubredditViewModel
import com.abhat.feed.ui.trendingsubreddit.TrendingSubredditViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
val feedModule = module {
    viewModel { provideFeedViewModel(get(), get()) }
    viewModel { provideTrendingSubredditViewModel(get(), get()) }
    viewModel { provideSubredditViewModel(get(), get()) }
    factory { provideFeedRepository(get()) }
    factory { provideTrendingSubredditRepository(get()) }
    factory { provideSubredditRepository(get()) }
}

private fun provideFeedViewModel(feedRepository: FeedRepositoryImpl,
                                 contextProvider: CoroutineContextProvider) = FeedViewModel(feedRepository, contextProvider)

private fun provideFeedRepository(redditApi: RedditApi) = FeedRepositoryImpl(redditApi)

private fun provideTrendingSubredditViewModel(trendingSubredditRepository: TrendingSubredditRepositoryImpl,
                                              contextProvider: CoroutineContextProvider) = TrendingSubredditViewModel(trendingSubredditRepository, contextProvider)

private fun provideTrendingSubredditRepository(redditApi: RedditApi) = TrendingSubredditRepositoryImpl(redditApi)

private fun provideSubredditViewModel(subredditRepository: SubredditRepositoryImpl, coroutineContextProvider: CoroutineContextProvider) = SubredditViewModel(subredditRepository, coroutineContextProvider)

private fun provideSubredditRepository(redditApi: RedditApi) = SubredditRepositoryImpl(redditApi)
