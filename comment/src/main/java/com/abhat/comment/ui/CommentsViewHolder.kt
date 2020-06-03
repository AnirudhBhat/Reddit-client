package com.abhat.comment.ui

import android.view.View
import android.view.ViewGroup
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
        }
    }

    fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = v.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
    }
}