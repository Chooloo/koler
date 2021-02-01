package com.chooloo.www.callmanager.ui.cursor

import android.database.Cursor
import androidx.recyclerview.widget.RecyclerView
import com.chooloo.www.callmanager.ui.base.MvpView

interface CursorMvpView : MvpView {
    val itemCount: Int

    fun updateData(cursor: Cursor)
    fun showEmptyPage(isShow: Boolean)
    fun showNoPermissions(isShow: Boolean)
    fun animateListView()
    fun setOnScrollListener(onScrollListener: RecyclerView.OnScrollListener)
}