package com.chooloo.www.koler.adapter

import android.net.Uri
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.App
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.util.call.CallItem
import com.chooloo.www.koler.util.call.CallItem.Companion.CallState.*

class CallsAdapter : ListAdapter<CallItem>() {
    fun updateCallItem(callItem: CallItem) {
        data.items.forEachIndexed { index, item ->
            if (item.isTheSameCall(callItem)) {
                if (callItem.isDisconnected) {
                    removeItem(item)
                    return
                } else {
                    updateItem(callItem, index)
                    return
                }
            }
        }
        addItem(callItem)
    }

    override fun onBindListItem(listItem: ListItem, item: CallItem) {
        val context = listItem.context
        val account = item.getAccount(context)

        listItem.apply {
            leftButtonVisibility = !item.isConnected
            rightButtonVisibility = !item.isConnected
            titleText = account.name ?: account.number ?: "Unknown"
            captionText = App.resources?.getString(item.state.stringRes)

            setImageUri(Uri.parse(account.photoUri))
            setLeftButtonTintColor(R.color.green_foreground)
            setRightButtonDrawable(R.drawable.round_swap_calls_24)
            setOnLeftButtonClickListener { item.mergeConference() }
            setOnRightButtonClickListener { item.swapConference() }
            setLeftButtonDrawable(R.drawable.ic_call_merge_black_24dp)

            when (item.state) {
                HOLDING -> {
                    setCaptionTextColor(ContextCompat.getColor(context, R.color.red_foreground))
                }
                ACTIVE -> {
                    setCaptionTextColor(ContextCompat.getColor(context, R.color.green_foreground))
                }
                DISCONNECTED -> {
                    setCaptionTextColor(ContextCompat.getColor(context, R.color.red_foreground))
                }
                else -> {
                }
            }
        }
    }
}