package com.setalis.amorehr.data.models

import kotlinx.serialization.Serializable

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 *
 */
@Serializable
data class Attendance(
    var id: String? = null,
    var clock_in: String? = null,
    var clock_out: String? = null,
    var date: String? = null,
    var distance_in: Double? = null,
    var distance_out: Double? = null,
    var photo_clock_in: String? = null,
    var photo_clock_out: String? = null,
    var location_clock_in: String? = null,
    var location_clock_out: String? = null,
    var shift_clock_in: String? = null,
    var shift_clock_out: String? = null
)