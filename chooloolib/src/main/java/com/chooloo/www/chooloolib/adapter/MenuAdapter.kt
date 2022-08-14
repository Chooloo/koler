package com.chooloo.www.chooloolib.adapter

import android.annotation.SuppressLint
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import com.chooloo.www.chooloolib.databinding.ListItemBinding
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.model.ListData
import com.chooloo.www.chooloolib.ui.widgets.listitemholder.ListItemHolder
import com.chooloo.www.chooloolib.ui.widgets.listitemholder.MenuItemHolder
import javax.inject.Inject

@SuppressLint("RestrictedApi")
class MenuAdapter @Inject constructor(
    animationsInteractor: AnimationsInteractor
) : ListAdapter<MenuItem>(animationsInteractor) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MenuItemHolder(
        ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindListItem(listItemHolder: ListItemHolder, item: MenuItem) {
        listItemHolder.apply {
            isClickable = item.isEnabled
            titleText = item.title.toString()
            if (SDK_INT >= VERSION_CODES.O) {
                captionText = item.contentDescription?.toString()
            }

            setImageDrawable(item.icon)
        }
    }

    override fun convertDataToListData(items: List<MenuItem>) = ListData(items)
}