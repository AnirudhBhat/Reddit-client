package com.abhat.feed.ui.trendingsubreddit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhat.core.SortType.SortType
import com.abhat.feed.R
import com.abhat.feed.ui.FeedFragment
import kotlinx.android.synthetic.main.item_sort_bottom_sheet.view.*
import kotlinx.android.synthetic.main.item_trending_subreddit_bottom_sheet.view.*

/**
 * Created by Anirudh Uppunda on 06,July,2020
 */
class TrendingSubredditAdapter(private var trendingSubredditsList: List<String>?,
                               private val feedFragment: FeedFragment?): RecyclerView.Adapter<TrendingSubredditAdapter.TrendingSubredditViewHolder>() {


    fun updateTrendingSubredditsList(trendingSubredditsList: List<String>) {
        this.trendingSubredditsList = trendingSubredditsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingSubredditViewHolder {
        return TrendingSubredditViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_trending_subreddit_bottom_sheet, parent, false))
    }

    override fun getItemCount() = trendingSubredditsList?.size ?: 0

    override fun onBindViewHolder(holder: TrendingSubredditViewHolder, position: Int) {
        holder.bind()
    }

    inner class TrendingSubredditViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            with (itemView) {
                tv_trending_subreddit.text = trendingSubredditsList?.get(position) ?: ""
            }
        }
    }
}