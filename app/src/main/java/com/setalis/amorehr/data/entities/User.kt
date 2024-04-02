package com.setalis.amorehr.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Crafted by Al (ismealdi)
 * Setalis Digital
 */
@Parcelize
@Entity(tableName = "users")
@Serializable
data class User(
    @PrimaryKey var id: String,
    var email: String? = null,
    var name: String? = null,
    var telpon: String? = null,
    @SerialName("role_id") var role: Int? = null,
    var position: String? = null,
    var company: String? = null,
    var shift: String? = null,
    var clock_in: String? = null,
    var clock_out: String? = null,
    @SerialName("lat") var latitude: Double? = 0.0,
    @SerialName("long") var longitude: Double? = 0.0,
    var radius: Int? = 0,
) : Parcelable {}