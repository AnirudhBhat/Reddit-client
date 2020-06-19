package com.abhat.comment.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.abhat.comment.R
import com.abhat.core.model.Children
import kotlinx.android.synthetic.main.item_card.view.*

/**
 * Created by Anirudh Uppunda on 03,June,2020
 */
class CommentsAdapter(private val cardData: CardData,
                      private var commentsList: List<Children>,
                      private val imageUrl: String?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            RedditCardViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_card, parent, false))
        } else {
            CommentsViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_comments_single_row, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return commentsList.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            (holder as RedditCardViewHolder).bind(cardData)
        } else {
            (holder as CommentsViewHolder).bind(commentsList, position - 1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            0
        } else {
            position
        }
    }

    fun updateCommentsList(commentsList: MutableList<Children>) {
        this.commentsList = commentsList
    }

    inner class RedditCardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(cardData: CardData) {
            with (itemView) {
                title.text = cardData.title
                author.text = cardData.author
                subreddit.text = cardData.subreddit
                created.text = cardData.timeHoursAgo
                points.text = cardData.points + " Points"
                comments.text = cardData.comments + " Comments"
                imageUrl?.let { imageUrl ->
                    iv_image.load(imageUrl)
                } ?: run {
                    iv_image.visibility = View.GONE
                }
            }
        }
    }
}