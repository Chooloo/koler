package com.chooloo.www.kontacts.ui.quickcontact

import android.content.ContentUris
import android.content.Intent
import com.chooloo.www.chooloolib.ui.base.BaseViewState
import com.chooloo.www.chooloolib.util.MutableDataLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuickContactViewState @Inject constructor(

) : BaseViewState() {
    val showContactEvent = MutableDataLiveEvent<Long>()

    fun onIntent(intent: Intent) {
        intent.data?.let {
            showContactEvent.call(ContentUris.parseId(it))
        } ?: run {
            onFinish()
        }
    }

    fun onFragmentFinish() {
        onFinish()
    }
}