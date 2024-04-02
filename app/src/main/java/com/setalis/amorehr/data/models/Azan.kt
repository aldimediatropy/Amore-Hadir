package com.setalis.amorehr.data.models

import kotlinx.serialization.Serializable

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 *
 */
@Serializable
data class Azan(
    var name: String? = null,
    var time: String? = null,
    var ringed: Boolean = false,
    var countdown: Boolean = false,
    var tomorrow: Boolean = false
)
