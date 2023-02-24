package com.chooloo.www.chooloolib.util


fun String.initials() =
    split(' ').mapNotNull { it.firstOrNull()?.toString() }.reduce { acc, s -> acc + s }

fun String.isRTL() = if (length < 1) {
    true
} else {
    val directionality = Character.getDirectionality(get(0))
    directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
            directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC
}