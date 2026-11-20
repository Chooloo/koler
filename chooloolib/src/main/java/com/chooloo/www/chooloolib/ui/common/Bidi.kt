package com.chooloo.www.chooloolib.ui.common

private const val LEFT_TO_RIGHT_ISOLATE = '\u2066'
private const val POP_DIRECTIONAL_ISOLATE = '\u2069'

fun String.forceLtr(): String = "$LEFT_TO_RIGHT_ISOLATE$this$POP_DIRECTIONAL_ISOLATE"
