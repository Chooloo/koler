package com.chooloo.www.chooloolib.util


fun String.initials() =
    split(' ').mapNotNull { it.firstOrNull()?.toString() }.reduce { acc, s -> acc + s }