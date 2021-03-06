package com.abhat.feed.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhat.core.SortType.SortType
import com.abhat.feed.R
import kotlinx.android.synthetic.main.item_sort_bottom_sheet.view.*

/**
 * Created by Anirudh Uppunda on 06,July,2020
 */
class SortAdapter(private val sortList: List<SortType>,
                  private val feedFragment: FeedFragment?,
                  private val subreddit: String,
                  private val sortBottomSheet: SortBottomSheet): RecyclerView.Adapter<SortAdapter.SortViewHolder>() {


    private var after: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortViewHolder {
        return SortViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sort_bottom_sheet, parent, false))
    }

    override fun getItemCount() = sortList.size

    override fun onBindViewHolder(holder: SortViewHolder, position: Int) {
        holder.bind()
    }

    inner class SortViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            with (itemView) {
                tv_sort_by.text = sortList[adapterPosition].name
                subreddit_layout.setOnClickListener {
                    feedFragment?.showProgressBar()
                    when (tv_sort_by.text) {
                        "hot" -> {
                            feedFragment?.getFeed(subreddit, after, SortType.hot)
                        }

                        "new" -> {
                            feedFragment?.getFeed(subreddit, after, SortType.new)
                        }

                        "rising" -> {
                            feedFragment?.getFeed(subreddit, after, SortType.rising)
                        }

                        "best" -> {
                            feedFragment?.getFeed(subreddit, after, SortType.best)
                        }
                    }
                    sortBottomSheet.dismiss()
                }
            }
        }
    }
}