package com.setalis.amorehr.data.api.attend

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
@Serializable
data class AttendRequest(
    var latitude: Double? = null,
    var longitude: Double? = null,
    @SerialName("photo_b64") var image: String? = null
)