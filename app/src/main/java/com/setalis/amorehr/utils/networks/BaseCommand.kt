package com.setalis.amorehr.utils.networks

import com.mediatropy.eureka.data.network.AmResponse
import com.setalis.amorehr.utils.commons.Constants
import com.setalis.amorehr.utils.commons.Constants.RESPONSE_TYPE.UNKNOWN_HOST
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.UnknownHostException

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
open class BaseCommand : KoinComponent {

    protected val network by inject<Network>()

    protected suspend fun <E> makeCall(call: suspend () -> HttpResponse,
                block: suspend (HttpResponse) -> AmResponse<AmContract<E>>)
    : AmResponse<AmContract<E>> {
        return try {
            val response = call.invoke()
            block.invoke(response)
        } catch (e: Throwable) {
            if(e is ResponseException) {
                if(e.response.bodyAsText().contains("NOT_ALLOWED")) {
                    (AmResponse.Error(Constants.RESPONSE_TYPE.NOT_ALLOWED))
                }else if(e.response.bodyAsText().contains("-2000")) {
                    (AmResponse.Error(Constants.RESPONSE_TYPE.SUBSCRIPTION_NOT_ACTIVE))
                }else{
                    (AmResponse.Error(e.response.status.toString()))
                }
            } else if(e is UnknownHostException) {
                (AmResponse.Error(UNKNOWN_HOST))
            } else {
                (AmResponse.Error("Server error"))
            }
        }
    }

}