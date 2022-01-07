package com.chooloo.www.koler.adapter

import com.chooloo.www.koler.R
import com.chooloo.www.koler.di.activitycomponent.ActivityComponent
import com.chooloo.www.koler.ui.list.ListData
import com.chooloo.www.koler.ui.widgets.listitem.ListItem
import com.google.android.material.internal.ViewUtils

class ChoicesAdapter(component: ActivityComponent) : ListAdapter<String>(component) {
    override fun onBindListItem(listItem: ListItem, item: String) {
        listItem.apply {
            setTitleBold(true)
            setImageTint(component.colors.getColor(R.color.color_opposite))

            titleText = item
            imageSize = ViewUtils.dpToPx(context, 30).toInt()
            imageDrawable = component.drawables.getDrawable(R.drawable.round_fiber_manual_record_20)
        }
    }

    override fun convertDataToListData(data: List<String>) = ListData(data)
}