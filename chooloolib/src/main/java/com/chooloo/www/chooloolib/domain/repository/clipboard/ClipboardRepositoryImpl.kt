package com.chooloo.www.chooloolib.domain.repository.clipboard

import android.content.ClipData
import android.content.ClipboardManager

import com.chooloo.www.chooloolib.domain.repository.base.BaseRepositoryImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClipboardRepositoryImpl @Inject constructor(
    private val clipboardManager: ClipboardManager
) : BaseRepositoryImpl(), ClipboardRepository {
    override val lastCopiedText: String?
        get() = clipboardManager.primaryClip?.getItemAt(0)?.text?.toString()

    override fun copyText(text: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("Copied number", text))
    }
}