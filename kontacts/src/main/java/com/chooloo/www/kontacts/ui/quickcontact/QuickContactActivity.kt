package com.chooloo.www.kontacts.ui.quickcontact

import android.os.Bundle
import com.chooloo.www.chooloolib.ui.caller.CallerScreen
import com.chooloo.www.chooloolib.ui.caller.CallerTarget
import com.chooloo.www.chooloolib.ui.common.BaseActivity
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuickContactActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contactId = intent.data?.lastPathSegment?.toLongOrNull() ?: return finish()
        setChoolooContent {
            BottomSheet(onDismiss = ::finish) {
                CallerScreen(CallerTarget.ContactTarget(contactId))
            }
        }
    }
}
