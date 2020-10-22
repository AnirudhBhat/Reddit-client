package com.abhat.search.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.feed.R


/**
 * Created by Anirudh Uppunda on 24,September,2020
 */
class SearchActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        replaceFragment(SearchBottomSheetFragment(CoroutineContextProvider()))
    }

    fun replaceFragment(fragment: Fragment, tag: String = "general") {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.search_container, fragment, tag)
            .commitAllowingStateLoss()
    }

    fun onSubredditSelect(subreddit: String) {
        val returnIntent = Intent()
        returnIntent.putExtra("subreddit", subreddit)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}