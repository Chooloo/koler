package com.chooloo.www.kontacts.ui.activity

import android.content.ContentUris
import com.chooloo.www.chooloolib.ui.activity.BaseActivity
import com.chooloo.www.chooloolib.ui.compose.BottomSheet
import com.chooloo.www.chooloolib.ui.view.CallerView
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
                CallerView(contactId = contactId)
            }
        }
    }
}
