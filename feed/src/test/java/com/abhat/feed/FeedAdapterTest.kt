package com.abhat.feed

import android.widget.RelativeLayout
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.model.*
import com.abhat.feed.ui.FeedViewModel
import com.abhat.feed.ui.FeedAdapter
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Anirudh Uppunda on 09,May,2020
 */
class FeedAdapterTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var feedViewModel: FeedViewModel
    private lateinit var feedRepositoryImpl: FakeFeedRepositorySuccessResponse
    private lateinit var observer: Observer<Pair<Boolean, RelativeLayout?>>
    private lateinit var feedAdapter: FeedAdapter

    @Before
    fun setup() {
        feedRepositoryImpl = FakeFeedRepositorySuccessResponse()
        observer = mock()
        feedViewModel = FeedViewModel(feedRepositoryImpl,
            TestContextProvider()
        )
        feedAdapter = FeedAdapter(
            null,
            null,
            feedViewModel,
            returnChildren(),
            TestContextProvider()
        )
    }

    @Test
    fun `calling get points with 11000 should return 11k`() {
        runBlocking {
            Assert.assertEquals("11k points", feedAdapter.getPoints("11000"))
        }
    }

    @Test
    fun `calling get comments with 11000 should return 11k`() {
        runBlocking {
            Assert.assertEquals("11k comments", feedAdapter.getComments("11000"))
        }
    }

    @Test
    fun `calling get points with 11 should return 11`() {
        runBlocking {
            Assert.assertEquals("11 points", feedAdapter.getPoints("11"))
        }
    }

    @Test
    fun `calling get comments with 11 should return 11`() {
        runBlocking {
            Assert.assertEquals("11 comments", feedAdapter.getComments("11"))
        }
    }

    @Test
    fun `calling get points with 1000 should return 1k`() {
        runBlocking {
            Assert.assertEquals("1k points", feedAdapter.getPoints("1000"))
        }
    }

    @Test
    fun `calling get comments with 1000 should return 1k`() {
        runBlocking {
            Assert.assertEquals("1k comments", feedAdapter.getComments("1000"))
        }
    }

    @Test
    fun `reddit response with url which ends with jpg or png and is not of type video is not a news and should return false`() {
        runBlocking {
            Assert.assertEquals(false, feedAdapter.isItNews(returnChildren("http://www.testurl.com.jpg", false), 0))
        }
    }

    @Test
    fun `reddit response with url which ends with v dot redd dot it and is not of type video is not a news and should return false`() {
        runBlocking {
            Assert.assertEquals(false, feedAdapter.isItNews(returnChildren("http://v.redd.it/qujasdh51", false), 0))
        }
    }

    @Test
    fun `reddit response with url which DOES NOT ends with jpg or png and is not of type video, IS a news and should return true`() {
        runBlocking {
            Assert.assertEquals(true, feedAdapter.isItNews(returnChildren("http://www.testurl.com", false), 0))
        }
    }

    @Test
    fun `reddit response with url which DOES NOT ends with jpg or png and is not of type video BUT is gif should return false`() {
        runBlocking {
            Assert.assertEquals(false, feedAdapter.isItNews(returnChildren("http://www.testurlgif.com", false), 0))
        }
    }

    @Test
    fun `reddit response with url which DOES NOT ends with jpg or png and is not of type video BUT is gifv should return false`() {
        runBlocking {
            Assert.assertEquals(false, feedAdapter.isItNews(returnChildren("http://www.testurl.gifv", false), 0))
        }
    }

    @Test
    fun `reddit response with url which DOES NOT ends with jpg or png and is not of type video BUT is from gfycat should return false`() {
        runBlocking {
            Assert.assertEquals(false, feedAdapter.isItNews(returnChildren("http://www.testurlgfycat.com", false), 0))
        }
    }


    @Test
    fun `reddit response with url which DOES NOT ends with jpg or png and IS of type video should return false`() {
        runBlocking {
            Assert.assertEquals(false, feedAdapter.isItNews(returnChildren("http://www.testurl.com", true), 0))
        }
    }

    @Test
    fun `getNewsDomainAndSourceUrl method should return proper domain and url source`() {
        runBlocking {
            Assert.assertEquals(Pair("cnbc", "http://www.cnbc/index.com"), feedAdapter.getNewsDomainAndSourceUrl(returnChildren("http://www.cnbc/index.com", false, "http://www.testurl.com"), 0))
        }
    }

    @Test
    fun `getHostNameFromUrl method should return vishwavani as host for vishwavani web url`() {
        runBlocking {
            Assert.assertEquals("vishwavani", feedAdapter.getHostNameFromUrl("http://www.vishwavani.com"))
        }
    }

    @Test
    fun `getHostNameFromUrl method should return ycombinator as host for hackernews web url`() {
        runBlocking {
            Assert.assertEquals("news.ycombinator", feedAdapter.getHostNameFromUrl("http://news.ycombinator.com"))
        }
    }

    @Test
    fun `getHostNameFromUrl method should return null as host for invalid url`() {
        runBlocking {
            Assert.assertEquals(null, feedAdapter.getHostNameFromUrl("invalidurl"))
        }
    }

    @Test
    fun `getHostNameFromUrl method should return null as host for empty url`() {
        runBlocking {
            Assert.assertEquals(null, feedAdapter.getHostNameFromUrl(""))
        }
    }

    @Test
    fun `isItAGif method must NOT return true if url is type reddit domain gif`() {
        runBlocking {
            Assert.assertEquals(false, feedAdapter.isItAGif(returnChildren(url = "https://v.redd.it/py2ql468uqx41"), position = 0))
        }
    }

    @Test
    fun `isItAGif method must return true if url ends with gif`() {
        runBlocking {
            Assert.assertEquals(true, feedAdapter.isItAGif(returnChildren(url = "https://test/py2ql468uqx41.gif"), position = 0))
        }
    }

    @Test
    fun `isItAGif method must return true if url ends with gifv`() {
        runBlocking {
            Assert.assertEquals(true, feedAdapter.isItAGif(returnChildren(url = "https://test/py2ql468uqx41.gifv"), position = 0))
        }
    }

    @Test
    fun `isItAGif method must NOT return true if url contains gfycat`() {
        runBlocking {
            Assert.assertEquals(false, feedAdapter.isItAGif(returnChildren(url = "https://testgfycat/py2ql468uqx41"), position = 0))
        }
    }

    @Test
    fun `isItAVideo method must return true if url contains youtu`() {
        runBlocking {
            Assert.assertEquals(true, feedAdapter.isItAVideo(returnChildren(url = "https://youtu.be/py2ql468uqx41"), position = 0))
        }
    }

    @Test
    fun `isItAVideo method must return true if isVideo is true`() {
        runBlocking {
            Assert.assertEquals(true, feedAdapter.isItAVideo(returnChildren(url = "https://test/py2ql468uqx41", isVideo = true), position = 0))
        }
    }

    @Test
    fun `selectAppropriateResolution method must return 5th resolution if there are 6 resolutions`() {
        runBlocking {
            Assert.assertEquals("https://preview.redd.it/2zy3jpx2hay41.jpg?width=1080&crop=smart&auto=webp&s=cdc1e5d25199b9574e8106043cbfc7e630cb588d",
                feedAdapter.selectAppropriateResolution(returnChildren(previewSize = 6), 0))
        }
    }

    @Test
    fun `selectAppropriateResolution method must return 4th resolution if there are 5 resolutions`() {
        runBlocking {
            Assert.assertEquals("https://preview.redd.it/2zy3jpx2hay41.jpg?width=960&crop=smart&auto=webp&s=8eb8a1e4a51b02037a7c954090d702115abff850",
                feedAdapter.selectAppropriateResolution(returnChildren(previewSize = 5), 0))
        }
    }

    @Test
    fun `selectAppropriateResolution method must return 3th resolution if there are 4 resolutions`() {
        runBlocking {
            Assert.assertEquals("https://preview.redd.it/2zy3jpx2hay41.jpg?width=640&crop=smart&auto=webp&s=b773717885536f1d8c0be3b76807e12e30014b16",
                feedAdapter.selectAppropriateResolution(returnChildren(previewSize = 4), 0))
        }
    }

    @Test
    fun `selectAppropriateResolution method must return 2nd resolution if there are 3 resolutions`() {
        runBlocking {
            Assert.assertEquals("https://preview.redd.it/2zy3jpx2hay41.jpg?width=320&crop=smart&auto=webp&s=028a15ce13780a3fa0e817918f6ed8f6b16142fc",
                feedAdapter.selectAppropriateResolution(returnChildren(previewSize = 3), 0))
        }
    }

    @Test
    fun `selectAppropriateResolution method must return 1st resolution if there are 2 resolutions`() {
        runBlocking {
            Assert.assertEquals("https://preview.redd.it/2zy3jpx2hay41.jpg?width=216&crop=smart&auto=webp&s=d8a8fa6b732472f52cb0a64a1b4a63390a1c6c92",
                feedAdapter.selectAppropriateResolution(returnChildren(previewSize = 2), 0))
        }
    }

    @Test
    fun `selectAppropriateResolution method must return 0th resolution if there are 1 resolutions`() {
        runBlocking {
            Assert.assertEquals("https://preview.redd.it/2zy3jpx2hay41.jpg?width=108&crop=smart&auto=webp&s=5eb3e6cf14f8225498251ab8a2764341c860bae6",
                feedAdapter.selectAppropriateResolution(returnChildren(previewSize = 1), 0))
        }
    }

    @Test
    fun `shouldShowNewsSourceOverlay method must return false if there is no preview model object`() {
        runBlocking {
            Assert.assertEquals(false,
                feedAdapter.shouldShowNewsSourceOverlay(returnChildren(previewSize = 0), 0))
        }
    }

    @Test
    fun `shouldShowNewsSourceOverlay method must return true if there is preview model object and we have 1 image resolution`() {
        runBlocking {
            Assert.assertEquals(true,
                feedAdapter.shouldShowNewsSourceOverlay(returnChildren(previewSize = 1), 0))
        }
    }

    @Test
    fun `shouldShowNewsSourceOverlay method must return true if there is preview model object and we have 2 image resolution`() {
        runBlocking {
            Assert.assertEquals(true,
                feedAdapter.shouldShowNewsSourceOverlay(returnChildren(previewSize = 2), 0))
        }
    }

    @Test
    fun `shouldShowNewsSourceOverlay method must return true if there is preview model object and we have 3 image resolution`() {
        runBlocking {
            Assert.assertEquals(true,
                feedAdapter.shouldShowNewsSourceOverlay(returnChildren(previewSize = 3), 0))
        }
    }

    @Test
    fun `shouldShowNewsSourceOverlay method must return true if there is preview model object and we have 4 image resolution`() {
        runBlocking {
            Assert.assertEquals(true,
                feedAdapter.shouldShowNewsSourceOverlay(returnChildren(previewSize = 4), 0))
        }
    }

    @Test
    fun `shouldShowNewsSourceOverlay method must return true if there is preview model object and we have 5 image resolution`() {
        runBlocking {
            Assert.assertEquals(true,
                feedAdapter.shouldShowNewsSourceOverlay(returnChildren(previewSize = 5), 0))
        }
    }

    @Test
    fun `shouldShowNewsSourceOverlay method must return true if there is preview model object and we have 6 image resolution`() {
        runBlocking {
            Assert.assertEquals(true,
                feedAdapter.shouldShowNewsSourceOverlay(returnChildren(previewSize = 6), 0))
        }
    }

    @Test
    fun `isItGifFromReddit must return true and should contain valid gifLink and shouldUseGlideForGif flag set`() {
        runBlocking {
            val response = returnChildren(secureMedia = returnSecureMedia(url = "http://www.google.com/", type = null))
            val expectedResponse = response
            Assert.assertTrue(feedAdapter.isItAGifFromReddit(response, 0))
            Assert.assertEquals(response[0].data?.gifLink, "http://www.google.com/")
            Assert.assertFalse(response[0].data?.shouldUseGlideForGif)
        }
    }

    @Test
    fun `isItGifFromGfyCat must return false if type in secure media does not contain word 'gfycat'`() {
        runBlocking {
            val response = returnChildren(secureMedia = returnSecureMedia(url = "https://www.google.com/asdasdff"))
            val expectedResponse = response
            Assert.assertFalse(feedAdapter.isItAGifFromGfycat(response, 0))
        }
    }

    @Test
    fun `isItGifFromGfyCat must return true if type in secure media does contains word 'gfycat'`() {
        runBlocking {
            val response = returnChildren(secureMedia = returnSecureMedia(url = "https://www.asdgfycat.com/asdasdff", type = "gfycat"))
            val expectedResponse = response
            Assert.assertTrue(feedAdapter.isItAGifFromGfycat(response, 0))
        }
    }

    @Test
    fun `isItAGifFromOtherSource must return true if the source is not from reddit or gfycat but it is a gif`() {
        runBlocking {
            val response = returnChildren(url = "http://www.google.gif", secureMedia = returnSecureMedia(url = "http://www.google.gif", type = ""))
            Assert.assertTrue(feedAdapter.isItGifFromOtherSource(response, 0))
        }
    }

    @Test
    fun `isItAGifFromOtherSource must return false if the source is from reddit`() {
        runBlocking {
            val response = returnChildren(url = "http://www.google.gif", secureMedia = returnSecureMedia(url = "http://www.google.gif", type = null))
            Assert.assertFalse(feedAdapter.isItGifFromOtherSource(response, 0))
        }
    }

    @Test
    fun `isItAGifFromOtherSource must return false if the source is from gfycat`() {
        runBlocking {
            val response = returnChildren(url = "http://www.google.gif", secureMedia = returnSecureMedia(url = "http://www.google.gif", type = "gfycat"))
            Assert.assertFalse(feedAdapter.isItGifFromOtherSource(response, 0))
        }
    }



    class TestContextProvider : CoroutineContextProvider() {
        override val Main: CoroutineDispatcher = Dispatchers.Unconfined
        override val IO: CoroutineDispatcher = Dispatchers.Unconfined
    }

    private fun returnData(): Data {
        return Data(
            null,
            mutableListOf<Children>(
                Children(
                    prepareAndReturnData(),
                    "",
                    1
                )
            ),
            ""
        )
    }

    private fun returnChildren(url: String = "",
                               isVideo: Boolean = false,
                               domain: String = "",
                               previewSize: Int = 5, secureMedia: SecureMedia? = null): MutableList<Children> {
        return mutableListOf<Children>(
            Children(
                prepareAndReturnData(url, isVideo, domain, previewSize, secureMedia),
                "",
                1
            )
        )
    }

    private fun returnRedditResponse(url: String, isVideo: Boolean): RedditResponse {
        return  RedditResponse(
            kind = "",
            data = Data(
                null,
                mutableListOf<Children>(
                    Children(
                    prepareAndReturnData(url = url),
                        "",
                        1
                )
                ),
                ""
            )
        )
    }

    private fun returnSecureMedia(url: String = "http://www.google.com/", type: String? = null): SecureMedia {
        return SecureMedia(
            redditVideo = RedditVideo(
                fallbackUrl = "",
                dashUrl = "",
                scrubberMediaUrl = "",
                hlsUrl = url,
                isGif = false
            ),
            oembed = null,
            type = type
        )
    }

    private fun prepareAndReturnData(url: String = "",
                                     isVideo: Boolean = false, domain: String = "",
                                     previewSize: Int = 5, secureMedia: SecureMedia? = null, displaName: String? = null): ChildrenData {

        return ChildrenData(false,
                "",
            false,
            0,
            true,
                "",
            displaName,
                true,
                listOf(),
                false,
                true,
                false,
                false,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                listOf(),
                0,
                0,
                0,
                "",
                false,
                "",
                0,
                "",
                null,
                0,
                "",
                "",
                url,
        null,
        returnPreview(previewSize),
        null,
            domain = domain,
        isVideo = isVideo,
        secureMedia = secureMedia)
    }

    private fun returnPreview(size: Int): Preview? {
        return when (size) {
            1 -> {
                Preview(
                    listOf(Images(
                        source = Resolutions(
                            url = "https://preview.redd.it/2zy3jpx2hay41.jpg?auto=webp&amp;s=86befbbde9b6f4e9545e9b751471d67bdb3352ed",
                            width = 0,
                            height = 0
                        ),
                        resolutions = listOf(
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=108&amp;crop=smart&amp;auto=webp&amp;s=5eb3e6cf14f8225498251ab8a2764341c860bae6",
                                width = 108,
                                height = 108
                            )
                    ))), null)
            }

            2 -> {
                Preview(
                    listOf(Images(
                        source = Resolutions(
                            url = "https://preview.redd.it/2zy3jpx2hay41.jpg?auto=webp&amp;s=86befbbde9b6f4e9545e9b751471d67bdb3352ed",
                            width = 0,
                            height = 0
                        ),
                        resolutions = listOf(
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=108&amp;crop=smart&amp;auto=webp&amp;s=5eb3e6cf14f8225498251ab8a2764341c860bae6",
                                width = 108,
                                height = 108
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=216&amp;crop=smart&amp;auto=webp&amp;s=d8a8fa6b732472f52cb0a64a1b4a63390a1c6c92",
                                width = 216,
                                height = 216
                            )
                        ))), null)
            }

            3 -> {
                Preview(
                    listOf(Images(
                        source = Resolutions(
                            url = "https://preview.redd.it/2zy3jpx2hay41.jpg?auto=webp&amp;s=86befbbde9b6f4e9545e9b751471d67bdb3352ed",
                            width = 0,
                            height = 0
                        ),
                        resolutions = listOf(
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=108&amp;crop=smart&amp;auto=webp&amp;s=5eb3e6cf14f8225498251ab8a2764341c860bae6",
                                width = 108,
                                height = 108
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=216&amp;crop=smart&amp;auto=webp&amp;s=d8a8fa6b732472f52cb0a64a1b4a63390a1c6c92",
                                width = 216,
                                height = 216
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=320&amp;crop=smart&amp;auto=webp&amp;s=028a15ce13780a3fa0e817918f6ed8f6b16142fc",
                                width = 320,
                                height = 320
                            )
                        ))), null)
            }

            4 -> {
                Preview(
                    listOf(Images(
                        source = Resolutions(
                            url = "https://preview.redd.it/2zy3jpx2hay41.jpg?auto=webp&amp;s=86befbbde9b6f4e9545e9b751471d67bdb3352ed",
                            width = 0,
                            height = 0
                        ),
                        resolutions = listOf(
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=108&amp;crop=smart&amp;auto=webp&amp;s=5eb3e6cf14f8225498251ab8a2764341c860bae6",
                                width = 108,
                                height = 108
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=216&amp;crop=smart&amp;auto=webp&amp;s=d8a8fa6b732472f52cb0a64a1b4a63390a1c6c92",
                                width = 216,
                                height = 216
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=320&amp;crop=smart&amp;auto=webp&amp;s=028a15ce13780a3fa0e817918f6ed8f6b16142fc",
                                width = 320,
                                height = 320
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=640&amp;crop=smart&amp;auto=webp&amp;s=b773717885536f1d8c0be3b76807e12e30014b16",
                                width = 640,
                                height = 640
                            )
                        ))), null)
            }

            5 -> {
                Preview(
                    listOf(Images(
                        source = Resolutions(
                            url = "https://preview.redd.it/2zy3jpx2hay41.jpg?auto=webp&amp;s=86befbbde9b6f4e9545e9b751471d67bdb3352ed",
                            width = 0,
                            height = 0
                        ),
                        resolutions = listOf(
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=108&amp;crop=smart&amp;auto=webp&amp;s=5eb3e6cf14f8225498251ab8a2764341c860bae6",
                                width = 108,
                                height = 108
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=216&amp;crop=smart&amp;auto=webp&amp;s=d8a8fa6b732472f52cb0a64a1b4a63390a1c6c92",
                                width = 216,
                                height = 216
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=320&amp;crop=smart&amp;auto=webp&amp;s=028a15ce13780a3fa0e817918f6ed8f6b16142fc",
                                width = 320,
                                height = 320
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=640&amp;crop=smart&amp;auto=webp&amp;s=b773717885536f1d8c0be3b76807e12e30014b16",
                                width = 640,
                                height = 640
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=960&amp;crop=smart&amp;auto=webp&amp;s=8eb8a1e4a51b02037a7c954090d702115abff850",
                                width = 960,
                                height = 960
                            )
                        ))), null)
            }

            6 -> {
                Preview(
                    listOf(Images(
                        source = Resolutions(
                            url = "https://preview.redd.it/2zy3jpx2hay41.jpg?auto=webp&amp;s=86befbbde9b6f4e9545e9b751471d67bdb3352ed",
                            width = 0,
                            height = 0
                        ),
                        resolutions = listOf(
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=108&amp;crop=smart&amp;auto=webp&amp;s=5eb3e6cf14f8225498251ab8a2764341c860bae6",
                                width = 108,
                                height = 108
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=216&amp;crop=smart&amp;auto=webp&amp;s=d8a8fa6b732472f52cb0a64a1b4a63390a1c6c92",
                                width = 216,
                                height = 216
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=320&amp;crop=smart&amp;auto=webp&amp;s=028a15ce13780a3fa0e817918f6ed8f6b16142fc",
                                width = 320,
                                height = 320
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=640&amp;crop=smart&amp;auto=webp&amp;s=b773717885536f1d8c0be3b76807e12e30014b16",
                                width = 640,
                                height = 640
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=960&amp;crop=smart&amp;auto=webp&amp;s=8eb8a1e4a51b02037a7c954090d702115abff850",
                                width = 960,
                                height = 960
                            ),
                            Resolutions(
                                url = "https://preview.redd.it/2zy3jpx2hay41.jpg?width=1080&amp;crop=smart&amp;auto=webp&amp;s=cdc1e5d25199b9574e8106043cbfc7e630cb588d",
                                width = 1080,
                                height = 1080
                            )
                        ))), null)
            }

            else -> {
               null
            }
        }
    }

}