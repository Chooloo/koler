package com.chooloo.www.chooloolib.utils

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
import android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
import android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
import android.widget.Toast
import com.chooloo.www.chooloolib.ui.viewmodel.base.BaseViewModel

fun toast(context: Context, stringRes: Int?) {
    stringRes?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
}

fun showVMError(context: Context, error: BaseViewModel.VMError) {
    toast(context, error.strRes)
}

fun Activity.disableKeyboard() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        keyguardManager.requestDismissKeyguard(this, null)
    } else {
        @Suppress("DEPRECATION")
        window.addFlags(FLAG_DISMISS_KEYGUARD)
    }
}

fun Activity.showWhenLocked() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        setTurnScreenOn(true)
        setShowWhenLocked(true)
        keyguardManager.requestDismissKeyguard(this, null)
    } else {
        @Suppress("DEPRECATION")
        window.addFlags(FLAG_SHOW_WHEN_LOCKED or FLAG_TURN_SCREEN_ON or FLAG_DISMISS_KEYGUARD)
    }
}
