package com.chooloo.www.koler.ui.list

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.chooloo.www.koler.adapter.ListAdapter
import com.chooloo.www.koler.ui.base.BaseContract

interface ListContract : BaseContract {
    interface View<ItemType> : BaseContract.View {
        val searchHint: String?

        fun scrollToTop()
        fun requestSearchFocus()
        fun setupScrollIndicator()
        fun showItem(item: ItemType)
        fun showEmpty(isShow: Boolean)
        fun showLoading(isLoading: Boolean)
        fun setEmptyIcon(@DrawableRes res: Int?)
        fun setEmptyReason(@StringRes res: Int?)
        fun setAdapter(adapter: ListAdapter<ItemType>)
    }

    interface Controller<ItemType, V : View<ItemType>> : BaseContract.Controller<V> {
        fun onItemClick(item: ItemType) {}
        fun onItemLongClick(item: ItemType) {}
        fun onSearchTextChanged(text: String) {}
        fun setOnItemsChangedListener(onItemsChangedListener: (List<ItemType>) -> Unit? = {})
    }
}