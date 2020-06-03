package com.abhat.comment.di

import com.abhat.comment.data.CommentsApi
import com.abhat.comment.data.CommentsRepositoryImpl
import com.abhat.comment.ui.CommentsViewModel
import com.abhat.core.RedditApi
import com.abhat.core.common.CoroutineContextProvider
import org.koin.dsl.module.module

/**
 * Created by Anirudh Uppunda on 03,June,2020
 */

val postDetailModule = module {
    factory { providePostDetailViewModel(get(), get()) }
    factory { providePostDetailRepository(get()) }
}

private fun providePostDetailViewModel(commentsRepository: CommentsRepositoryImpl, contextProvider: CoroutineContextProvider) = CommentsViewModel(commentsRepository, contextProvider)

private fun providePostDetailRepository(commentsApi: RedditApi) = CommentsRepositoryImpl(commentsApi)