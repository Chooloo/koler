package com.chooloo.www.chooloolib.domain.repository.clipboard

interface ClipboardRepository {
    val lastCopiedText: String?

    fun copyText(text: String)
}
