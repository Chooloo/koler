package com.chooloo.www.chooloolib.utils

import android.content.Context
import android.widget.Toast
import com.chooloo.www.chooloolib.ui.common.BaseViewModel

fun toast(context: Context, stringRes: Int?) {
    stringRes?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
}

fun showVMError(context: Context, error: BaseViewModel.VMError) {
    toast(context, error.strRes)
}
