package com.chooloo.www.kontacts.ui.quickcontact

import android.content.ContentUris
import com.chooloo.www.chooloolib.ui.caller.CallerScreen
import com.chooloo.www.chooloolib.ui.common.BaseActivity
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuickContactActivity : BaseActivity() {
    override fun onSetup() {
        val contactId = intent.data?.let {
            try {
                ContentUris.parseId(it)
            } catch (_: Exception) {
                null
            }
        }

        if (contactId == null) {
            finish()
            return
        }

        setChoolooContent {
            BottomSheet(
                visible = true,
                onDismiss = { finish() }
            ) {
                CallerScreen(contactId = contactId)
            }
        }
    }
}
