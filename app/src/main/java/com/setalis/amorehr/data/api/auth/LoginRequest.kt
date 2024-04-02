package com.setalis.amorehr.data.api.auth

import kotlinx.serialization.Serializable

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
@Serializable
data class LoginRequest(
    val email: String? = null,
    var password: String? = null
)