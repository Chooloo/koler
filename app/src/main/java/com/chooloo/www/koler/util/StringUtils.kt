package com.chooloo.www.koler.util


fun String.withoutLastChars(chars: Int) = substring(0, length - chars)

fun String.withoutLastChar() = withoutLastChars(1)

fun String.initials() =
    split(' ').mapNotNull { it.firstOrNull()?.toString() }.reduce { acc, s -> acc + s }