package com.abhat.reddit

import org.junit.Assert
import org.junit.Test

/**
 * Created by Anirudh Uppunda on 29,May,2020
 */
class MediaPlayerManagerTest {

    private val mediaPlayerManager = MediaPlayerManager()

    @Test
    fun `when url is from imgur and is a gif it should verify it properly`() {
        val url = "https://www.imgur.com?asd727asd.gif"
        Assert.assertTrue(mediaPlayerManager.isUrlFromImgurAndIsGif(url))
    }

    @Test
    fun `if Url is from imgur and is a gif then replace gif with mp4 `() {
        val url = "https://www.imgur.com?asd727asd.gif"
        val expectedUrl = "https://www.imgur.com?asd727asd.mp4"
        Assert.assertEquals(expectedUrl, mediaPlayerManager.replaceImgurGifUrlWithMp4(url))
    }

    @Test
    fun `if Url is from imgur and is a gifv then replace gifv with mp4 `() {
        val url = "https://www.imgur.com?asd727asd.gifv"
        val expectedUrl = "https://www.imgur.com?asd727asd.mp4"
        Assert.assertEquals(expectedUrl, mediaPlayerManager.replaceImgurGifUrlWithMp4(url))
    }

    @Test
    fun `if Url is from imgur and is a gifv then replace http with https `() {
        val url = "http://www.imgur.com?asd727asd.gifv"
        val expectedUrl = "https://www.imgur.com?asd727asd.mp4"
        Assert.assertEquals(expectedUrl, mediaPlayerManager.replaceImgurGifUrlWithMp4(url))
    }
}