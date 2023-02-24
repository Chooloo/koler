package com.chooloo.www.chooloolib.ui.recent

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chooloo.www.chooloolib.data.model.RecentAccount
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.util.*
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
    private val _name = MutableLiveData<String?>()
    private val _imageUri = MutableLiveData<Uri>()
    private val _recentId = MutableLiveData(0L)
    private val _typeImage = MutableLiveData<Int>()
    private val _caption = MutableLiveData<String>()
    private val _isBlocked = MutableLiveData<Boolean>()
    private val _recentNumber = MutableLiveData<String>()
    private val _isContactVisible = MutableLiveData(false)
    private val _isAddContactVisible = MutableLiveData(false)
    private val _showMoreEvent = MutableLiveEvent()
    private val _callEvent = MutableDataLiveEvent<String>()
    private val _showRecentEvent = MutableDataLiveEvent<Long>()
    private val _showContactEvent = MutableDataLiveEvent<Long>()

    val name = _name as LiveData<String?>
    val caption = _caption as LiveData<String>
    val imageUri = _imageUri as LiveData<Uri>
    val recentId = _recentId as LiveData<Long>
    val typeImage = _typeImage as LiveData<Int>
    val isBlocked = _isBlocked as LiveData<Boolean>
    val recentNumber = _recentNumber as LiveData<String>
    val isContactVisible = _isContactVisible as LiveData<Boolean>
    val isAddContactVisible = _isAddContactVisible as LiveData<Boolean>
    val showMoreEvent = _showMoreEvent as LiveEvent
    val callEvent = _callEvent as DataLiveEvent<String>
    val showRecentEvent = _showRecentEvent as DataLiveEvent<Long>
    val showContactEvent = _showContactEvent as DataLiveEvent<Long>

    private var _recent: RecentAccount? = null


    override fun attach() {
        super.attach()
        if (_recent == null) return
    }

    fun onRecentId(recentId: Long) {
        _recentId.value = recentId
        viewModelScope.launch {
            recents.getRecent(recentId).collect {
                _recent = it
                it?.number?.let { _recentNumber.value = it }
                _name.value =
                    if (_recent?.cachedName?.isNotEmpty() == true) it?.cachedName else it?.number
                it?.type?.let { _typeImage.value = recents.getCallTypeImage(it) }

                _caption.value = "${it?.relativeTime ?: ""} • ${
                    if ((it?.duration ?: -1) > 0L) "${
                        getElapsedTimeString(it?.duration!!)
                    } •" else ""
                }"

                phones.lookupAccount(it?.number).also { phoneLookupAccount ->
                    phoneLookupAccount?.photoUri?.let { pu -> _imageUri.value = Uri.parse(pu) }
                    _isContactVisible.value = phoneLookupAccount?.name != null
                    _isAddContactVisible.value = phoneLookupAccount?.name == null
                }
            }
        }
    }

    fun onSms() {
        navigations.sendSMS(recentNumber.value)
    }

    fun onCall() {
        recentNumber.value?.let(_callEvent::call)
    }

    fun onAddContact() {
        recentNumber.value?.let(navigations::addContact)
    }

    fun onOpenContact() {
        viewModelScope.launch {
            val account = phones.lookupAccount(recentNumber.value)
            account?.contactId?.let(navigations::viewContact)
        }
    }

    fun onRecentClick(recentId: Long) {
        _showRecentEvent.call(recentId)
    }

    fun onMoreClick() {
        _showMoreEvent.call()
    }
}