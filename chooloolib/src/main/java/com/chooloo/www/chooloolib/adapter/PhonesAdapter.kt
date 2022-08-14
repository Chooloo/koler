package com.chooloo.www.chooloolib.adapter

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.model.ListData
import com.chooloo.www.chooloolib.model.PhoneAccount
import com.chooloo.www.chooloolib.ui.widgets.listitemholder.ListItemHolder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PhonesAdapter @Inject constructor(
    animationsInteractor: AnimationsInteractor,
    @ApplicationContext private val context: Context
) : ListAdapter<PhoneAccount>(animationsInteractor) {
    override fun onBindListItem(listItemHolder: ListItemHolder, item: PhoneAccount) {
        listItemHolder.apply {
            isImageVisible = false
            titleText = item.number
            isRightButtonVisible = true
            isRightButtonEnabled = true
            captionText = Phone.getTypeLabel(context.resources, item.type, item.label).toString()

            setRightButtonIcon(R.drawable.call)
        }
    }

    override fun convertDataToListData(data: List<PhoneAccount>) =
        ListData.fromPhones(data, withHeader = false)
}