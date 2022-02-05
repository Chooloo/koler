package com.chooloo.www.chooloolib.adapter

import android.graphics.Color
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.chooloolib.data.ListData
import com.chooloo.www.chooloolib.data.account.RecentAccount
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.ui.widgets.listitem.ListItem
import com.chooloo.www.chooloolib.util.getHoursString
import javax.inject.Inject

class RecentsAdapter @Inject constructor(
    animationsInteractor: AnimationsInteractor,
    private val preferencesInteractor: PreferencesInteractor,
    private val phonesInteractor: PhonesInteractor,
    private val stringsInteractor: StringsInteractor,
    private val recentsInteractor: RecentsInteractor
) : ListAdapter<RecentAccount>(animationsInteractor) {

    override fun onBindListItem(listItem: ListItem, item: RecentAccount) {
        listItem.apply {
            isCompact = preferencesInteractor.isCompact
            captionText = if (item.date != null) context.getHoursString(item.date) else null
            phonesInteractor.lookupAccount(item.number) {
                titleText = it?.name ?: item.number
                it?.let {
                    captionText = "$captionText · ${
                        stringsInteractor.getString(Phone.getTypeLabelResource(it.type))
                    }"
                }
            }

            setImageBackgroundColor(Color.TRANSPARENT)
            setImageResource(recentsInteractor.getCallTypeImage(item.type))
        }
    }

    override fun convertDataToListData(data: List<RecentAccount>) = ListData.fromRecents(data)
}