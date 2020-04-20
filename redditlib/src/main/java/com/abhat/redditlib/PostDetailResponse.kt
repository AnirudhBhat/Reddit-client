package com.abhat.redditlib

import com.squareup.moshi.Json

data class PostDetailResponse(
        @field:Json(name = "data")
        val data: Data,
        @field:Json(name = "kind")
        val kind: String
) {
    data class Data(
            @field:Json(name = "after")
            val after: Any?,
            @field:Json(name = "before")
            val before: Any?,
            @field:Json(name = "children")
            val children: MutableList<Children>,
            @field:Json(name = "dist")
            val dist: Any?,
            @field:Json(name = "modhash")
            val modhash: String
    ) {
        data class Children(
                @field:Json(name = "data")
                val data: Data,
                @field:Json(name = "kind")
                val kind: String,
                var indent: Int
        ) {
            data class Data(
                    var isGif: Boolean,
                    var gifLink: String,
                    var imageUrl: String?,
                    @field:Json(name = "crosspost_parent_list")
                    val crossPost: List<Preview.Images.Resolutions.Media.RedditVideo.Oembed.CrossPost>,
                    @field:Json(name = "saved")
                    var saved:Boolean,
                    @field:Json(name = "upvoted")
                    var upvoted:Boolean,
                    @field:Json(name = "downvoted")
                    var downvoted:Boolean,
                    @field:Json(name = "likes")
                    var likes:Any,
                    @field:Json(name = "subreddit")
                    val subreddit:String,
                    @field:Json(name = "subreddit_id")
                    val subredditId:String,
                    @field:Json(name = "link_id")
                    val linkId:String,
                    @field:Json(name = "author")
                    val author:String,
                    @field:Json(name = "body")
                    val body: String,
                    @field:Json(name = "body_html")
                    val bodyHtml:String?,
                    @field:Json(name = "selftext_html")
                    val selfTextHtml:String?,
                    @field:Json(name = "children")
                    val children: List<String>?,
                    @field:Json(name = "count")
                    val count: Int,
                    @field:Json(name = "created_utc")
                    val createdUtc:Long,
                    @field:Json(name = "depth")
                    val depth: Int,
                    @field:Json(name = "id")
                    val id: String,
                    @field:Json(name = "is_submitter")
                    val isSubmitter: Boolean,
                    @field:Json(name = "name")
                    val name: String,
                    @field:Json(name="num_comments")
                    val numComments:Int,
                    @field:Json(name = "parent_id")
                    val parentId: String,
                    @field:Json(name = "replies")
                    val replies: PostDetailResponse?,
                    @field:Json(name="score")
                    val score:Int,
                    @field:Json(name="title")
                    val title:String,
                    @field:Json(name="link_title")
                    val linkTitle:String,
                    @field:Json(name="url")
                    val url:String?,
                    @field:Json(name="thumbnail")
                    val thumbnail:String?,
                    @field:Json(name="preview")
                    val preview:Preview?,
                    @field:Json(name="media")
                    val media: Preview.Images.Resolutions.Media?
            ) {
                data class Preview(
                        @field:Json(name="images")
                        val images:List<Images>?
                ) {
                    data class Images(
                            @field:Json(name="resolutions")
                            val resolutions:List<Resolutions>?
                    ) {
                        data class Resolutions(
                                @field:Json(name="url")
                                val url:String?
                        ) {
                            data class Media(
                                    @field:Json(name="reddit_video")
                                    val redditVideo:RedditVideo?,
                                    @field:Json(name="oembed")
                                    val oembed: RedditVideo.Oembed?,
                                    @field:Json(name="type")
                                    val type: String?
                            ) {
                                data class RedditVideo(
                                        @field:Json(name="fallback_url")
                                        val fallbackUrl:String?,
                                        @field:Json(name="dash_url")
                                        val dashUrl:String?,
                                        @field:Json(name="scrubber_media_url")
                                        val scrubberMediaUrl:String?,
                                        @field:Json(name="hls_url")
                                        val hlsUrl:String?,
                                        @field:Json(name="is_gif")
                                        val isGif:Boolean?
                                ) {
                                    data class Oembed(
                                            @field:Json(name="thumbnail_url")
                                            val thumbnailUrl:String?
                                    ) {
                                        data class CrossPost(
                                                @field:Json(name = "secure_media")
                                                val secureMedia:SecureMedia?
                                        ) {
                                            data class SecureMedia(
                                                    @field:Json(name = "reddit_video")
                                                    val redditVideo:RedditVideo?,
                                                    @field:Json(name="oembed")
                                                    val oembed: RedditVideo.Oembed?
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}