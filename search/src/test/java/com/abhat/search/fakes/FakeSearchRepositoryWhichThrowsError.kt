package com.abhat.search.fakes

import com.abhat.search.data.SearchRepository
import com.abhat.search.data.SearchResult
import java.lang.Exception

/**
 * Created by Anirudh Uppunda on 23,October,2020
 */
class FakeSearchRepositoryWhichThrowsError(private val exception: Exception): SearchRepository {
    override suspend fun search(searchQuery: String): SearchResult {
        throw exception
    }
}