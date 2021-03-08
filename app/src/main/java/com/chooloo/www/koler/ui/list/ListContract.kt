package com.chooloo.www.koler.ui.list

import com.chooloo.www.koler.ui.base.BaseContract

interface ListContract : BaseContract {
    interface View : BaseContract.View {
        val itemCount: Int

        fun showEmptyPage(isShow: Boolean)
        fun showPermissionsPage(isShow: Boolean)
        fun animateListView()
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onNoPermissions()
        fun onResults()
        fun onNoResults()
        fun onEnablePermissionClick()
    }
}