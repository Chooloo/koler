package com.chooloo.www.koler.ui.main

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chooloo.www.chooloolib.interactor.permission.PermissionsInteractor
import com.chooloo.www.chooloolib.interactor.preferences.PreferencesInteractor
import com.chooloo.www.chooloolib.interactor.string.StringsInteractor
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.util.DataLiveEvent
import com.chooloo.www.chooloolib.util.LiveEvent
import com.chooloo.www.chooloolib.util.MutableDataLiveEvent
import com.chooloo.www.chooloolib.util.MutableLiveEvent
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

    private val _searchText = MutableLiveData<String?>()
    private val _searchHint = MutableLiveData<String?>()
    private val _currentPageIndex = MutableLiveData(0)
    private val _isSearching = MutableLiveData(false)
    private val _showMenuEvent = MutableLiveEvent()
    private val _showDialerEvent = MutableDataLiveEvent<String>()

    val searchText = _searchText as LiveData<String?>
    val searchHint = _searchHint as LiveData<String?>
    val currentPageIndex = _currentPageIndex as LiveData<Int>
    val isSearching = _isSearching as LiveData<Boolean>
    val showMenuEvent = _showMenuEvent as LiveEvent
    val showDialerEvent = _showDialerEvent as DataLiveEvent<String>


    override fun attach() {
        super.attach()
        permissions.checkDefaultDialer {}
        _currentPageIndex.value = preferences.defaultPage.index
        _searchHint.value = strings.getString(R.string.hint_search_items)
    }

    fun onMenuClick() {
        _showMenuEvent.call()
    }

    fun onDialpadFabClick() {
        _showDialerEvent.call("")
    }

    fun onViewIntent(intent: Intent) {
        try {
            val intentText = URLDecoder.decode(intent.dataString ?: "", "utf-8")
            _showDialerEvent.call(intentText.substringAfter("tel:").trim())
        } catch (e: Exception) {
            onError(R.string.error_couldnt_get_number_from_intent)
        }
    }

    fun onSearchFocusChange(isFocus: Boolean) {
        _isSearching.value = isFocus
        _searchHint.value = if (isFocus) "" else strings.getString(R.string.hint_search_items)
    }

    fun onPageSelected(pageIndex: Int) {
        _currentPageIndex.value = pageIndex
    }
}