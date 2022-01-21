package com.chooloo.www.chooloolib.adapter

import android.annotation.SuppressLint
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.ListData
import com.chooloo.www.chooloolib.di.activitycomponent.ActivityComponent
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import com.google.android.material.internal.ViewUtils

class ChoicesAdapter(component: ActivityComponent) : ListAdapter<String>(component) {
    @SuppressLint("RestrictedApi")
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