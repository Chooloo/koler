package com.chooloo.www.callmanager.ui.list

import com.chooloo.www.callmanager.ui.base.MvpView

interface ListMvpView : MvpView {

    val itemCount: Int

    fun showEmptyPage(isShow: Boolean)
    fun showNoPermissions(isShow: Boolean)
    fun animateListView()
}