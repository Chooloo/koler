package com.chooloo.www.chooloolib.utils


fun String.initials() =
    split(' ').mapNotNull { it.firstOrNull()?.toString() }.reduce { acc, s -> acc + s }

fun String.isRTL() = if (isEmpty()) {
    true
} else {
    val directionality = Character.getDirectionality(get(0))
    directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
            directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC
}

fun String.parseDialpadText() = replace(Regex("[^+#*0-9]"), "")