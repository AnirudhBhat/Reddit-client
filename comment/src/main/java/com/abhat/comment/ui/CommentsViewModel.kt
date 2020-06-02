package com.abhat.comment.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhat.comment.data.CommentsRepository
import com.abhat.core.common.CoroutineContextProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

class CommentsViewModel(private val commentRepository: CommentsRepository,
                        private val contextProvider: CoroutineContextProvider
) : ViewModel() {

    private val viewState: MutableLiveData<PostDetailState> = MutableLiveData()
    fun getViewState() = viewState as LiveData<PostDetailState>

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
                        reducer(PostDetailState.Loading(isLoading = false))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    reducer(PostDetailState.Failure(throwable = e.cause))
                    reducer(PostDetailState.Loading(isLoading = false))
                }
            }
        }
    }

    fun onAction(action: Action) {
        when (action) {
            is Action.LoadPostDetails -> {
                loadPostDetails(action.subreddit, action.articleUrl)
                //reducer(loadPostDetails(action.subreddit, action.articleUrl))
            }
        }
    }

    private fun reducer(postDetailState: PostDetailState) {
        when (postDetailState) {
            is PostDetailState.Loading -> {
                currentUIState = currentUIState.copy(isLoading = postDetailState.isLoading, success = null, error = null)
            }

            is PostDetailState.Success -> {
                currentUIState = currentUIState.copy(isLoading = false, success = postDetailState.response, error = null)
            }

            is PostDetailState.Failure -> {
                currentUIState = currentUIState.copy(isLoading = false, success = null, error = postDetailState.throwable)
            }
        }
    }
}
