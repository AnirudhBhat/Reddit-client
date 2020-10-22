package com.abhat.search.di

import com.abhat.core.RedditApi
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.search.data.SearchRepositoryImpl
import com.abhat.search.ui.SearchViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val searchModule = module {
    viewModel { provideSearchViewModel(get(), get()) }
    factory { provideSearchRepository(get()) }
}

private fun provideSearchViewModel(searchRepository: SearchRepositoryImpl, contextProvider: CoroutineContextProvider) = SearchViewModel(searchRepository, contextProvider)

private fun provideSearchRepository(redditApi: RedditApi) = SearchRepositoryImpl(redditApi)

