package com.abhat.search.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.abhat.core.FakeRedditResponse
import com.abhat.core.RedditApi
import com.abhat.search.data.SearchRepository
import com.abhat.search.data.SearchRepositoryImpl
import com.abhat.search.data.SearchResult
import com.abhat.search.fakes.FakeRedditApi
import com.abhat.search.fakes.FakeRedditApiWhichThrowsError
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Anirudh Uppunda on 24,October,2020
 */
class SearchRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var searchRepository: SearchRepository
    private lateinit var redditApi: RedditApi

    @Test
    fun `on proper response from api, return proper success response` () {
        runBlocking {
            val redditResponse = FakeRedditResponse.returnRedditResponse()
            redditApi = FakeRedditApi(redditResponse)
            searchRepository = SearchRepositoryImpl(redditApi)
            Assert.assertEquals(SearchResult.Success(redditResponse), searchRepository.search("LifeProTips"))
        }
    }

    @Test
    fun `on error response from api, return proper error response` () {
        runBlocking {
            val redditResponse = RuntimeException()
            redditApi = FakeRedditApiWhichThrowsError(redditResponse)
            searchRepository = SearchRepositoryImpl(redditApi)
            Assert.assertEquals(SearchResult.Error(redditResponse.cause), searchRepository.search("LifeProTips"))
        }
    }
}