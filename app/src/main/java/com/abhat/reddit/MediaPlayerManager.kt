package com.abhat.reddit

class MediaPlayerManager {
    fun isUrlFromImgurAndIsGif(url: String): Boolean {
        return url.contains("imgur") && !(url.endsWith(".jpg") || url.endsWith(".png"))
    }

    fun replaceImgurGifUrlWithMp4(url: String): String {
        return url.replace("http:", "https:")
            .replace(".gifv", ".mp4")
            .replace(".gif", ".mp4")
    }
}
