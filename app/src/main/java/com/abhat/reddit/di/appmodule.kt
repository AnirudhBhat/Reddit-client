package com.abhat.reddit.di

import com.abhat.feed.ui.MediaPlayerManager
import org.koin.dsl.module.module

/**
 * Created by Anirudh Uppunda on 29,May,2020
 */

val appModule = module {
    factory { provideMediaPlayerManager() }
}

private fun provideMediaPlayerManager() = MediaPlayerManager()