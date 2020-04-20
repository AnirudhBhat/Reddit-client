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

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.profile -> {
                    startActivity(Intent(this@MainActivity, OauthActivity::class.java))
                    true
                }

                else -> {
                    true
                }
            }
        }
    }

//    fun replaceFragment(fragment: Fragment) {
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.container, fragment, "general")
//            .commitAllowingStateLoss()
//    }
}
