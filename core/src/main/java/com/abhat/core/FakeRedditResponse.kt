package com.abhat.core

import com.abhat.core.SortType.SortType
import com.abhat.core.model.*

/**
 * Created by Anirudh Uppunda on 25,May,2020
 */

object FakeRedditResponse {
    private fun returnChildren(url: String = "",
                               isVideo: Boolean = false,
                               domain: String = "",
                               previewSize: Int = 5): MutableList<Children> {
        return mutableListOf<Children>(
            Children(
                prepareAndReturnData(
                    url,
                    isVideo,
                    domain,
                    previewSize
                ),
                "",
                1
            )
        )
    }

    fun returnRedditResponse(url: String = "", isVideo: Boolean = false,
    subreddit: String = "", sortType: SortType = SortType.hot,
                             replies: RedditResponse? = null): RedditResponse {
        return  RedditResponse(
            kind = "",
            data = Data(
                null,
                mutableListOf<Children>(
                    Children(
                        prepareAndReturnData(
                            url = url,
                            replies = replies
                        ),
                        "",
                        1,
                        isParentComment = true,
                        childrenIndex = 0
                    ),
                    Children(
                        prepareAndReturnData(
                            url = url,
                            replies = null
                        ),
                        "",
                        1,
                        isParentComment = false,
                        childrenIndex = 1
                    ),
                    Children(
                        prepareAndReturnData(
                            url = url,
                            replies = null
                        ),
                        "",
                        1,
                        isParentComment = true,
                        childrenIndex = 2
                    ),
                    Children(
                        prepareAndReturnData(
                            url = url,
                            replies = null
                        ),
                        "",
                        1,
                        isParentComment = false,
                        childrenIndex = 3
                    ),
                    Children(
                        prepareAndReturnData(
                            url = url,
                            replies = null
                        ),
                        "",
                        1,
                        isParentComment = true,
                        childrenIndex = 4
                    )
                ),
                ""
            )
        )
    }

    fun returnChildrens(): List<Children> {
        return mutableListOf<Children>(
            Children(
                prepareAndReturnData(),
                "",
                1,
                isParentComment = true,
                childrenIndex = 0
            ),
            Children(
                prepareAndReturnData(
                ),
                "",
                1,
                isParentComment = false,
                childrenIndex = 1
            ),
            Children(
                prepareAndReturnData(
                ),
                "",
                1,
                isParentComment = true,
                childrenIndex = 2
            ),
            Children(
                prepareAndReturnData(
                ),
                "",
                1,
                isParentComment = false,
                childrenIndex = 3
            ),
            Children(
                prepareAndReturnData(
                ),
                "",
                1,
                isParentComment = true,
                childrenIndex = 4
            ),
            Children(
                prepareAndReturnData(
                ),
                "",
                1,
                isParentComment = false,
                childrenIndex = 5
            ),
            Children(
                prepareAndReturnData(
                ),
                "",
                1,
                isParentComment = false,
                childrenIndex = 6
            ),
            Children(
                prepareAndReturnData(
                ),
                "",
                1,
                isParentComment = false,
                childrenIndex = 7
            ),
            Children(
                prepareAndReturnData(
                ),
                "",
                1,
                isParentComment = false,
                childrenIndex = 8
            ),
            Children(
                prepareAndReturnData(
                ),
                "",
                1,
                isParentComment = false,
                childrenIndex = 9
            ),
            Children(
                prepareAndReturnData(
                ),
                "",
                1,
                isParentComment = true,
                childrenIndex = 10
            )
        )
    }

    fun returnRedditPostDetailResponse(url: String = "",
                                       isVideo: Boolean = false,
                                        replies: RedditResponse? = null): List<RedditResponse> {
        return  listOf<RedditResponse>(
            RedditResponse(
                kind = "",
                data = Data(
                    null,
                    mutableListOf<Children>(
                        Children(
                            prepareAndReturnData(
                                url = url
                            ),
                            "",
                            1
                        )
                    ),
                    ""
                )
            ),
            RedditResponse(
            kind = "",
            data = Data(
                null,
                mutableListOf<Children>(
                    Children(
                        prepareAndReturnData(
                            url = url,
                            replies = replies
                        ),
                        "",
                        1,
                        isParentComment = true,
                        childrenIndex = 0
                    ),
                    Children(
                        prepareAndReturnData(
                            url = url,
                            replies = null
                        ),
                        "",
                        1,
                        isParentComment = false,
                        childrenIndex = 1
                    ),
                    Children(
                        prepareAndReturnData(
                            url = url,
                            replies = null
                        ),
                        "",
                        1,
                        isParentComment = true,
                        childrenIndex = 2
                    ),
                    Children(
                        prepareAndReturnData(
                            url = url,
                            replies = null
                        ),
                        "",
                        1,
                        isParentComment = false,
                        childrenIndex = 3
                    ),
                    Children(
                        prepareAndReturnData(
                            url = url,
                            replies = null
                        ),
                        "",
                        1,
                        isParentComment = true,
                        childrenIndex = 4
                    )
                ),
                ""
            )
        ))
    }

    private fun prepareAndReturnData(url: String = "",
                                     isVideo: Boolean = false, domain: String = "",
                                     previewSize: Int = 5,
                                     replies: RedditResponse? = null): ChildrenData {

        return ChildrenData(false,
            "",
            false,
            0,
            false,
            "",
            true,
            listOf(),
            false,
            true,
            false,
            "",
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
            replies,
            0,
            "",
            "",
            url,
            null,
            returnPreview(previewSize),
            null,
            domain = domain,
            isVideo = isVideo,
            secureMedia = null)
    }

    fun returnRepliesResponse(): RedditResponse {
        return RedditResponse(
            kind = "",
            data = Data(
                null,
                mutableListOf<Children>(
                    Children(
                        prepareAndReturnData(),
                        "",
                        1,
                        isParentComment = false
                    )
                ),
                ""
            )
        )
    }

    private fun returnPreview(size: Int): Preview? {
        return when (size) {
            1 -> {
                Preview(
                    listOf(
                        Images(
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
                            ))
                    ), null)
            }

            2 -> {
                Preview(
                    listOf(
                        Images(
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
                            ))
                    ), null)
            }

            3 -> {
                Preview(
                    listOf(
                        Images(
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
                            ))
                    ), null)
            }

            4 -> {
                Preview(
                    listOf(
                        Images(
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
                            ))
                    ), null)
            }

            5 -> {
                Preview(
                    listOf(
                        Images(
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
                            ))
                    ), null)
            }

            6 -> {
                Preview(
                    listOf(
                        Images(
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
                            ))
                    ), null)
            }

            else -> {
                null
            }
        }
    }
}