package com.chooloo.www.chooloolib.ui.list

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.chooloo.www.chooloolib.adapter.ListAdapter
import com.chooloo.www.chooloolib.ui.base.BaseContract

interface ListContract : BaseContract {
    interface View<ItemType> : BaseContract.View {
        var isScrollerVisible: Boolean

        fun showEmpty(isShow: Boolean)
        fun showLoading(isLoading: Boolean)
        fun setEmptyIcon(@DrawableRes res: Int?)
        fun setEmptyReason(@StringRes res: Int?)
        fun setAdapter(adapter: ListAdapter<ItemType>)
    }

    interface Controller<ItemType, V : View<ItemType>> : BaseContract.Controller<V> {
        fun applyFilter(filter: String)
        fun onItemClick(item: ItemType) {}
        fun onItemLongClick(item: ItemType) {}
        fun setOnItemClickListener(onItemClickListener: (ItemType) -> Unit)
        fun setOnItemLongClickListener(onItemLongClickListener: (ItemType) -> Unit)
        fun setOnItemsChangedListener(onItemsChangedListener: (List<ItemType>) -> Unit? = {})
    }
}