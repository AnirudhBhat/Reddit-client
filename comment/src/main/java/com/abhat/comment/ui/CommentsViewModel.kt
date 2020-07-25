package com.abhat.comment.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhat.comment.data.CommentsRepository
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.core.model.Children
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

class CommentsViewModel(private val commentRepository: CommentsRepository,
                        private val contextProvider: CoroutineContextProvider
) : ViewModel() {

    private val uiState: MutableLiveData<UIState> = MutableLiveData()
    fun getUIState() = uiState as LiveData<UIState>

    private var currentUIState = UIState()
        set(value) {
            field = value
            uiState.postValue(value)
        }

    private fun loadPostDetails(subreddit: String, articleUrl: String) {
        reducer(PostDetailState.Loading(isLoading = true))
        viewModelScope.launch(contextProvider.Main) {
            supervisorScope {
                try {
                    val response =
                        withContext(viewModelScope.coroutineContext + contextProvider.IO) {
                            commentRepository.loadPostDetails(subreddit, articleUrl)
                        }
                    response?.let { redditResponseList ->
                        reducer(PostDetailState.Success(response = redditResponseList[1]))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    reducer(PostDetailState.Failure(throwable = e.cause))
                }
            }
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.LoadPostDetails -> {
                loadPostDetails(action.subreddit, action.articleUrl)
            }
        }
    }

    private fun reducer(postDetailState: PostDetailState) {
        currentUIState = when (postDetailState) {
            is PostDetailState.Loading -> {
                currentUIState.copy(isLoading = postDetailState.isLoading, success = null, error = null)
            }

            is PostDetailState.Success -> {
                currentUIState.copy(isLoading = false, success = postDetailState.response, error = null)
            }

            is PostDetailState.Failure -> {
                currentUIState.copy(isLoading = false, success = null, error = postDetailState.throwable)
            }
        }
    }

    fun getNextParentCommentPosition(previousParentCommentPosition: Int, list: List<Children>): Int? {
        list.forEachIndexed {index,  children ->
            if (index > previousParentCommentPosition && children.isParentComment) {
                return index
            }
        }
        return null
    }
}
