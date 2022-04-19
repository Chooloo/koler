package com.chooloo.www.kontacts.ui.contact

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.interactor.contacts.ContactsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.model.ContactAccount
import com.chooloo.www.chooloolib.model.PhoneAccount
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewState @Inject constructor(
    private val phones: PhonesInteractor,
    private val contacts: ContactsInteractor,
    private val navigations: NavigationsInteractor
) : BaseViewState() {
    val callEvent = DataLiveEvent<String>()
    val contactId = MutableLiveData<Long>()
    val contactImage = MutableLiveData<Uri>()
    val contactName = MutableLiveData<String>()
    var isFavorite = MutableLiveData<Boolean>()
    val showHistoryEvent = DataLiveEvent<String>()

    private var contact: ContactAccount? = null


    private fun withFirstNumber(callback: (PhoneAccount) -> Unit) {
        contact?.let {
            phones.getContactAccounts(it.id) {
                it?.getOrNull(0)?.let(callback::invoke)
            }
        }
    }

    fun onContactId(contactId: Long) {
        contacts.queryContact(contactId) { ca ->
            contact = ca
            contactName.value = ca?.name
            isFavorite.value = ca?.starred == true
            ca?.photoUri?.let { contactImage.value = Uri.parse(it) }
        }
    }

    fun onSmsClick() {
        withFirstNumber { navigations.sendSMS(it.number) }
    }

    fun onCallClick() {
        withFirstNumber { callEvent.call(it.number) }
    }

    fun onEditClick() {
    }

    fun onHistoryClick() {
        contact?.name?.let { showHistoryEvent.call(it) }
    }

    fun onWhatsappClick() {

    }

    fun onDeleteClick() {
        contact?.let {
            contacts.deleteContact(it.id)
            finishEvent.call()
        }
    }
}