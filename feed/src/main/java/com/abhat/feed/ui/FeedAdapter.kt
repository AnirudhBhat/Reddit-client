package com.abhat.feed.ui

import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.abhat.core.PointsFormatter
import com.abhat.core.SortType.SortType
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.model.Children
import com.abhat.feed.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.activity_reddit_card.view.*
import kotlinx.coroutines.*
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
open class FeedAdapter(
    private val context: FragmentActivity? = null,
    private val fragment: FeedFragment? = null,
    private val feedViewModel: FeedViewModel,
    private var redditData: MutableList<Children>? = null,
    private val contextProvider: CoroutineContextProvider
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var view: FeedViewHolder? = null
    private var trendingAndSortViewHolder: TrendingAndSortViewHolder? = null
    private val ioScope = CoroutineScope(contextProvider.IO + SupervisorJob())
    private val mainScope = CoroutineScope(contextProvider.Main + SupervisorJob())

    private var sortType: SortType = SortType.hot

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        return if (viewType == 0) {
            trendingAndSortViewHolder =  TrendingAndSortViewHolder(layoutInflater.inflate(R.layout.item_trending_and_sort, parent, false), fragment)
            trendingAndSortViewHolder?.sortType = sortType
            trendingAndSortViewHolder as TrendingAndSortViewHolder
        } else {
            view = FeedViewHolder(layoutInflater.inflate(R.layout.activity_reddit_card, parent, false))
            view as FeedViewHolder
        }
    }

    override fun getItemCount(): Int {
        return redditData?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun getRedditData(): List<Children>? {
        return redditData
    }

    fun updateRedditData(redditData: MutableList<Children>?, sortType: SortType) {
        this.redditData = redditData
        // This variable is needed for the very first time the app opens and
        // TrendingAndSortViewHolder is not created yet we don't know the sort type yet.
        this.sortType = sortType
        notifyDataSetChanged()
        trendingAndSortViewHolder?.sortType = sortType
    }

    fun addRedditData(redditData: List<Children>?, sortType: SortType) {
        trendingAndSortViewHolder?.sortType = sortType
        this.sortType = sortType
        redditData?.let { redditData ->
            this.redditData?.addAll(redditData)
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            (holder as TrendingAndSortViewHolder).bind()
        } else {
            ioScope.launch {
                val title = redditData?.get(position - 1)?.data?.title ?: redditData?.get(position - 1)?.data?.bodyHtml ?: ""
                val author = redditData?.get(position - 1)?.data?.author ?: ""
                val points = redditData?.get(position - 1)?.data?.score?.toString() ?: ""
                val comments = redditData?.get(position - 1)?.data?.numComments?.toString() ?: ""
                val subreddit = redditData?.get(position - 1)?.data?.subreddit ?: ""
//        val bodyHtml = redditData?.children?.get(position - 1)?.data?.bodyHtml ?: ""
                val created = redditData?.get(position - 1)?.data?.createdUtc?.times(1000) ?: 0
                val over18 = redditData?.get(position - 1)?.data?.over18 ?: false
                withContext(contextProvider.Main) {
                    (holder as FeedViewHolder).bind(
                        title, author, points, comments, subreddit, "",
                        created, "", over18, position - 1
                    )
                }
            }
        }

//        val selfTextHtml = redditData?.children?.get(position)?.data?.selfTextHtml ?: ""
    }

    suspend fun getPoints(pointsString: String): String {
        return withContext(ioScope.coroutineContext) {
            PointsFormatter.format(pointsString.toLong()) + " points"
        }
    }

    suspend fun getComments(commentsString: String): String {
        return withContext(ioScope.coroutineContext) {
            PointsFormatter.format(commentsString.toLong()) + " comments"
        }
    }

    suspend fun getFormattedDate(date: Date): String {
        return withContext(ioScope.coroutineContext) {
            dateFormatter(date)
        }
    }

    suspend fun isItNews(redditData: MutableList<Children>?, position: Int): Boolean {
        return withContext(ioScope.coroutineContext) {
            val url = redditData?.get(position)?.data?.url ?: ""
            val isVideo = redditData?.get(position)?.data?.isVideo!!
            withContext(mainScope.coroutineContext) {
                val result = (!url.endsWith(".jpg")
                        && !url.endsWith(".png")
                        && !url.contains("gifv")
                        && !url.contains("gif")
                        && !url.contains("gfycat")
                        && !url.contains("v.redd.it")
                        && !isVideo)
                if (result) redditData?.get(position).data?.isThisNews = true
                result
            }
        }
    }

    suspend fun shouldShowNewsSourceOverlay(
        redditData: MutableList<Children>?,
        position: Int
    ): Boolean {
        return withContext(ioScope.coroutineContext) {
            redditData?.get(position)?.data?.preview != null
        }
    }

    suspend fun getNewsDomainAndSourceUrl(
        redditData: MutableList<Children>?,
        position: Int
    ): Pair<String, String> {
        return withContext(ioScope.coroutineContext) {
            val data = redditData?.get(position)?.data ?: null
            Pair(getHostNameFromUrl(data?.url ?: "") ?: "", data?.url ?: "")
        }
    }

    suspend fun isItAGif(redditData: MutableList<Children>?, position: Int): Boolean {
        return withContext(ioScope.coroutineContext) {
            val url = redditData?.get(position)?.data?.url ?: ""
            withContext(mainScope.coroutineContext) {
                if ((url.endsWith("gifv")
                            || url.endsWith("gif"))
                ) {
                    redditData?.get(position)?.data?.shouldUseGlideForGif = true
                    redditData?.get(position)?.data?.gifLink = url
                    true
                } else {
                    false
                }
            }
        }
    }

    suspend fun isItAGifFromGfycat(redditData: MutableList<Children>?, position: Int): Boolean {
        return withContext(ioScope.coroutineContext) {
            val data = redditData?.get(position)?.data
            val type = data?.secureMedia?.type ?: ""
            if (type.contains("gfycat")) {
                data?.preview?.redditVideoPreview?.hlsUrl?.let {
                    data?.gifLink = it
                    data?.shouldUseGlideForGif = false
                } ?: run {
                    if (data?.crossPost?.size ?: 0 > 0) {
                        data?.crossPost?.get(0)?.secureMedia?.oembed?.let { oembed ->
                            data?.gifLink = data?.secureMedia?.oembed?.thumbnailUrl
                            data?.shouldUseGlideForGif = true
                        } ?: run {
                            data?.gifLink = data?.secureMedia?.oembed?.thumbnailUrl
                            data?.shouldUseGlideForGif = true
                        }
                    } else {
                        data?.gifLink = data?.secureMedia?.oembed?.thumbnailUrl
                        data?.shouldUseGlideForGif = true
                    }
                }
                true
            } else {
                false
            }
        }
    }

    suspend fun isItAGifFromReddit(redditData: MutableList<Children>?, position: Int): Boolean {
        return withContext(ioScope.coroutineContext) {
            val data = redditData?.get(position)?.data
            val type = data?.secureMedia?.type ?: null
            if (type == null) {
                data?.secureMedia?.redditVideo?.let { redditVideo ->
                    data?.gifLink = redditVideo.hlsUrl
                    data?.shouldUseGlideForGif = false
                    true
                } ?: run {
                    if (data?.crossPost?.size ?: 0 > 0) {
                        data?.crossPost?.get(0)?.secureMedia?.redditVideo?.let { redditVideo ->
                            data?.gifLink = redditVideo.hlsUrl
                            data?.shouldUseGlideForGif = false
                            data?.url?.contains("v.redd.it") ?: false
                        } ?: run {
                            if (data?.url?.contains("v.redd.it") == true) {
                                data?.gifLink = data?.url
                                data?.shouldUseGlideForGif = true
                                true
                            } else {
                                data?.gifLink = null
                                data?.shouldUseGlideForGif = false
                                false
                            }
//                            data?.url?.contains("v.redd.it") ?: false
                        }
                    } else {
//                        data?.gifLink = data?.url
//                        data?.shouldUseGlideForGif = true
//                        data?.url?.contains("v.redd.it") ?: false
                        if (data?.url?.contains("v.redd.it") == true) {
                            data?.gifLink = data?.url
                            data?.shouldUseGlideForGif = true
                            true
                        } else {
                            data?.gifLink = null
                            data?.shouldUseGlideForGif = false
                            false
                        }
                    }
                }
            } else {
                false
            }
        }
    }

    suspend fun isItGifFromOtherSource(redditData: MutableList<Children>?, position: Int): Boolean {
        return withContext(ioScope.coroutineContext) {
            val url = redditData?.get(position)?.data?.url ?: ""
            if (!isItAGifFromReddit(redditData, position) &&
                !isItAGifFromGfycat(redditData, position) &&
                isItAGif(redditData, position)
            ) {
                redditData?.get(position)?.data?.gifLink = url
                true
            } else {
                false
            }
        }
    }

    suspend fun isItAVideo(redditData: MutableList<Children>?, position: Int): Boolean {
        return withContext(ioScope.coroutineContext) {
            (!isItAGifFromReddit(redditData, position) &&
                    (redditData?.get(position)?.data?.isVideo ?: false
                            || redditData?.get(position)?.data?.url?.contains(
                        "youtu"
                    ) ?: false))
        }
    }

    fun getHostNameFromUrl(url: String): String? {
        val hostname = URI(url).host
        hostname?.let { hostname ->
            val host = if (hostname.startsWith("www.")) hostname.substring(4) else hostname
            return if (host.contains(".")) host.substring(0, host.lastIndexOf(".")) else host
        }
        return null
    }

    suspend fun selectAppropriateResolution(
        redditData: MutableList<Children>?,
        adapterPosition: Int
    ): String? {
        return withContext(ioScope.coroutineContext) {
            redditData?.get(adapterPosition)?.data?.preview?.let {
                when {
                    redditData?.get(adapterPosition)?.data?.preview?.images?.get(0)?.resolutions?.size ?: 0 >= 6 -> {
                        redditData?.get(adapterPosition)?.data?.preview?.images?.get(0)?.resolutions?.get(
                            5
                        )?.url?.replace(
                            "amp;s",
                            "s"
                        )?.replace("amp;", "")
                    }
                    redditData?.get(adapterPosition)?.data?.preview?.images?.get(0)?.resolutions?.size ?: 0 >= 5 -> {
                        redditData?.get(adapterPosition)?.data?.preview?.images?.get(0)?.resolutions?.get(
                            4
                        )?.url?.replace(
                            "amp;s",
                            "s"
                        )?.replace("amp;", "")
                    }
                    redditData?.get(adapterPosition)?.data?.preview?.images?.get(0)?.resolutions?.size ?: 0 >= 4 -> {
                        redditData?.get(adapterPosition)?.data?.preview?.images?.get(0)?.resolutions?.get(
                            3
                        )?.url?.replace(
                            "amp;s",
                            "s"
                        )?.replace("amp;", "")
                    }
                    redditData?.get(adapterPosition)?.data?.preview?.images?.get(0)?.resolutions?.size ?: 0 >= 3 -> {
                        redditData?.get(adapterPosition)?.data?.preview?.images?.get(0)?.resolutions?.get(
                            2
                        )?.url?.replace(
                            "amp;s",
                            "s"
                        )?.replace("amp;", "")
                    }
                    redditData?.get(adapterPosition)?.data?.preview?.images?.get(0)?.resolutions?.size ?: 0 >= 2 -> {
                        redditData?.get(adapterPosition)?.data?.preview?.images?.get(0)?.resolutions?.get(
                            1
                        )?.url?.replace(
                            "amp;s",
                            "s"
                        )?.replace("amp;", "")
                    }
                    redditData?.get(adapterPosition)?.data?.preview?.images?.get(0)?.resolutions?.size ?: 0 >= 1 -> {
                        redditData?.get(adapterPosition)?.data?.preview?.images?.get(0)?.resolutions?.get(
                            0
                        )?.url?.replace(
                            "amp;s",
                            "s"
                        )?.replace("amp;", "")
                    }
                    else -> {
                        null
                    }
                }
            }
        }
    }

    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            with (itemView) {
                iv_image.setOnClickListener {
                    if (redditData?.get(position - 1)?.data?.isThisNews == true) {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(redditData?.get(position - 1)?.data?.url)))
                    } else {
                        val intent =
                            Intent(this@FeedAdapter.context, MediaActivity::class.java)
                        val options: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                context as FragmentActivity,
                                (iv_image as View),
                                "transition_image"
                            )
                        intent.putExtra("imageHeight", iv_image.height)
                        intent.putExtra("imageUrl", redditData?.get(position - 1)?.data?.imageUrl)
                        intent.putExtra("gif_url", redditData?.get(position - 1)?.data?.gifLink)
                        intent.putExtra("url", redditData?.get(position - 1)?.data?.url)
                        intent.putExtra(
                            "shoulduseglide",
                            redditData?.get(position - 1)?.data?.shouldUseGlideForGif
                        )
                        context.startActivity(intent, options.toBundle())
                    }
                }

                reddit_card_layout.setOnClickListener {
//                    val intent = Intent(context, CommentsActivity::class.java)
                    val intent = Intent()
                    intent.setClassName(context, "com.abhat.comment.ui.CommentsActivity")
                    intent.putExtra("title", title.text)
                    intent.putExtra("author", author.text)
                    intent.putExtra("subreddit", subreddit.text)
                    intent.putExtra("hoursAgo", created.text)
                    intent.putExtra("points", points.text)
                    intent.putExtra("comments", comments.text)
                    intent.putExtra("imageUrl", redditData?.get(position - 1)?.data?.imageUrl)
                    intent.putExtra("description", redditData?.get(position - 1)?.data?.selfTextHtml)
                    intent.putExtra("articleUrl",
                        redditData?.get(position - 1)?.data?.id
                    )
                    context.startActivity(intent)
                }
            }
        }

        fun bind(
            titleString: String?,
            authorString: String,
            pointsString: String,
            commentsString: String,
            subredditString: String,
            bodyHtmlString: String?,
            createdLong: Long,
            selfTextHtml: String?,
            over18: Boolean?,
            position: Int
        ) {
            with(itemView) {
                mainScope.launch {
                    supervisorScope {
                        launch {
                            var url: String? = null
                            try {
                                url = selectAppropriateResolution(redditData, position)
                                redditData?.get(position)?.data?.imageUrl = url
                            } catch (e: ArrayIndexOutOfBoundsException) {
                                e.printStackTrace()
                            }
                            url?.let { url ->
                                iv_image.visibility = View.VISIBLE
                                Glide.with(context)
                                    .load(url)
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .placeholder(R.color.gray_300)
                                    .into(iv_image)
                            } ?: run {
                                iv_image.visibility = View.GONE
                            }
                        }

                        launch {
                            if (over18 == true) {
                                nsfw_overlay.visibility = View.VISIBLE
                            } else {
                                nsfw_overlay.visibility = View.GONE
                            }
                        }

                        launch {
                            when {
                                isItAGifFromGfycat(redditData, position) -> {
                                    gif_indicator.visibility = View.VISIBLE
                                    gif_indicator.text = "Gfycat"
                                    gif_indicator.textSize = 12F
                                    video_indicator.visibility = View.GONE
                                }

                                isItAGifFromReddit(redditData, position) -> {
                                    gif_indicator.visibility = View.VISIBLE
                                    gif_indicator.text = "Reddit"
                                    gif_indicator.textSize = 12F
                                    video_indicator.visibility = View.GONE
                                }

                                isItGifFromOtherSource(redditData, position) -> {
                                    gif_indicator.visibility = View.VISIBLE
                                    gif_indicator.text = "GIF"
                                    gif_indicator.textSize = 18F
                                    video_indicator.visibility = View.GONE
                                }
                                isItAVideo(redditData, position) -> {
                                    gif_indicator.visibility = View.GONE
                                    video_indicator.visibility = View.VISIBLE
                                }
                                else -> {
                                    gif_indicator.visibility = View.GONE
                                    video_indicator.visibility = View.GONE
                                }
                            }
                        }

                        launch {
                            if (isItNews(redditData, position) && shouldShowNewsSourceOverlay(
                                    redditData,
                                    position
                                )
                            ) {
                                news_source_overlay.visibility = View.VISIBLE
                                val domainAndSourceUrlPair =
                                    getNewsDomainAndSourceUrl(redditData, position)
                                source_url.text = domainAndSourceUrlPair.second
                                domain.text = domainAndSourceUrlPair.first
                            } else {
                                news_source_overlay.visibility = View.GONE
                            }
                        }
                    }
                }
                loadRestOfTheUI(
                    itemView,
                    titleString,
                    pointsString,
                    subredditString,
                    bodyHtmlString,
                    commentsString,
                    authorString,
                    createdLong,
                    position
                )
            }
        }

        private fun loadRestOfTheUI(
            itemView: View, titleString: String?,
            pointsString: String,
            subredditString: String,
            bodyHtmlString: String?,
            commentsString: String,
            authorString: String,
            createdLong: Long,
            position: Int
        ) {
            with(itemView) {
                titleString?.let {
                    CoroutineScope(CoroutineContextProvider().IO).async {
                        val redditTitle = Html.fromHtml(Html.fromHtml(titleString).toString()).toString()
                        withContext(CoroutineContextProvider().Main) {
                            title.text = redditTitle
                            subreddit.text = subredditString
                        }
                    }
                } ?: run {
                    CoroutineScope(CoroutineContextProvider().IO).async {
                        val titleHtml =
                            Html.fromHtml(Html.fromHtml(bodyHtmlString).toString()).toString()
                        val subredditText =
                            redditData?.get(position)?.data?.linkId?.split("_")?.get(1)
                                ?: ""
                        withContext(CoroutineContextProvider().Main) {
                            title.text = titleHtml
                            subreddit.text = subredditText
                        }
                    }
                }

//            selfTextHtml?.let {
//                selfText.visibility = View.VISIBLE
//                selfText.text = Html.fromHtml(Html.fromHtml(selfTextHtml).toString()).toString()
//            } ?: run {
//                selfText.visibility = View.GONE
//            }
//
                val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
                    Log.d("TAG", " " + throwable.stackTrace)
                }
                author.text = authorString
                mainScope.launch(handler) {
                    supervisorScope {
                        launch {
                            points.text = getPoints(pointsString)
                        }
                        launch {
                            comments.text = getComments(commentsString)
                        }
                        launch {
                            created.text = getFormattedDate(Date(createdLong))
                        }
                    }
                }
                //created.text = dateFormatter(Date(createdLong))
            }
        }
    }


    fun dateFormatter(date: Date): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date = inputFormat.parse(inputFormat.format(date))
        val formattedDate = DateUtils.getRelativeTimeSpanString(
            date.time,
            Calendar.getInstance().timeInMillis,
            DateUtils.MINUTE_IN_MILLIS
        )
        return formattedDate.toString()
    }
}