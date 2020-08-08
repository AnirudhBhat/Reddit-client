package com.abhat.core.extensions

import android.util.Base64

/**
 * Created by Anirudh Uppunda on 12,March,2020
 */

fun String.encodeBase64ToString(): String {
    return Base64.encodeToString("$this:".toByteArray(), Base64.NO_WRAP)
}