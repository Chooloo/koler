package com.chooloo.www.chooloolib.ui.briefcontact

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.contacts.ContactsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.model.ContactAccount
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BriefContactViewState @Inject constructor(
    private val phones: PhonesInteractor,
    private val contacts: ContactsInteractor,
    private val navigations: NavigationsInteractor,
    private val permissions: PermissionsInteractor
) : BaseViewState() {
    val contactId = MutableLiveData<Long>()
    val contactImage = MutableLiveData<Uri>()
    val isFavorite = MutableLiveData<Boolean>()
    val contactName = MutableLiveData<String?>()
    val contactNumber = MutableLiveData<String?>()

    val showMoreEvent = LiveEvent()
    val callEvent = DataLiveEvent<String>()

    private var firstNumber: String? = null
    private var contact: ContactAccount? = null


    private fun withFirstNumber(callback: (String?) -> Unit) {
        if (firstNumber != null) {
            callback.invoke(firstNumber)
        } else {
            contact?.let {
                phones.getContactAccounts(it.id) { phones ->
                    firstNumber = phones?.getOrNull(0)?.number
                    callback.invoke(firstNumber)
                }
            }
        }
    }

    fun onContactId(contactId: Long) {
        this.contactId.value = contactId
        contacts.observeContact(contactId) {
            contact = it
            contactName.value = it?.name
            isFavorite.value = it?.starred == true
            withFirstNumber { contactNumber.value = it }
            it?.photoUri?.let { uri ->
                contactImage.value = Uri.parse(uri)
            }
        }
    }

    fun onCallClick() {
        withFirstNumber {
            it?.let(callEvent::call) ?: run {
                errorEvent.call(R.string.error_no_number_to_call)
            }
        }
    }

    fun onSmsClick() {
        withFirstNumber { it?.let(navigations::sendSMS) }
    }

    fun onEditClick() {
        contactId.value?.let(navigations::editContact)
    }

    fun onDelete() {
        permissions.runWithWriteContactsPermissions {
            contactId.value?.let(contacts::deleteContact)
            finishEvent.call()
        }
    }

    fun onMoreClick() {
        showMoreEvent.call()
    }
}