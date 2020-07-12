package com.abhat.reddit

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.abhat.feed.ui.FeedFragment
import com.abhat.oauth.ui.OauthActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //startActivity(Intent(this, OauthActivity::class.java))
        replaceFragment(FeedFragment.newInstance())
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> {
                    startActivity(Intent(this@MainActivity, OauthActivity::class.java))
                    true
                }

                R.id.front_page -> {
                    replaceFragment(FeedFragment.newInstance())
                    true
                }

                R.id.subreddit -> {
                    //replaceFragment()
                    replaceFragment(FeedFragment.newInstance(true))
                    Handler().postDelayed({
                        bottom_navigation.menu.getItem(0).isCheckable = true
                        bottom_navigation.menu.getItem(0).isChecked = true
                    }, 1000)
                    bottom_navigation.menu.getItem(1).isCheckable = false
                    bottom_navigation.menu.getItem(1).isChecked = false
                    true
                }

                else -> {
                    true
                }
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment, "general")
            .commitAllowingStateLoss()
    }
}
