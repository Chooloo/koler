package com.chooloo.www.koler.adapter

import android.graphics.Color
import android.view.MenuItem
import com.chooloo.www.koler.R
import com.chooloo.www.koler.di.activitycomponent.ActivityComponent
import com.chooloo.www.koler.ui.list.ListData
import com.chooloo.www.koler.ui.widgets.listitem.ListItem
import com.google.android.material.internal.ViewUtils

class MenuAdapter(component: ActivityComponent) : ListAdapter<MenuItem>(component) {
    override fun onBindListItem(listItem: ListItem, item: MenuItem) {
        listItem.apply {
            setBackgroundColor(Color.TRANSPARENT)
            setTitleTextAppearance(R.style.Koler_Text_Subtitle1)
            setImageTint(component.colors.getColor(R.color.color_opposite))
            setTitleColor(component.colors.getColor(R.color.color_opposite))

            paddingTop = 28
            paddingBottom = 28
            imageDrawable = item.icon
            titleText = item.title.toString()
            imageSize = ViewUtils.dpToPx(context, 30).toInt()
        }
    }

    override fun convertDataToListData(data: List<MenuItem>) = ListData(data)
}