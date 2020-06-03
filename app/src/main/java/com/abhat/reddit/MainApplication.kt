package com.abhat.reddit

import android.app.Application
import com.abhat.comment.di.postDetailModule
import com.abhat.core.di.module
import com.abhat.feed.di.feedModule
import com.abhat.oauth.di.oauthModule
import com.abhat.reddit.di.appModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.android.startKoin

/**
 * Created by Anirudh Uppunda on 12,March,2020
 */
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(module, oauthModule, feedModule, appModule, postDetailModule) )
        AndroidThreeTen.init(this)
    }
}