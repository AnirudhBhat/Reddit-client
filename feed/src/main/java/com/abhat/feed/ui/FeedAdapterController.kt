package com.abhat.feed.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.abhat.core.PointsFormatter
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.model.Children
import com.abhat.feed.ui.state.FeedAdapterViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * Created by Anirudh Uppunda on 10,May,2020
 */
class FeedAdapterController(private var redditData: MutableList<Children>? = null) {

    private val feedAdapterViewState = MutableLiveData<FeedAdapterViewState>()

    fun getFeedAdapterViewState() = feedAdapterViewState as LiveData<FeedAdapterViewState>

    private var currentFeedAdapterViewState = FeedAdapterViewState()
        set(value) {
            field = value
            feedAdapterViewState.postValue(value)
        }

    fun selectAppropriateResolution(adapterPosition: Int) {
        redditData?.get(adapterPosition)?.data?.preview?.let {
            when {
                redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!!.size >= 4 -> {
                    currentFeedAdapterViewState = currentFeedAdapterViewState.copy(
                        url = redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions?.get(3)?.url?.replace(
                            "amp;s",
                            "s"
                        )?.replace("amp;", "")
                    )
                }
                redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!!.size >= 3 -> {
                    currentFeedAdapterViewState = currentFeedAdapterViewState.copy(
                        url = redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions?.get(2)?.url?.replace(
                            "amp;s",
                            "s"
                        )?.replace("amp;", "")
                    )
                }
                redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions!!.size >= 2 -> {
                    currentFeedAdapterViewState = currentFeedAdapterViewState.copy(
                        url = redditData?.get(adapterPosition)?.data?.preview?.images!![0].resolutions?.get(1)?.url?.replace(
                            "amp;s",
                            "s"
                        )?.replace("amp;", "")
                    )
                }
            }
        }
    }

    fun setPointsAndComments(pointsString: String, commentsString: String, points: TextView, comments: TextView) {
        CoroutineScope(CoroutineContextProvider().IO).async {
            val pointsString = PointsFormatter.format(pointsString.toLong())
            val commentsString = PointsFormatter.format(commentsString.toLong())
            withContext(CoroutineContextProvider().Main) {
//                points.text = "$pointsString points"
//                comments.text = "$commentsString comments"
                currentFeedAdapterViewState = currentFeedAdapterViewState.copy(
                    points = pointsString,
                    comments =  commentsString,
                    pointsTextView = points,
                    commentsTextView =  comments
                )
            }
        }
    }

}