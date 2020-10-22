package com.abhat.search.ui

/**
 * Created by Anirudh Uppunda on 24,September,2020
 */
sealed class Action {
    data class Search(val searchQuery: String): Action()
}