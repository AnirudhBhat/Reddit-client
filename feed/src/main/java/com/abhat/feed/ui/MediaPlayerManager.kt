package com.abhat.feed.ui

class MediaPlayerManager {
    fun isUrlFromImgurAndIsGif(url: String?): Boolean {
        url?.let { url ->
            return url.contains("imgur") && !(url.endsWith(".jpg") || url.endsWith(".png"))
        } ?: run {
            return false
        }
    }

    fun replaceImgurGifUrlWithMp4(url: String): String {
        return url.replace("http:", "https:")
            .replace(".gifv", ".mp4")
            .replace(".gif", ".mp4")
    }
}
