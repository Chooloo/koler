package com.chooloo.www.koler.adapter

import android.provider.ContactsContract
import android.view.ViewGroup
import com.chooloo.www.koler.R
import com.chooloo.www.koler.data.account.PhoneAccount
import com.chooloo.www.koler.di.boundcomponent.BoundComponentRoot
import com.chooloo.www.koler.ui.widgets.listitem.ListItem
import com.chooloo.www.koler.ui.widgets.listitem.ListItemButton
import com.chooloo.www.koler.ui.widgets.listitem.ListItemHolder

class PhonesAdapter(boundComponent: BoundComponentRoot) :
    ListAdapter<PhoneAccount>(boundComponent) {

    private val _blockedDrawable by lazy {
        boundComponent.drawableInteractor.getDrawable(R.drawable.round_block_black_24)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListItemHolder(ListItemButton(parent.context))


    override fun onBindListItem(listItem: ListItem, item: PhoneAccount) {
        listItem.apply {
            isPadded = false
            imageVisibility = false
            titleText = item.number
            captionText = boundComponent.stringInteractor.getString(
                ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(item.type)
            )
            setTitleBold(true)

            boundComponent.permissionInteractor.runWithDefaultDialer(R.string.error_not_default_dialer_blocked) {
                if (boundComponent.numbersInteractor.isNumberBlocked(item.number)) {
                    imageDrawable = _blockedDrawable
                }
            }
        }
    }
}