package com.setalis.amorehr.utils.networks.model

import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
@Serializable
data class StatusCode(
    val code: Int,
    val debug: String?,
    @SerialName("msg") val message: String?
)

class StatusCodeResponseException(response: HttpResponse, text: String) :
    ResponseException(response, text) {
    override val message: String = text
}