package com.setalis.amorehr.utils.networks

import kotlinx.serialization.Serializable

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 *
 * Common class used by API response.
 * @param <T> the type of the response object
 */
@Serializable
data class AmContract<out T>(
    val data: T,
    var valid: Boolean? = false,
    var message: String? = null,
)

