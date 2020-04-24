package com.abhat.reddit.adapter

import android.content.Context
import android.text.Html
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.abhat.core.PointsFormatter
import com.abhat.core.model.PostDetailResponse
import com.abhat.reddit.R
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Anirudh Uppunda on 22,April,2020
 */
class FeedAdapter(private val context: Context,
                  private var redditData: PostDetailResponse.Data? = null): RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        return FeedViewHolder(layoutInflater.inflate(R.layout.reddit_card_single_row, parent, false))
    }

    override fun getItemCount(): Int {
        return redditData?.children?.size ?: 0
    }

    fun updateRedditData(redditData: PostDetailResponse.Data?) {
        this.redditData = redditData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val title = redditData?.children?.get(position)?.data?.title ?: ""
        val author = redditData?.children?.get(position)?.data?.author ?: ""
        val points = redditData?.children?.get(position)?.data?.score?.toString() ?: ""
        val comments = redditData?.children?.get(position)?.data?.numComments?.toString() ?: ""
        val subreddit = redditData?.children?.get(position)?.data?.subreddit ?: ""
        val bodyHtml = redditData?.children?.get(position)?.data?.bodyHtml ?: ""
        val created = redditData?.children?.get(position)?.data?.createdUtc ?: 0 * 1000
        val selfTextHtml = redditData?.children?.get(position)?.data?.selfTextHtml ?: ""
        val over18 = redditData?.children?.get(position)?.data?.over18 ?: false

        holder.bind(title, author, points, comments, subreddit, bodyHtml,
            created, selfTextHtml, over18)
    }

    inner class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(titleString: String?, authorString: String, pointsString: String, commentsString: String, subredditString: String, bodyHtmlString: String?,
                 createdLong: Long, selfTextHtml: String?, over18: Boolean?) {
            val title = itemView.findViewById<TextView>(R.id.title)
            val author = itemView.findViewById<TextView>(R.id.author)
            val subreddit = itemView.findViewById<TextView>(R.id.subreddit)
            val points = itemView.findViewById<TextView>(R.id.points)
            val comments = itemView.findViewById<TextView>(R.id.comments)
            val redditLayout = itemView.findViewById<CardView>(R.id.redditLayout)
            val image = itemView.findViewById<ImageView>(R.id.image)
            val created = itemView.findViewById<TextView>(R.id.created)
            val selfText = itemView.findViewById<TextView>(R.id.description)
            val nsfw_overlay = itemView.findViewById<RelativeLayout>(R.id.nsfw_overlay)

            if (over18 == true) {
                nsfw_overlay.visibility = View.VISIBLE
            } else {
                nsfw_overlay.visibility = View.GONE
            }

            titleString?.let {
                title.text = titleString
                subreddit.text = subredditString
            } ?: run {
                title.text = Html.fromHtml(Html.fromHtml(bodyHtmlString).toString()).toString()
                subreddit.text =  redditData?.children?.get(adapterPosition)?.data?.linkId?.split("_")?.get(1)
                        ?: ""
            }

            selfTextHtml?.let {
                selfText.visibility = View.VISIBLE
                selfText.text = Html.fromHtml(Html.fromHtml(selfTextHtml).toString()).toString()
            } ?: run {
                selfText.visibility = View.GONE
            }

            author.text = authorString
            points.text = PointsFormatter.format(pointsString.toLong()) + " points"
            comments.text = PointsFormatter.format(commentsString.toLong())  + " comments"
            created.text = dateFormatter(Date(createdLong))
            selectAppropriateResolution(adapterPosition, image)

            image.setOnClickListener {
                over18?.let { over18 ->
                    if (over18) {
                        if (nsfw_overlay.visibility == View.VISIBLE) {
                            nsfw_overlay.visibility = View.GONE
                        } else {
                            nsfw_overlay.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun selectAppropriateResolution(adapterPosition: Int, image: ImageView) {
        redditData?.children?.get(adapterPosition)?.data?.preview?.let {
            when {
                redditData?.children?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!!.size >= 4 -> loadImageOrGif(redditData?.children?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!![3].url?.replace("amp;s", "s")?.replace("amp;", ""), image, adapterPosition)
                redditData?.children?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!!.size >= 3 -> loadImageOrGif(redditData?.children?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!![2].url?.replace("amp;s", "s")?.replace("amp;", ""), image, adapterPosition)
                redditData?.children?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!!.size >= 2 -> loadImageOrGif(redditData?.children?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!![1].url?.replace("amp;s", "s")?.replace("amp;", ""), image, adapterPosition)
                else -> {
                    image.visibility = View.GONE
                }
            }
        } ?: run {
            image.visibility = View.GONE
        }
    }

    private fun loadImageOrGif(url: String?, image: ImageView, adapterPosition: Int) {
        image.visibility = View.VISIBLE
        redditData?.children?.get(adapterPosition)?.data?.imageUrl = url
        Glide.with(context)
            .load(url)
            .placeholder(R.color.gray_600)
            .into(image)
    }

    fun dateFormatter(date: Date): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date = inputFormat.parse(inputFormat.format(date))
        val formattedDate = DateUtils.getRelativeTimeSpanString(date.time, Calendar.getInstance().timeInMillis, DateUtils.MINUTE_IN_MILLIS)
        return formattedDate.toString()
    }
}