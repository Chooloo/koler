package com.chooloo.www.chooloolib.ui.recent

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.interactor.blocked.BlockedInteractor
import com.chooloo.www.chooloolib.interactor.drawable.DrawablesInteractor
import com.chooloo.www.chooloolib.interactor.navigation.NavigationsInteractor
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.phoneaccounts.PhonesInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.recents.RecentsInteractor
import com.chooloo.www.chooloolib.model.RecentAccount
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.LiveEvent
import com.chooloo.www.chooloolib.util.getElapsedTimeString
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecentViewState @Inject constructor(
    private val phones: PhonesInteractor,
    private val recents: RecentsInteractor,
    private val blocked: BlockedInteractor,
    private val drawables: DrawablesInteractor,
    private val preferences: PreferencesInteractor,
    private val navigations: NavigationsInteractor,
    private val permissions: PermissionsInteractor
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
        recents.observeRecent(recentId) {
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
            phones.lookupAccount(it?.number) {
                it?.photoUri?.let { imageUri.value = Uri.parse(it) }
                isContactVisible.value = it?.name != null
                isAddContactVisible.value = it?.name == null
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
        phones.lookupAccount(recentNumber.value) { account ->
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