package com.chooloo.www.chooloolib.util


object PhoneNumberUtils {
    fun isMmi(number: String) =
        (number.startsWith("**04") || number.startsWith("**05")) && number.endsWith("#")

    fun isUri(number: String?) =
        number != null && (number.contains("@") || number.contains("%40"))

    fun isSecretCode(number: String) =
        number.length > 8 && number.startsWith("*#*#") && number.endsWith("#*#*")
}