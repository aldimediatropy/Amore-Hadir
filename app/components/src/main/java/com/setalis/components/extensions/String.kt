package com.setalis.components.extensions

import android.util.Patterns
import java.text.NumberFormat

fun String?.getValueOrEmpty(): String {
    return when (this) {
        null -> emptyString()
        else -> this
    }
}

fun String?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun Int?.getValueOrZero(): Int {
    return when (this) {
        null -> 0
        else -> this
    }
}

fun Int?.toNumberFormat() : String {
    return NumberFormat.getNumberInstance(locale()).format(this)
}