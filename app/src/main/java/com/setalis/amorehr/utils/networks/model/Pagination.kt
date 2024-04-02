package com.setalis.amorehr.utils.networks.model

import com.setalis.components.extensions.getValueOrZero
import kotlinx.serialization.Serializable

/**
 * Crafted by Al (ismealdi) on 09/04/23.
 * Mediatropy
 */
@Serializable
data class Pagination(
    val skip: Int? = 0,
    val take: Int? = 0,
    val total: Int? = 109
) {
    fun hasLoadMore() = ((total.getValueOrZero() - (skip.getValueOrZero() + take.getValueOrZero())) > 0)
}