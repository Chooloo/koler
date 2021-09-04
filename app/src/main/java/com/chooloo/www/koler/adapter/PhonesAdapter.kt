package com.chooloo.www.koler.adapter

import androidx.recyclerview.widget.DiffUtil
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.account.PhoneAccount
import com.chooloo.www.koler.di.boundcomponent.BoundComponentRoot
import com.chooloo.www.koler.ui.widgets.ListItem

class PhonesAdapter(boundComponent: BoundComponentRoot) :
    ListAdapter<PhoneAccount>(boundComponent, DIFF_CALLBACK) {

    private val _blockedDrawable by lazy {
        boundComponent.drawableInteractor.getDrawable(R.drawable.round_block_black_24)
    }


    override fun onBindListItem(listItem: ListItem, item: PhoneAccount) {
        listItem.apply {
            titleText = item.number
            imageVisibility = false

            boundComponent.permissionInteractor.runWithDefaultDialer(R.string.error_not_default_dialer_blocked) {
                if (boundComponent.numbersInteractor.isNumberBlocked(item.number)) {
                    imageDrawable = _blockedDrawable
                }
            }
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PhoneAccount>() {
            override fun areItemsTheSame(oldItem: PhoneAccount, newItem: PhoneAccount) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: PhoneAccount, newItem: PhoneAccount) =
                oldItem == newItem
        }
    }
}