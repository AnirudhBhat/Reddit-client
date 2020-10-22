package com.abhat.search.data

import com.abhat.core.RedditApi

/**
 * Created by Anirudh Uppunda on 24,September,2020
 */

class SearchRepositoryImpl(private val redditApi: RedditApi): SearchRepository {
    override suspend fun search(searchQuery: String): SearchResult {
        return try {
            val response = redditApi.search(searchQuery).await()
            SearchResult.Success(response = response)
        } catch (e: Exception) {
            SearchResult.Error(e.cause)
        }
    }

}