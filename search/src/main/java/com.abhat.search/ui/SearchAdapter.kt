package com.abhat.search.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.abhat.feed.R
import kotlinx.android.synthetic.main.item_search.view.*

/**
 * Created by Anirudh Uppunda on 24,September,2020
 */
class SearchAdapter(
    private var searchResultsList: MutableList<SearchViewModel.SearchedSubreddits>,
    private val listener: (SearchViewModel.SearchedSubreddits) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return searchResultsList.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind()
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            with(itemView) {
                tv_search_result_text.text = searchResultsList[position].name
                iv_subreddit_icon.load(searchResultsList[position].icon)
                search_result_layout.setOnClickListener {
                    listener(searchResultsList[position])
                }
            }
        }
    }

    fun updateSearchResultList(searchResultsList: MutableList<SearchViewModel.SearchedSubreddits>) {
        this.searchResultsList = searchResultsList
        notifyDataSetChanged()
    }
}