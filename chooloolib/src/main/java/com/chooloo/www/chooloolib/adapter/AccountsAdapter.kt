package com.chooloo.www.chooloolib.adapter

import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.model.ListData
import com.chooloo.www.chooloolib.model.RawContactAccount
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import javax.inject.Inject

class AccountsAdapter @Inject constructor(
    animations: AnimationsInteractor,
    private val strings: StringsInteractor
) : ListAdapter<RawContactAccount>(animations) {
    override fun onBindListItem(listItem: ListItem, item: RawContactAccount) {
        listItem.apply {
            background = null
            captionText = null
            imageVisibility = false
            isRightButtonVisible = true
            titleText = strings.getString(item.type.titleStringRes)

            setTitleBold(true)
            if (item.type == RawContactAccount.RawContactType.WHATSAPP) {
                setRightButtonDrawable(R.drawable.round_whatsapp_20)
                setRightButtonTintColor(R.color.green_foreground)
                setRightButtonBackgroundTintColor(R.color.green_background)
            }
        }
    }

    override fun convertDataToListData(data: List<RawContactAccount>) =
        ListData.fromRawContacts(data, accounts = true)
}