package com.chooloo.www.koler.ui.main

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.model.ContactAccount
import com.chooloo.www.chooloolib.model.RecentAccount
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.LiveEvent
import com.chooloo.www.koler.R
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class MainViewState @Inject constructor(
    private val strings: StringsInteractor,
    private val permissions: PermissionsInteractor,
    private val preferences: PreferencesInteractor,
) :
    BaseViewState() {

    val searchText = MutableLiveData<String?>()
    val searchHint = MutableLiveData<String?>()
    val currentPageIndex = MutableLiveData(0)
    val isSearching = MutableLiveData(false)

    val showMenuEvent = LiveEvent()
    val showRecentEvent = DataLiveEvent<Long>()
    val showContactEvent = DataLiveEvent<Long>()
    val showDialerEvent = DataLiveEvent<String>()


    override fun attach() {
        super.attach()
        permissions.checkDefaultDialer {}
        currentPageIndex.value = preferences.defaultPage.index
        searchHint.value = strings.getString(R.string.hint_search_items)
    }

    fun onMenuClick() {
        showMenuEvent.call()
    }

    fun onDialpadFabClick() {
        showDialerEvent.call("")
    }

    fun onViewIntent(intent: Intent) {
        try {
            val intentText = URLDecoder.decode(intent.dataString ?: "", "utf-8")
            showDialerEvent.call(intentText.substringAfter("tel:"))
        } catch (e: Exception) {
            errorEvent.call(R.string.error_couldnt_get_number_from_intent)
        }
    }

    fun onSearchFocusChange(isFocus: Boolean) {
        isSearching.value = isFocus
        searchHint.value = if (isFocus) "" else strings.getString(R.string.hint_search_items)
    }

    fun onRecentItemClick(recent: RecentAccount) {
        showRecentEvent.call(recent.id)
    }

    fun onContactItemClick(contact: ContactAccount) {
        showContactEvent.call(contact.id)
    }
}