package com.abhat.comment.ui

import android.text.Html
import android.text.TextUtils
import android.text.method.LinkMovementMethod
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
            parent_comment.text = if (commentsList[pos].isParentComment) "true" else "false"
            comment_index.text = commentsList[pos].childrenIndex.toString()
            if (!TextUtils.isEmpty(commentsList[pos].data?.body)) {
                tv_comment.text = Html.fromHtml((Html.fromHtml(commentsList[pos].data?.bodyHtml).toString()))
                tv_comment.movementMethod = LinkMovementMethod.getInstance()
                setMargins(cv_comment, commentsList[pos].indent, 0, 0, 0)
                tv_author.text = commentsList[pos].data?.author
                tv_points.text = commentsList[pos].data?.score.toString() + " Points"
            } else {
                tv_comment.text = "Load more comments"
                tv_comment.setTextColor(context.resources.getColor(android.R.color.holo_blue_dark))
                setMargins(cv_comment, commentsList[pos].indent, 0, 0, 0)
            }
        }
    }

    fun setMargins(v: ConstraintLayout, l: Int, t: Int, r: Int, b: Int) {
        val newLayoutParams =
            v.layoutParams as RecyclerView.LayoutParams
        newLayoutParams.topMargin = 0
        newLayoutParams.leftMargin = l
        newLayoutParams.rightMargin = 0
        v.layoutParams = newLayoutParams
        view.vertical_divider_1.visibility = View.VISIBLE


//        when (l) {
//            5 -> {
//                view.horizontal_divider.visibility = View.VISIBLE
//                view.vertical_divider_1.visibility = View.GONE
//                view.vertical_divider_2.visibility = View.GONE
//                view.vertical_divider_3.visibility = View.GONE
//                view.vertical_divider_4.visibility = View.GONE
//                view.vertical_divider_5.visibility = View.GONE
//                view.vertical_divider_6.visibility = View.GONE
//            }
//
//            28 -> {
//                view.horizontal_divider.visibility = View.GONE
//                view.vertical_divider_2.visibility = View.VISIBLE
//                view.vertical_divider_3.visibility = View.GONE
//                view.vertical_divider_4.visibility = View.GONE
//                view.vertical_divider_5.visibility = View.GONE
//                view.vertical_divider_6.visibility = View.GONE
//            }
//
//            40 -> {
//                view.horizontal_divider.visibility = View.GONE
//                view.vertical_divider_2.visibility = View.VISIBLE
//                view.vertical_divider_3.visibility = View.VISIBLE
//                view.vertical_divider_4.visibility = View.GONE
//                view.vertical_divider_5.visibility = View.GONE
//                view.vertical_divider_6.visibility = View.GONE
//            }
//
//            52 -> {
//                view.horizontal_divider.visibility = View.GONE
//                view.vertical_divider_2.visibility = View.VISIBLE
//                view.vertical_divider_3.visibility = View.VISIBLE
//                view.vertical_divider_4.visibility = View.VISIBLE
//                view.vertical_divider_5.visibility = View.GONE
//                view.vertical_divider_6.visibility = View.GONE
//            }
//
//            64 -> {
//                view.horizontal_divider.visibility = View.GONE
//                view.vertical_divider_2.visibility = View.VISIBLE
//                view.vertical_divider_3.visibility = View.VISIBLE
//                view.vertical_divider_4.visibility = View.VISIBLE
//                view.vertical_divider_5.visibility = View.VISIBLE
//                view.vertical_divider_6.visibility = View.GONE
//            }
//
//            76 -> {
//                view.horizontal_divider.visibility = View.GONE
//                view.vertical_divider_2.visibility = View.VISIBLE
//                view.vertical_divider_3.visibility = View.VISIBLE
//                view.vertical_divider_4.visibility = View.VISIBLE
//                view.vertical_divider_5.visibility = View.VISIBLE
//                view.vertical_divider_6.visibility = View.VISIBLE
//            }
//
//            else -> {
//                view.horizontal_divider.visibility = View.GONE
//                view.vertical_divider_2.visibility = View.VISIBLE
//                view.vertical_divider_3.visibility = View.VISIBLE
//                view.vertical_divider_4.visibility = View.VISIBLE
//                view.vertical_divider_5.visibility = View.VISIBLE
//                view.vertical_divider_6.visibility = View.VISIBLE
//            }
//
//        }
    }
}