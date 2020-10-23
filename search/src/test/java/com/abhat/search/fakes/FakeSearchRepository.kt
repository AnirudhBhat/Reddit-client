package com.abhat.search.fakes

import com.abhat.core.FakeRedditResponse
import com.abhat.search.data.SearchRepository
import com.abhat.search.data.SearchResult

/**
 * Created by Anirudh Uppunda on 23,October,2020
 */
class FakeSearchRepository: SearchRepository {
    override suspend fun search(searchQuery: String): SearchResult {
        return SearchResult.Success(FakeRedditResponse.returnRedditResponse())
    }
}