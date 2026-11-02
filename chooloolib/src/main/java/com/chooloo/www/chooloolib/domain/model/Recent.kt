package com.chooloo.www.chooloolib.domain.model

data class Recent(
    val id: Long,
    val number: String,
    val dateMillis: Long,
    val type: Type,
    val durationSeconds: Long = 0,
    val name: String? = null,
    val photoUri: String? = null,
    val numberType: Int? = null,
    val numberLabel: String? = null,
    val presentation: Presentation = Presentation.ALLOWED,
) {
    enum class Type { INCOMING, OUTGOING, MISSED, VOICEMAIL, REJECTED, BLOCKED, UNKNOWN }

    enum class Presentation { ALLOWED, RESTRICTED, PAYPHONE, UNKNOWN }
}
