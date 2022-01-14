package com.chooloo.www.koler.adapter

import android.net.Uri
import com.chooloo.www.chooloolib.adapter.ListAdapter
import com.chooloo.www.chooloolib.data.ListData
import com.chooloo.www.chooloolib.data.call.Call
import com.chooloo.www.chooloolib.di.activitycomponent.ActivityComponent
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import com.chooloo.www.koler.R

class CallItemsAdapter(component: ActivityComponent) : ListAdapter<Call>(component) {
    override fun onBindListItem(listItem: ListItem, item: Call) {
        listItem.apply {
            component.phones.lookupAccount(item.number) { account ->
                account?.photoUri?.let {
                    setImageUri(Uri.parse(it))
                } ?: run {
                    imageDrawable = component.drawables.getDrawable(R.drawable.round_person_20)
                }

                account?.displayString?.let {
                    titleText = it
                    captionText = item.number
                } ?: run {
                    titleText = item.number
                }
            }

//            imageSize = ViewUtils.dpToPx(context, 30).toInt()
            setImageTint(component.colors.getColor(R.color.color_opposite))

            isLeftButtonVisible = true
            setLeftButtonDrawable(R.drawable.round_call_split_24)
            setLeftButtonTintColor(R.color.orange_foreground)
            setLeftButtonBackgroundTintColor(R.color.orange_background)

            isRightButtonVisible = true
            setRightButtonDrawable(R.drawable.round_call_end_24)
            setRightButtonTintColor(R.color.red_foreground)
            setRightButtonBackgroundTintColor(R.color.red_background)
        }
    }

    override fun convertDataToListData(data: List<Call>) = ListData(data)
}