package com.abhat.core.common

import android.content.Context
import android.content.SharedPreferences
import com.abhat.core.model.TokenEntity
import com.google.gson.Gson

/**
 * Created by Anirudh Uppunda on 20,April,2020
 */
class PreferenceHelper {
    companion object {
        fun getTokenFromPrefs(context: Context): TokenEntity? {
            val gson = Gson()
            val json = getPreference(
                context
            ).getString("token", "")
            return gson.fromJson(json, TokenEntity::class.java)
        }

        fun storeToken(context: Context, token: TokenEntity) {
            val prefsEditor = getPreference(
                context
            ).edit()
            val gson = Gson()
            val json = gson.toJson(token)
            prefsEditor.putString("token", json)
            token.refresh_token?.let {
                if (it.isNotEmpty()) {
                    prefsEditor.putString("refresh_token", token.refresh_token)
                }
            } ?: run {
                prefsEditor.putString("refresh_token",
                    getRefreshTokenFromPrefs(
                        context
                    )
                )
            }
            prefsEditor.commit()
        }

        fun getRefreshTokenFromPrefs(context: Context): String? {
            return getPreference(
                context
            ).getString("refresh_token", "")
        }

        fun getPreference(context: Context): SharedPreferences {
            return context.getSharedPreferences(
                "com.abhat.oauth", Context.MODE_PRIVATE)
        }
    }
}