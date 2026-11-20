package com.chooloo.www.chooloolib.ui.dialpad

data class DialpadInput(
    val text: String = "",
    val selectionStart: Int = text.length,
    val selectionEnd: Int = selectionStart,
) {
    private val selectionMin get() = minOf(selectionStart, selectionEnd)
    private val selectionMax get() = maxOf(selectionStart, selectionEnd)

    fun replaceSelection(insert: String) = replace(selectionMin, selectionMax, insert)

    fun deleteBackward(): DialpadInput = when {
        selectionMin != selectionMax -> replace(selectionMin, selectionMax, "")
        selectionMin == 0 -> this
        else -> replace(selectionMin - 1, selectionMin, "")
    }

    private fun replace(start: Int, end: Int, insert: String) =
        DialpadInput(text.replaceRange(start, end, insert), start + insert.length)
}
