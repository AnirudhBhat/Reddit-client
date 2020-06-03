package com.abhat.comment.ui

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.abhat.core.model.Children
import kotlinx.android.synthetic.main.item_comments_single_row.view.*


/**
 * Created by Anirudh Uppunda on 03,June,2020
 */
class CommentsViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    fun bind(commentsList: List<Children>, pos: Int) {
        with (view) {
            tv_comment.text = commentsList[pos].data?.body
            setMargins(cv_comment, commentsList[pos].indent, 0, 0, 0)
            tv_author.text = commentsList[pos].data?.author
            tv_points.text = commentsList[pos].data?.score.toString() + " Points"
        }
    }

    fun setMargins(v: ConstraintLayout, l: Int, t: Int, r: Int, b: Int) {
        val newLayoutParams =
            v.layoutParams as RecyclerView.LayoutParams
        newLayoutParams.topMargin = 0
        newLayoutParams.leftMargin = l
        newLayoutParams.rightMargin = 0
        v.layoutParams = newLayoutParams
    }
}