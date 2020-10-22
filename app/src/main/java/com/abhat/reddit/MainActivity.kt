package com.abhat.reddit

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.abhat.core.common.PreferenceHelper
import com.abhat.feed.ui.FeedFragment
import com.abhat.feed.ui.SubredditBottomSheetFragment
import com.abhat.feed.ui.constants.Constants
import com.abhat.oauth.ui.OauthFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //startActivity(Intent(this, OauthActivity::class.java))
        if (savedInstanceState == null) {
            replaceFragment(FeedFragment.newInstance(), "feed_fragment")
        }
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> {
//                    replaceFragment(OauthFragment())
                    PreferenceHelper.getTokenFromPrefs(this)?.let {
                        replaceFragment(FeedFragment.newInstance(source = "profile"), "feed_fragment")
                        true
                    } ?: run {
                        supportFragmentManager?.let {
                            OauthFragment().show(it, "oauth_bottom_sheet_fragment")
                        }
                        Handler().postDelayed({
                            bottom_navigation.menu.getItem(2).isCheckable = false
                            bottom_navigation.menu.getItem(2).isChecked = false
                        }, 1000)
                        true
                    }
                }

                R.id.front_page -> {
                    replaceFragment(FeedFragment.newInstance(), "feed_fragment")
                    true
                }

                R.id.search -> {
                    val intent = Intent()
                    intent.setClassName(this, "com.abhat.search.ui.SearchActivity")
                    startActivityForResult(intent, 9999)
                    true
                }

                R.id.subreddit -> {
                    //replaceFragment()
                    val subredditBottomSheetFragment = SubredditBottomSheetFragment.newInstance(null)
                    val feedFragment = supportFragmentManager.findFragmentByTag("feed_fragment") as? FeedFragment
                    supportFragmentManager?.let {
                        subredditBottomSheetFragment?.show(it, Constants.KEY_SUBREDDIT_BOTTOM_SHEET)
                    }
                    feedFragment?.let {
//                        (feedFragment as FeedFragment).openSubredditBottomSheet()
                        subredditBottomSheetFragment.feedFragment = it
                        Handler().postDelayed({
                            bottom_navigation.menu.getItem(0).isCheckable = true
                            bottom_navigation.menu.getItem(0).isChecked = true
                        }, 1000)
                        bottom_navigation.menu.getItem(1).isCheckable = false
                        bottom_navigation.menu.getItem(1).isChecked = false
                    } ?: run {
//                        replaceFragment(FeedFragment.newInstance(true))
                        Handler().postDelayed({
                            bottom_navigation.menu.getItem(2).isCheckable = true
                            bottom_navigation.menu.getItem(2).isChecked = true
                        }, 1000)
                        bottom_navigation.menu.getItem(1).isCheckable = false
                        bottom_navigation.menu.getItem(1).isChecked = false
                    }
                    true
                }

                else -> {
                    true
                }
            }
        }
    }

    fun replaceFragment(fragment: Fragment, tag: String = "general") {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, tag)
            .commitAllowingStateLoss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 9999) {
            if (resultCode === Activity.RESULT_OK) {
                replaceFragment(FeedFragment.newInstance(subreddit = data?.getStringExtra("subreddit") ?: "frontpage"), "feed_fragment")
            }
            if (resultCode === Activity.RESULT_CANCELED) {

            }
        }
    }
}
