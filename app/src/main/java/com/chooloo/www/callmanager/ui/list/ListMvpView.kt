package com.chooloo.www.callmanager.ui.list

import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.callmanager.ui.base.MvpView

interface ListMvpView : MvpView {
    val itemCount: Int

    fun showEmptyPage(isShow: Boolean)
    fun showNoPermissions(isShow: Boolean)
    fun animateListView()
    fun setOnScrollListener(onScrollListener: RecyclerView.OnScrollListener)
}