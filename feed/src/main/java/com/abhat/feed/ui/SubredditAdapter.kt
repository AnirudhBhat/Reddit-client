package com.abhat.feed.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhat.core.SortType.SortType
import com.abhat.feed.R
import kotlinx.android.synthetic.main.item_sort_bottom_sheet.view.*
import kotlinx.android.synthetic.main.item_subreddit_bottom_sheet.view.*

/**
 * Created by Anirudh Uppunda on 06,July,2020
 */
class SubredditAdapter(
    private var subredditList: List<String>?,
    private val feedFragment: FeedFragment?,
    private val sortType: SortType?,
    private val subredditBottomSheetFragment: SubredditBottomSheetFragment
) : RecyclerView.Adapter<SubredditAdapter.SubredditViewHolder>() {


    private var after: String = ""

    fun updateSubredditList(subredditList: List<String>?) {
        this.subredditList = subredditList?.sortedBy {
            it.toLowerCase()
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubredditViewHolder {
        return SubredditViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_subreddit_bottom_sheet, parent, false)
        )
    }

    override fun getItemCount() = subredditList?.size ?: 0

    override fun onBindViewHolder(holder: SubredditViewHolder, position: Int) {
        holder.bind()
    }

    inner class SubredditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            with (itemView) {
                tv_subreddit.setOnClickListener {
                    feedFragment?.showProgressBar()
                    val subreddit = tv_subreddit.text.toString()
                    feedFragment?.getFeed(subreddit, after, sortType ?: SortType.hot)
                    subredditBottomSheetFragment.dismiss()
                }
            }
        }
        fun bind() {
            with(itemView) {
                tv_subreddit.text = subredditList?.get(adapterPosition) ?: ""
            }
        }
    }
}