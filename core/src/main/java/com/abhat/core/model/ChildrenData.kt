package com.abhat.core.model

import android.os.Parcelable
import com.abhat.core.PointsFormatter
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by Anirudh Uppunda on 04,May,2020
 */
@Parcelize
data class ChildrenData(
    var isGif: Boolean,
    var gifLink: String?,
    var isThisNews: Boolean = false,
    var indent: Int,
    var shouldUseGlideForGif: Boolean = false,
    var imageUrl: String? = null,
    @field:Json(name = "display_name")
    val displayName:String?,
    @field:Json(name="over_18")
    val over18: Boolean? = null,
    @field:Json(name = "crosspost_parent_list")
    val crossPost: List<CrossPost>?,
    @field:Json(name = "saved")
    var saved:Boolean,
    @field:Json(name = "upvoted")
    var upvoted:Boolean,
    @field:Json(name = "downvoted")
    var downvoted:Boolean,
    @field:Json(name = "likes")
    var likes:Boolean?,
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
    val id: String?,
    @field:Json(name = "is_submitter")
    val isSubmitter: Boolean,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name="num_comments")
    val numComments:Int,
    @field:Json(name = "parent_id")
    val parentId: String,
    @field:Json(name = "replies")
    val replies: RedditResponse?,
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
    val preview: Preview?,
    @field:Json(name="media")
    val media: Media?,
    @field:Json(name="secure_media")
    val secureMedia: SecureMedia?,
    @field:Json(name="is_video")
    val isVideo: Boolean = false,
    @field:Json(name="domain")
    val domain: String,
    @field:Json(name="icon_img")
    val icon: String?
): Parcelable