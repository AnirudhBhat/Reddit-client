package com.abhat.reddit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhat.reddit.R
import com.abhat.reddit.SortType.SortType
import kotlinx.android.synthetic.main.item_sort_bottom_sheet.view.*

/**
 * Created by Anirudh Uppunda on 06,July,2020
 */
class SortAdapter(private val sortList: List<SortType>): RecyclerView.Adapter<SortAdapter.SortViewHolder>() {

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
            }
        }
    }
}