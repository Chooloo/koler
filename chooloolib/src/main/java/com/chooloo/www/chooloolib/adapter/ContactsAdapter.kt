package com.chooloo.www.chooloolib.adapter

import android.net.Uri
import com.chooloo.www.chooloolib.data.model.ContactAccount
import com.chooloo.www.chooloolib.data.model.ListData
import com.chooloo.www.chooloolib.di.module.IoScope
import com.chooloo.www.chooloolib.di.module.MainScope
import com.chooloo.www.chooloolib.interactor.animation.AnimationsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.ui.widgets.listitemholder.ListItemHolder
import com.chooloo.www.chooloolib.util.initials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


open class ContactsAdapter @Inject constructor(
    animations: AnimationsInteractor,
    private val phones: PhonesInteractor,
    @IoScope private val ioScope: CoroutineScope,
    @MainScope private val mainScope: CoroutineScope
) : ListAdapter<ContactAccount>(animations) {
    private var _withFavs: Boolean = true
    private var _withHeaders: Boolean = true

    var withFavs: Boolean
        get() = _withFavs
        set(value) {
            _withFavs = value
        }

    var withHeaders: Boolean
        get() = _withHeaders
        set(value) {
            _withHeaders = value
        }


    override fun onBindListItem(
        listItemHolder: ListItemHolder,
        item: ContactAccount,
        position: Int
    ) {
        listItemHolder.apply {
            titleText = item.name
            imageInitials = item.name?.initials()

            ioScope.launch {
                val number = phones.getContactAccounts(item.id).firstOrNull()?.number
                mainScope.launch {
                    captionText = number
                }
            }

            setImageUri(if (item.photoUri != null) Uri.parse(item.photoUri) else null)
        }
    }

    override fun convertDataToListData(items: List<ContactAccount>) =
        if (_withHeaders) ListData.fromContacts(items, _withFavs) else ListData(items)
}