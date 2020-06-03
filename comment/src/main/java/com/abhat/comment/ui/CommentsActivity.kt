package com.abhat.comment.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.abhat.comment.R
import com.abhat.core.model.Children
import com.abhat.core.model.ChildrenData
import com.abhat.core.model.RedditResponse
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.item_card.*
import org.koin.android.ext.android.inject

/**
 * Created by Anirudh Uppunda on 03,June,2020
 */
class CommentsActivity: AppCompatActivity() {
    private val commentsViewModel: CommentsViewModel by inject()
    private var commentsAdapter: CommentsAdapter? = null
    private val list = mutableListOf<Children>()
    private var indent = 0

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
        setupRecyclerView(
            CardData(
                title,
                author,
                subreddit,
                timeHoursAgo,
                points,
                comments
        ), imageUrl
        )
        observeViewModel()
        commentsViewModel.onAction(Action.LoadPostDetails(subreddit, articleUrl))
    }

    private fun setupRecyclerView(cardData: CardData, imageUrl: String) {
        rv_comments.layoutManager = LinearLayoutManager(this)
        commentsAdapter = CommentsAdapter(cardData, listOf(), imageUrl)
        rv_comments.adapter = commentsAdapter
    }

    private fun observeViewModel() {
        commentsViewModel.getUIState().observe(this, Observer { uiState ->
            if (uiState.isLoading) {

            } else {

            }
            uiState.success?.data?.children?.let { children ->
                printRedditComments(children)
                commentsAdapter?.updateCommentsList(list)
                commentsAdapter?.notifyItemChanged(1)
//                rv_comments?.scheduleLayoutAnimation()
            }
            Log.d("TAG", "COMMENT: " + uiState?.success?.data?.children?.get(1)?.data?.body)
        })
    }

    private fun printRedditComments(children: MutableList<Children>) {
        for (children in children) {
            list.add(children)
            Log.d("TAG", "COMMENT: " + children.data.body)
            if (children.data.replies != null) {
                indent = getIndent(children.data.depth, indent)
                children.indent = indent
                printRedditComments((children.data.replies as RedditResponse).data.children)
            } else {
                indent = getIndent(children.data.depth, indent)
                children.indent = indent
            }
        }
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