package com.chooloo.www.chooloolib.adapter

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Phone
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.data.model.ListData
import com.chooloo.www.chooloolib.data.model.RecentAccount
import com.chooloo.www.chooloolib.di.module.IoScope
import com.chooloo.www.chooloolib.di.module.MainScope
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.drawable.DrawablesInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.ui.widgets.listitemholder.ListItemHolder
import com.chooloo.www.chooloolib.util.getHoursString
import com.chooloo.www.chooloolib.util.initials
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecentsAdapter @Inject constructor(
    animations: AnimationsInteractor,
    private val phones: PhonesInteractor,
    private val recents: RecentsInteractor,
    private val drawables: DrawablesInteractor,
    @IoScope private val ioScope: CoroutineScope,
    @MainScope private val mainScope: CoroutineScope,
    @ApplicationContext private val context: Context
) : ListAdapter<RecentAccount>(animations) {

    private var _groupSimilar: Boolean = false

    var groupSimilar: Boolean
        get() = _groupSimilar
        set(value) {
            _groupSimilar = value
        }


    override fun onBindListItem(listItemHolder: ListItemHolder, item: RecentAccount) {
        listItemHolder.apply {
            val date = context.getHoursString(item.date)

            captionText = if (item.groupCount > 1) "(${item.groupCount}) $date ·" else "$date ·"

            ioScope.launch {
                val account = phones.lookupAccount(item.number)

                mainScope.launch {
                    titleText = account?.name ?: item.cachedName ?: item.number
                    setImageUri(account?.photoUri?.let(Uri::parse))
                    account?.let {
                        captionText =
                            "$captionText ${
                                Phone.getTypeLabel(
                                    context.resources,
                                    it.type,
                                    it.label
                                )
                            } ·"
                        imageInitials = it.name?.initials()
                        if (it.name == null || it.name.isEmpty()) {
                            drawables.getDrawable(R.drawable.person)?.let {
                                setImageDrawable(it)
                            }
                        }
                    }
                }
            }

            setCaptionImageRes(recents.getCallTypeImage(item.type))
        }
    }

    override fun convertDataToListData(items: List<RecentAccount>) =
        ListData.fromRecents(items, groupSimilar)
}