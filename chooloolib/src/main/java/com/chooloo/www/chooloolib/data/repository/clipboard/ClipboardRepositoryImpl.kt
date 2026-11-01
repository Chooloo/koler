package com.chooloo.www.chooloolib.data.repository.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import com.chooloo.www.chooloolib.R
import com.chooloo.www.chooloolib.domain.repository.clipboard.ClipboardRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClipboardRepositoryImpl @Inject constructor(
    private val clipboardManager: ClipboardManager,
    @ApplicationContext private val context: Context
) : ClipboardRepository {
    override val lastCopiedText: String?
        get() = clipboardManager.primaryClip?.getItemAt(0)?.text?.toString()

    override fun copyText(text: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("Copied number", text))
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            Toast.makeText(context, R.string.number_copied_to_clipboard, Toast.LENGTH_SHORT).show()
        }
    }
}
