package com.setalis.amorehr.data.api.auth

import com.setalis.amorehr.data.entities.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
@Serializable
data class LoginResponse(
    var user: User? = null,
    var employee: Employee? = null,
    var token: String? = null
)

@Serializable
data class Shift(
    var id: String,
    var name: String,
    @SerialName("clock_in") var clockIn: String,
    @SerialName("clock_out") var clockOut: String,
)

@Serializable
data class Employee(
    var id: String,
    @SerialName("name") var fullname: String,
    @SerialName("telpon") var phone: String,
    var employment: Employment? = null,
    var shift: Shift? = null,
)

@Serializable
data class Employment(
    var company: Company? = null,
    var position: Position? = null
)

@Serializable
data class Company(
    var id: String,
    @SerialName("lokasi") var name: String,
    var radius: Int? = 0,
    @SerialName("lat") var latitude: Double? = 0.0,
    @SerialName("long") var longitude: Double? = 0.0
)

@Serializable
data class Position(
    var id: String,
    var name: String
)