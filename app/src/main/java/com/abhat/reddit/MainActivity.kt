package com.abhat.reddit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

    fun openSortBottomSheet() {
        val sortBottomSheet = SortBottomSheet()
        sortBottomSheet.show(supportFragmentManager, "sort_bottom_sheet")
    }
}
