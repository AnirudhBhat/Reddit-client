package com.abhat.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhat.core.common.CoroutineContextProvider
import com.abhat.feed.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_search_layout.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by Anirudh Uppunda on 24,September,2020
 */
class SearchBottomSheetFragment(private val contextProvider: CoroutineContextProvider): BottomSheetDialogFragment() {

    private val ioScope = CoroutineScope(contextProvider.IO + SupervisorJob())
    private val mainScope = CoroutineScope(contextProvider.Main + SupervisorJob())

    private val searchViewModel: SearchViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppRoundedCornerBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_layout, container, false)
        return view
    }


    private fun setupRecyclerView() {
        rv_search?.layoutManager = LinearLayoutManager(activity)
        rv_search?.adapter = SearchAdapter(mutableListOf()) {
            (activity as? SearchActivity)?.onSubredditSelect(it)
        }
    }

    private fun observeViewModel() {
        searchViewModel.getUIState().observe(viewLifecycleOwner, Observer {
            (rv_search.adapter as? SearchAdapter)?.updateSearchResultList(it.success?.toMutableList() ?: mutableListOf())
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                ioScope.launch {
                    supervisorScope {
                        searchViewModel.onAction(Action.Search(s.toString()))
//                        searchViewModel.search(s.toString())
                    }
                }
            }
        })
    }
}