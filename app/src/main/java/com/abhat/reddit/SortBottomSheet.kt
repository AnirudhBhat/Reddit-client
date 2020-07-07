package com.abhat.reddit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhat.reddit.SortType.SortType
import com.abhat.reddit.adapter.SortAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_sort_bottomsheet_layout.*

/**
 * Created by Anirudh Uppunda on 06,July,2020
 */
class SortBottomSheet: BottomSheetDialogFragment() {

    var sortTypeList = listOf(
        SortType.Best,
        SortType.Hot,
        SortType.New,
        SortType.Rising
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sort_bottomsheet_layout, container, false)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    fun setupRecyclerView() {
        rv_sort.layoutManager = LinearLayoutManager(activity)
        rv_sort.adapter = SortAdapter(sortTypeList)
    }

}