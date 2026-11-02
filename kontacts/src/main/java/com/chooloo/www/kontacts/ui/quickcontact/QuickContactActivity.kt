package com.chooloo.www.kontacts.ui.quickcontact

import com.chooloo.www.chooloolib.ui.caller.CallerScreen
import com.chooloo.www.chooloolib.ui.common.BaseActivity
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuickContactActivity : BaseActivity() {
    override fun onSetup() {
        val contactId = intent.data?.lastPathSegment?.toLongOrNull()

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
