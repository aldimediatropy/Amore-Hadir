package com.setalis.components.extensions

import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.motion.widget.MotionLayout
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.GregorianCalendar
import java.util.Locale

fun emptyString() = ""
fun spaceString() = " "
fun slashString() = "/"

fun locale() : Locale {
    return AppCompatDelegate.getApplicationLocales().get(0) ?: Locale.getDefault()
}

fun gmtDiff(): Double {
    // Get the default system timezone
    val zoneId = ZoneId.systemDefault()

    // Get the current offset from UTC in seconds
    val zoneOffsetSeconds = zoneId.rules.getOffset(java.time.Instant.now()).totalSeconds

    // Convert the offset from seconds to hours

    return (zoneOffsetSeconds.toFloat() / (60 * 60)).toDouble()
}

fun stringToLocalTimeToMillis(timeString: String, format: String): Long {
    val formatter = DateTimeFormatter.ofPattern(format)
    return localTimeToMillis(LocalTime.parse(timeString, formatter))
}

fun localTimeToMillis(time: LocalTime): Long {
    val currentDate = LocalDateTime.now()
    val combinedDateTime = currentDate.with(time)

    return combinedDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
}

fun LocalDate.toGregorianCalendar(): GregorianCalendar {
    val localDateTime = this.atStartOfDay()
    val zoneId = java.time.ZoneId.systemDefault()
    return GregorianCalendar.from(localDateTime.atZone(zoneId))
}

fun View.motionVisibility(visibility: Int) {
    val motionLayout = parent as MotionLayout
    motionLayout.constraintSetIds.forEach {
        val constraintSet = motionLayout.getConstraintSet(it) ?: return@forEach
        constraintSet.setVisibility(this.id, visibility)
        constraintSet.applyTo(motionLayout)
    }
}