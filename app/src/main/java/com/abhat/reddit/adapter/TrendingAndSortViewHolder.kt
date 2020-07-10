package com.abhat.reddit.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.abhat.core.SortType.SortType
import com.abhat.reddit.FeedFragment
import kotlinx.android.synthetic.main.item_trending_and_sort.view.*

/**
 * Created by Anirudh Uppunda on 19,June,2020
 */
class TrendingAndSortViewHolder(
    view: View,
    fragment: FeedFragment?
) : RecyclerView.ViewHolder(view) {

    var sortType: SortType = SortType.hot
    init {
        with(view) {
            sort.setOnClickListener {
                fragment?.openSortBottomSheet()
            }

//            fragment?.getSortTypeLiveData()?.observe(fragment, Observer {
//                sort.text = it.name.capitalize()
//            })
        }
    }

    fun bind() {
        with (itemView) {
            sort.text = sortType.name
        }
    }
}