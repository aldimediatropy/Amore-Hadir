package com.setalis.amorehr.utils.networks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
@Serializable
data class AmError(
    @SerialName("details")
    val details: List<Detail>? = null
)

@Serializable
data class Detail(
    @SerialName("message")
    val message: String? = null
)