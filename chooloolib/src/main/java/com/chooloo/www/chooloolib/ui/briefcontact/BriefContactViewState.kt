package com.chooloo.www.chooloolib.ui.briefcontact

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.interactor.contacts.ContactsInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.model.ContactAccount
import com.chooloo.www.chooloolib.model.PhoneAccount
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
    val contactName = MutableLiveData<String?>()
    val contact = MutableLiveData<ContactAccount>()
    val isStarIconVisible = MutableLiveData<Boolean>()
    val isStarIconActivated = MutableLiveData<Boolean>()

    val callEvent = DataLiveEvent<String>()
    val confirmContactDeleteEvent = LiveEvent()

    private var _firstPhone: PhoneAccount? = null


    fun onContactId(contactId: Long) {
        this.contactId.value = contactId
        contacts.queryContact(contactId) {
            contactName.value = it?.name
            it?.photoUri?.let { uri -> contactImage.value = Uri.parse(uri) }
            isStarIconActivated.value = it?.starred == true
        }
        phones.getContactAccounts(contactId) {
            _firstPhone = it?.getOrNull(0)
        }
    }

    fun onActionCall() {
        _firstPhone?.number?.let(callEvent::call) ?: run {
            errorEvent.call(R.string.error_no_number_to_call)
        }
    }

    fun onActionSms() {
        _firstPhone?.number?.let(navigations::sendSMS)
    }

    fun onActionEdit() {
        contactId.value?.let(navigations::editContact)
    }

    fun onActionDelete() {
        confirmContactDeleteEvent.call()
    }

    fun onDeleteConfirmed() {
        permissions.runWithWriteContactsPermissions {
            contactId.value?.let(contacts::deleteContact)
            finishEvent.call()
        }
    }

    fun onActionStar(isActivate: Boolean) {
        contactId.value?.let { contacts.toggleContactFavorite(it, !isActivate) }
        isStarIconActivated.value = !isActivate
    }
}