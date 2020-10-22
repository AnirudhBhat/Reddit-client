package com.abhat.core.di

import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.common.MoshiAdapter
import com.abhat.core.RedditApi
import com.abhat.core.network.HostSelectionInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * Created by Anirudh Uppunda on 12,March,2020
 */

val module = module {
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { provideCoroutineContextProvider() }
    single { HostSelectionInterceptor("www.reddit.com") }
    factory { provideRedditApi(get()) }
}

private fun provideRedditApi(retrofit: Retrofit) = retrofit.create(RedditApi::class.java)

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val moshi = Moshi.Builder()
        .add(MoshiAdapter())
        .build()

    return Retrofit.Builder()
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://www.reddit.com/")
        .client(okHttpClient)
        .build()
}

private fun provideOkHttpClient(interceptor: HostSelectionInterceptor): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(interceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}

private fun provideCoroutineContextProvider() = CoroutineContextProvider()