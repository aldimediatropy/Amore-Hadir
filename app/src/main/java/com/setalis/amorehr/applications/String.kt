package com.setalis.amorehr

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.setalis.components.extensions.emptyString
import com.setalis.components.extensions.getValueOrEmpty
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


/**
 * Crafted by Al (ismealdi) on 21/03/24.
 * Setalis Digital
 */

fun String.replaceApi() = this.replace(BuildConfig.server.replace("https://", emptyString()), "**")

fun String?.toArrayListFromJson(): List<String> {
    if(this.isNullOrEmpty()) return emptyList()
    val gson = GsonBuilder().create()

    return try {
        gson.fromJson<ArrayList<String>>(this, object :
            TypeToken<ArrayList<String>>(){}.type)
    } catch (e: Exception) {
        emptyList()
    }
}

fun calculateDuration(startTime: String, endTime: String): String {
    if(startTime.contains(":") && endTime.contains(":")) {
        // Split the start and end time strings by ":"
        val startParts = startTime.split(":")
        val endParts = endTime.split(":")

        // Extract hours and minutes from the split strings
        val startHours = startParts[0].toInt()
        val startMinutes = startParts[1].toInt()
        val endHours = endParts[0].toInt()
        val endMinutes = endParts[1].toInt()

        // Calculate the duration in minutes
        val totalStartMinutes = startHours * 60 + startMinutes
        val totalEndMinutes = endHours * 60 + endMinutes
        val durationInMinutes = totalEndMinutes - totalStartMinutes

        // Convert duration to hours and minutes
        val hours = durationInMinutes / 60
        val minutes = durationInMinutes % 60

        // Return the formatted duration string
        return "$hours h : $minutes m"
    }else{
        return ""
    }
}

fun distance(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {
    val radius = 6371 // Earth radius in kilometers

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return radius * c
}

fun convertTimeFormat(inputTime: String): String {
    // Split the input time string by ":"
    val parts = inputTime.split(":")

    // Get the hours and minutes parts
    val hours = parts[0]
    val minutes = parts[1]

    // Return the formatted time string
    return "$hours:$minutes"
}

fun now(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy", Locale.ENGLISH)
    val formattedDate = currentDateTime.format(formatter)

    return formattedDate ?: ""
}


fun isToday(inputDateString: String): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .withZone(ZoneId.systemDefault())

    val instant = Instant.parse(inputDateString)
    val formattedInstant = formatter.format(instant)

    val currentDate = LocalDate.now()

    return formattedInstant == currentDate.format(formatter)
}


fun compareTimes(time1: String?, time2: String?): Boolean {
    if(time1.getValueOrEmpty().contains(":") && time2.getValueOrEmpty().contains(":")) {
        // Define the time format
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        // Parse the time strings to LocalTime
        val localTime1 = LocalTime.parse(time1, formatter)
        val localTime2 = LocalTime.parse(time2, formatter)

        // Compare the LocalTime objects
        return localTime1.isAfter(localTime2)
    }

    return false
}