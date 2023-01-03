package com.chooloo.www.chooloolib.ui.recent

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.data.model.RecentAccount
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.LiveEvent
import com.chooloo.www.chooloolib.util.getElapsedTimeString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentViewState @Inject constructor(
    private val phones: PhonesInteractor,
    private val recents: RecentsInteractor,
    private val navigations: NavigationsInteractor,
) :
    BaseViewState() {
    val name = MutableLiveData<String?>()
    val imageUri = MutableLiveData<Uri>()
    val recentId = MutableLiveData(0L)
    val typeImage = MutableLiveData<Int>()
    val caption = MutableLiveData<String>()
    val isBlocked = MutableLiveData<Boolean>()
    val recentNumber = MutableLiveData<String>()

    val isContactVisible = MutableLiveData(false)
    val isAddContactVisible = MutableLiveData(false)

    val showMoreEvent = LiveEvent()
    val callEvent = DataLiveEvent<String>()
    val confirmRecentDeleteEvent = LiveEvent()
    val showRecentEvent = DataLiveEvent<Long>()
    val showContactEvent = DataLiveEvent<Long>()

    private var _recent: RecentAccount? = null


    override fun attach() {
        super.attach()
        if (_recent == null) return
    }

    fun onRecentId(recentId: Long) {
        this.recentId.value = recentId
        viewModelScope.launch {
            recents.getRecent(recentId).collect {
                _recent = it
                recentNumber.value = it?.number
                name.value =
                    if (_recent!!.cachedName?.isNotEmpty() == true) it?.cachedName else it?.number
                it?.type?.let { typeImage.value = recents.getCallTypeImage(it) }

                caption.value = "${it?.relativeTime} • ${
                    if (it?.duration ?: -1 > 0L) "${
                        getElapsedTimeString(it?.duration!!)
                    } •" else ""
                }"
                val lookupAccount = phones.lookupAccount(it?.number)
                lookupAccount?.photoUri?.let { pu -> imageUri.value = Uri.parse(pu) }
                isContactVisible.value = lookupAccount?.name != null
                isAddContactVisible.value = lookupAccount?.name == null
            }
        }
    }

    fun onSms() {
        navigations.sendSMS(recentNumber.value)
    }

    fun onCall() {
        recentNumber.value?.let { callEvent.call(it) }
    }

    fun onAddContact() {
        recentNumber.value?.let { navigations.addContact(it) }
    }

    fun onOpenContact() {
        viewModelScope.launch {
            val account = phones.lookupAccount(recentNumber.value)
            account?.contactId?.let(navigations::viewContact)
        }
    }

    fun onRecentClick(recentId: Long) {
        showRecentEvent.call(recentId)
    }

    fun onConfirmDelete() {
        recentId.value?.let {
            recents.deleteRecent(it)
            finishEvent.call()
        }
    }

    fun onMoreClick() {
        showMoreEvent.call()
    }
}