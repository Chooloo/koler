package com.chooloo.www.chooloolib.adapter

import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.model.ListData
import com.chooloo.www.chooloolib.model.PhoneAccount
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import javax.inject.Inject

class PhonesAdapter @Inject constructor(
    animations: AnimationsInteractor
) : ListAdapter<PhoneAccount>(animations) {
    override fun onBindListItem(listItem: ListItem, item: PhoneAccount) {
        listItem.apply {
            background = null
            imageVisibility = false
            titleText = item.number
            isLeftButtonVisible = true
            isRightButtonVisible = true
            captionText = Phone.getTypeLabel(resources, item.type, item.label).toString()

            setTitleBold(true)
            setRightButtonDrawable(R.drawable.round_call_20)
            setLeftButtonTintColor(R.color.green_foreground)
            setLeftButtonDrawable(R.drawable.round_whatsapp_20)
            setLeftButtonBackgroundTintColor(R.color.green_background)
        }
    }

    override fun convertDataToListData(data: List<PhoneAccount>) = ListData.fromPhones(data)
}