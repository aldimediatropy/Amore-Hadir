package com.mediatropy.eureka.data.network

import kotlinx.serialization.Serializable

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 *
 * Wrapping contract response to handling response status
 * @param <T> the type of the response object
 */
@Serializable
sealed class AmResponse<out T : Any> {
    /**
     * response with a 2xx status code
     */
    data class Success<out T : Any>(val response: T) : AmResponse<T>()

    /**
     * response with a non-2xx status code.
     */
    data class Error(val message: String, val response: Int = 0) : AmResponse<Nothing>()
}