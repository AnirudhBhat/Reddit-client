package com.abhat.comment.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.abhat.comment.R
import com.abhat.core.model.Children
import com.abhat.core.model.RedditResponse
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.item_comments_single_row.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * Created by Anirudh Uppunda on 03,June,2020
 */
class CommentsActivity: AppCompatActivity() {
    private val commentsViewModel: CommentsViewModel by viewModel()
    private var commentsAdapter: CommentsAdapter? = null
    private val list = mutableListOf<Children>()
    private var indent = 0
    private var previousParentCommentPosition = 1
    private var childrenIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")
        val subreddit = intent.getStringExtra("subreddit")
        val timeHoursAgo = intent.getStringExtra("hoursAgo")
        val points = intent.getStringExtra("points")
        val comments = intent.getStringExtra("comments")
        val articleUrl = intent.getStringExtra("articleUrl")
        val imageUrl = intent.getStringExtra("imageUrl")
        val smoothScroller: SmoothScroller = object : LinearSmoothScroller(this) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        fab_next_parent_comment.setOnClickListener {
            previousParentCommentPosition = commentsViewModel.getNextParentCommentPosition(previousParentCommentPosition + 1, list) ?: 0
            smoothScroller.targetPosition = previousParentCommentPosition
            (rv_comments.layoutManager as LinearLayoutManager).startSmoothScroll(smoothScroller)
        }
        setupRecyclerView(
            CardData(
                title,
                author,
                subreddit,
                timeHoursAgo,
                points,
                comments
        ), imageUrl)
        observeViewModel()
        commentsViewModel.getUIState().value?.let {
            bindUI(it)
        } ?: run {
            commentsViewModel.onAction(Action.LoadPostDetails(subreddit, articleUrl))
        }
    }

    private fun setupRecyclerView(cardData: CardData, imageUrl: String?) {
        rv_comments.layoutManager = LinearLayoutManager(this)
        commentsAdapter = CommentsAdapter(cardData, listOf(), imageUrl)
        rv_comments.adapter = commentsAdapter
        commentsAdapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        rv_comments.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val firstVisibleItemPosition = (rv_comments.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if ((recyclerView.findViewHolderForLayoutPosition(firstVisibleItemPosition) as? CommentsViewHolder)?.itemView?.parent_comment?.text == "true") {
                    previousParentCommentPosition =  (recyclerView.findViewHolderForLayoutPosition(firstVisibleItemPosition) as? CommentsViewHolder)?.itemView?.comment_index?.text.toString().toInt()
                }
            }
        })
    }

    private fun observeViewModel() {
        commentsViewModel.getUIState().observe(this, Observer { uiState ->
            bindUI(uiState)
        })
    }

    private fun bindUI(uiState: UIState) {
        if (uiState.isLoading) {
            pb_comments_activity.visibility = View.VISIBLE
        } else {

        }
        uiState.success?.data?.children?.let { children ->
            pb_comments_activity.visibility = View.GONE
            fab_next_parent_comment?.visibility = View.VISIBLE
            printRedditComments(children)
            commentsAdapter?.updateCommentsList(list)
            commentsAdapter?.notifyItemChanged(1)
//                rv_comments?.scheduleLayoutAnimation()
        }
    }

    private fun printRedditComments(children: MutableList<Children>) {
        children.forEachIndexed { index, children ->
            childrenIndex++
            list.add(children)
            Log.d("TAG", "COMMENT: " + children.data.body)
            if (children.data.replies != null) {
                indent = getIndent(children.data.depth, indent)
                if (indent == 5) {
                    children.isParentComment = true
                    children.childrenIndex = childrenIndex
                }
                children.indent = indent
                (children?.data?.replies as? RedditResponse)?.data?.children?.let {
                    printRedditComments(it)
                }
            } else {
                indent = getIndent(children.data.depth, indent)
                if (indent == 5) {
                    children.isParentComment = true
                    children.childrenIndex = childrenIndex
                }
                children.indent = indent
            }
        }

//        for (children in children) {
//            //if (!TextUtils.isEmpty(children.data.body)) {
//
//            //}
//        }
    }

    private fun getIndent(depth: Int, indent: Int): Int {
        when (depth) {
            0 -> return 5

            1 -> return 24 + 4

            2 -> return 36 + 4

            3 -> return 48 + 4

            4 -> return 60 + 4

            5 -> return 72 + 4

            6 -> return 84 + 4

            else -> return indent + 12
        }
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }
}