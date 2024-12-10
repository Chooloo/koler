package com.chooloo.www.chooloolib.utils

import java.util.Locale


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

fun String.capitalize() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

fun String.beautify() = lowercase().split('_', ' ', '-').map { it.capitalize() }.joinToString(" ")