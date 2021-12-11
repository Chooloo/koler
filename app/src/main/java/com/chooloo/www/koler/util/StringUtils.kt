package com.chooloo.www.koler.util


fun String.initials() =
    split(' ').mapNotNull { it.firstOrNull()?.toString() }.reduce { acc, s -> acc + s }