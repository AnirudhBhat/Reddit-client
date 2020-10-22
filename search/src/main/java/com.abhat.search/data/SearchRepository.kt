package com.abhat.search.data

/**
 * Created by Anirudh Uppunda on 24,September,2020
 */
interface SearchRepository {
    suspend fun search(searchQuery: String): SearchResult
}