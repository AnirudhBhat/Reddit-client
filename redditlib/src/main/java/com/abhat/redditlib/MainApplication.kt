package com.abhat.redditlib

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen

class MainApplication: Application() {

    companion object {
        private lateinit var context: MainApplication

        fun getContext(): Context {
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        AndroidThreeTen.init(this)
    }
}