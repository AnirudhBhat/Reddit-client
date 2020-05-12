package com.abhat.reddit.adapter

import android.text.Html
import android.text.format.DateUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.abhat.core.PointsFormatter
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.model.Children
import com.abhat.feed.ui.FeedAdapterController
import com.abhat.feed.ui.FeedViewModel
import com.abhat.reddit.R
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
    private val feedViewModel: FeedViewModel,
    private var redditData: MutableList<Children>? = null,
    private val contextProvider: CoroutineContextProvider
) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    private var view: FeedViewHolder? = null
    private var feedAdapterController: FeedAdapterController? = null
    private val ioScope = CoroutineScope(contextProvider.IO + SupervisorJob())
    private val mainScope = CoroutineScope(contextProvider.Main + SupervisorJob())

    fun observeLiveData() {
        feedViewModel.isNsfwLiveData.observe(context!!, androidx.lifecycle.Observer { pair ->
            view?.let {
                if (pair.first) {
                    pair.second?.visibility = View.VISIBLE
                } else {
                    pair.second?.visibility = View.GONE
                }
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        view = FeedViewHolder(layoutInflater.inflate(R.layout.activity_reddit_card, parent, false))
        return view as FeedViewHolder
    }

    override fun getItemCount(): Int {
        return redditData?.size ?: 0
    }

    fun updateRedditData(redditData: MutableList<Children>?) {
        this.redditData = redditData
        notifyDataSetChanged()
    }

    fun addRedditData(redditData: List<Children>?) {
        redditData?.let { redditData ->
            this.redditData?.addAll(redditData)
            notifyItemChanged(redditData.size)
        }
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        ioScope.async {
            val title = redditData?.get(position)?.data?.title ?: ""
            val author = redditData?.get(position)?.data?.author ?: ""
            val points = redditData?.get(position)?.data?.score?.toString() ?: ""
            val comments = redditData?.get(position)?.data?.numComments?.toString() ?: ""
            val subreddit = redditData?.get(position)?.data?.subreddit ?: ""
//        val bodyHtml = redditData?.children?.get(position)?.data?.bodyHtml ?: ""
            val created = redditData?.get(position)?.data?.createdUtc?.times(1000) ?: 0
            val over18 = redditData?.get(position)?.data?.over18 ?: false
            withContext(contextProvider.Main) {
                holder.bind(
                    title, author, points, comments, subreddit, "",
                    created, "", over18
                )
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
                (!url.endsWith(".jpg")
                        && !url.endsWith(".png")
                        && !url.contains("gifv")
                        && !url.contains("gif")
                        && !url.contains("gfycat")
                        && !isVideo)
            }
        }
    }

    suspend fun shouldShowNewsSourceOverlay(redditData: MutableList<Children>?, position: Int): Boolean {
        return withContext(ioScope.coroutineContext) {
            redditData?.get(position)?.data?.preview != null
        }
    }

    suspend fun getNewsDomainAndSourceUrl(
        redditData: MutableList<Children>?,
        position: Int
    ): Pair<String, String> {
        return withContext(ioScope.coroutineContext) {
            val data = redditData?.get(position)?.data!!
            Pair(getHostNameFromUrl(data.url ?: "") ?: "", data.url ?: "")
        }
    }

    suspend fun isItAGif(redditData: MutableList<Children>?, position: Int): Boolean {
        return withContext(ioScope.coroutineContext) {
            val url = redditData?.get(position)?.data?.url ?: ""
            withContext(mainScope.coroutineContext) {
                (url.contains("gifv")
                        || url.contains("v.redd.it")
                        || url.contains("gif")
                        || url.contains("gfycat"))
            }
        }
    }

    suspend fun isItAVideo(redditData: MutableList<Children>?, position: Int): Boolean {
        return withContext(ioScope.coroutineContext) {
            (redditData?.get(position)?.data?.isVideo ?: false || redditData?.get(position)?.data?.url?.contains(
                "youtu"
            ) ?: false)
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

    suspend fun selectAppropriateResolution(redditData: MutableList<Children>?, adapterPosition: Int): String? {
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
        fun bind(
            titleString: String?,
            authorString: String,
            pointsString: String,
            commentsString: String,
            subredditString: String,
            bodyHtmlString: String?,
            createdLong: Long,
            selfTextHtml: String?,
            over18: Boolean?
        ) {
            feedViewModel.isNsfwLiveData.postValue(Pair(over18 ?: false, itemView.nsfw_overlay))

            with(itemView) {
                mainScope.launch {
                    val url = selectAppropriateResolution(redditData, adapterPosition)
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
                    when {
                        isItAGif(redditData, adapterPosition) -> {
                            gif_indicator.visibility = View.VISIBLE
                            video_indicator.visibility = View.GONE
                        }
                        isItAVideo(redditData, adapterPosition) -> {
                            gif_indicator.visibility = View.GONE
                            video_indicator.visibility = View.VISIBLE
                        }
                        else -> {
                            gif_indicator.visibility = View.GONE
                            video_indicator.visibility = View.GONE
                        }
                    }
                    if (isItNews(redditData, adapterPosition) && shouldShowNewsSourceOverlay(redditData, adapterPosition)) {
                        news_source_overlay.visibility = View.VISIBLE
                        val domainAndSourceUrlPair =
                            getNewsDomainAndSourceUrl(redditData, adapterPosition)
                        source_url.text = domainAndSourceUrlPair.second
                        domain.text = domainAndSourceUrlPair.first
                    } else {
                        news_source_overlay.visibility = View.GONE
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
                    createdLong
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
            createdLong: Long
        ) {
            with(itemView) {
                titleString?.let {
                    title.text = titleString
                    subreddit.text = subredditString
                } ?: run {
                    CoroutineScope(CoroutineContextProvider().IO).async {
                        val titleHtml =
                            Html.fromHtml(Html.fromHtml(bodyHtmlString).toString()).toString()
                        val subredditText =
                            redditData?.get(adapterPosition)?.data?.linkId?.split("_")?.get(1)
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
                author.text = authorString
                CoroutineScope(CoroutineContextProvider().Main).launch {
                    points.text = getPoints(pointsString)
                    comments.text = getComments(commentsString)
                    created.text = getFormattedDate(Date(createdLong))
                }
                //created.text = dateFormatter(Date(createdLong))

//
//            image.setOnClickListener {
//                over18?.let { over18 ->
//                    if (over18) {
//                        if (nsfw_overlay.visibility == View.VISIBLE) {
//                            nsfw_overlay.visibility = View.GONE
//                        } else {
//                            nsfw_overlay.visibility = View.VISIBLE
//                        }
//                    }
//                }
//            }
            }
        }
    }

    private fun selectAppropriateResolution(adapterPosition: Int, image: ImageView) {
//        redditData?.get(adapterPosition)?.data?.preview?.let {
////            loadImageOrGif(redditData?.get(adapterPosition)?.data?.preview?.images!![0].source.url?.replace("amp;s", "s")?.replace("amp;", ""),
////                image,
////                redditData?.get(adapterPosition)?.data?.preview?.images!![0].source.width,
////                redditData?.get(adapterPosition)?.data?.preview?.images!![0].source.height,
////                adapterPosition)
//            when {
//                redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!!.size >= 4 -> loadImageOrGif(
//                    redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!![3].url?.replace(
//                        "amp;s",
//                        "s"
//                    )?.replace("amp;", ""),
//                    image,
//                    redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!![3].width,
//                    redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!![3].height,
//                    adapterPosition
//                )
//                redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!!.size >= 3 -> loadImageOrGif(
//                    redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!![2].url?.replace(
//                        "amp;s",
//                        "s"
//                    )?.replace("amp;", ""),
//                    image,
//                    redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!![2].width,
//                    redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!![2].height,
//                    adapterPosition
//                )
//                redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!!.size >= 2 -> loadImageOrGif(
//                    redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!![1].url?.replace(
//                        "amp;s",
//                        "s"
//                    )?.replace("amp;", ""),
//                    image,
//                    redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!![1].width,
//                    redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!![1].height,
//                    adapterPosition
//                )
//                else -> {
//                    image.visibility = View.GONE
//                }
//            }
//        } ?: run {
//            image.visibility = View.GONE
//        }
    }

    private fun loadImageOrGif(
        url: String?,
        image: ImageView,
        width: Int?,
        height: Int?,
        adapterPosition: Int
    ) {
        image.visibility = View.VISIBLE
//        val requestOptions = RequestOptions()
//            .override(width ?: convertDpToPixel(640F).toInt(), height ?: convertDpToPixel(854f).toInt())
        redditData?.get(adapterPosition)?.data?.imageUrl = url
//        image.layoutParams.width = width ?: 0//convertPixelsToDp(width?.toFloat() ?: 0f).toInt()
//        image.layoutParams.height = height ?: 0//convertPixelsToDp(height?.toFloat() ?: 0f).toInt()
//        image.requestLayout()
//        Glide.with(context)
        //.asBitmap()
//            .load(url)
//            .placeholder(R.color.gray_300)
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .transition(GenericTransitionOptions.with(animationObject))
//            .into(object:  SimpleTarget<Bitmap>(convertDpToPixel(width?.toFloat() ?: 0f).toInt(),
//                convertDpToPixel(height?.toFloat() ?: 0f).toInt()
//            ) {
//                override fun onLoadCleared(placeholder: Drawable?) {
//
//                }
//
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    image.setImageBitmap(resource)
//                }
//
//            })
//            .placeholder(R.color.gray_300)
//
//            .override(width ?: 640, height ?: 854)
//            .dontAnimate()
//            .dontTransform()
//            .into(image)

        image.load(url) {
            crossfade(true)
            placeholder(R.color.gray_300)
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

    fun convertPixelsToDp(px: Float): Float {
        if (context == null) {
            return 0.0f
        }
        val resources = context.resources
        val metrics = resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertDpToPixel(dp: Float): Float {
        if (context == null) {
            return 0.0f
        }
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}