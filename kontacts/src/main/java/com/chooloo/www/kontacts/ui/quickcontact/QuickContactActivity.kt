package com.chooloo.www.kontacts.ui.quickcontact

import android.Manifest
import android.os.Bundle
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.ui.caller.CallerScreen
import com.chooloo.www.chooloolib.ui.caller.CallerTarget
import com.chooloo.www.chooloolib.ui.common.BaseActivity
import com.chooloo.www.chooloolib.ui.components.BottomSheet
import com.chooloo.www.chooloolib.ui.components.Permissioned
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuickContactActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val link = intent.data?.toString() ?: return finish()
        setChoolooContent {
            BottomSheet(visible = true, onDismiss = ::finish) {
                Permissioned(
                    permissions = listOf(Manifest.permission.READ_CONTACTS),
                    rationaleRes = R.string.permission_rationale_contacts,
                    optionalPermissions = listOf(Manifest.permission.WRITE_CONTACTS),
                ) {
                    CallerScreen(target = CallerTarget.LinkTarget(link), onDismiss = ::finish)
                }
            }
        }
    }
}
