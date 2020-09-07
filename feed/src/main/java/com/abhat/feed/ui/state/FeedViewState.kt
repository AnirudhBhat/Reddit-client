package com.abhat.feed.ui.state

import com.abhat.core.SortType.SortType
import com.abhat.core.model.Children
import com.abhat.core.model.RedditResponse

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
data class FeedViewState(
    val isLoading: Boolean = false,
    val feedList: RedditResponse? = null,
    val feedCardUI: List<FeedCardUI>? = null,
    val sortType: SortType = SortType.hot,
    val subreddit: String,
    val sortList: List<SortType> = listOf(SortType.hot, SortType.new, SortType.rising),
    val error: Throwable? = null,
    val authorizationError: Throwable? = null,
    val isSubredditBottomSheetOpen: Boolean = false,
    val isSortBottomSheetOpen: Boolean = false,
    val after: String = ""
)