package com.chooloo.www.chooloolib.adapter

import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.data.model.ListData
import com.chooloo.www.chooloolib.data.model.RawContactAccount
import com.chooloo.www.chooloolib.ui.widgets.listitemholder.ListItemHolder
import javax.inject.Inject

class AccountsAdapter @Inject constructor(
    animations: AnimationsInteractor,
    private val strings: StringsInteractor
) : ListAdapter<RawContactAccount>(animations) {
    override fun onBindListItem(
        listItemHolder: ListItemHolder,
        item: RawContactAccount,
        position: Int
    ) {
        listItemHolder.apply {
            captionText = null
            isImageVisible = false
            isRightButtonVisible = true
            titleText = strings.getString(item.type.titleStringRes)

            if (item.type == RawContactAccount.RawContactType.WHATSAPP) {
                setRightButtonIcon(R.drawable.whatsapp)
            }
        }
    }

    override fun convertDataToListData(items: List<RawContactAccount>) =
        ListData.fromRawContacts(items, accounts = true, withHeader = false)
}