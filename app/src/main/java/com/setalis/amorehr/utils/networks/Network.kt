package com.setalis.amorehr.utils.networks

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.setalis.amorehr.BuildConfig
import com.setalis.amorehr.applications.KoinModule
import com.setalis.amorehr.commons.AmLog
import com.setalis.amorehr.commons.managers.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
class Network(context: Context) : KoinComponent {

    private val server by inject<String>(named(KoinModule.Server))
    private val session by inject<SessionManager>()

    private val collector = ChuckerCollector(
        context = context,
        showNotification = true,
        // Allows to customize the retention period of collected data
        retentionPeriod = RetentionManager.Period.ONE_HOUR
    )

    private val interceptor = ChuckerInterceptor.Builder(context)
        .collector(collector)
        .redactHeaders("Authorization")
        .alwaysReadResponseBody(true)
        .build()

    private val okhttpEngine = io.ktor.client.engine.okhttp.OkHttp.create {
        addInterceptor(interceptor)
    }

    private val client = HttpClient(okhttpEngine) {
        expectSuccess = false

        //Header
        install(DefaultRequest) {
            header("Accept", "application/json")
            header("Content-type", "application/json")
            header("X-Api-Key", BuildConfig.key)

            if(session.tokenAccess.isNotEmpty()) {
                header("Authorization", "Bearer " + session.tokenAccess)
            }

            contentType(ContentType.Application.Json)

        }

        // Gson serialization
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }

        // Timeout
        install(HttpTimeout) {
            requestTimeoutMillis = 45000L
            connectTimeoutMillis = 15000L
            socketTimeoutMillis = 45000L
        }

        //Response logs
        install(Logging) {
            logger = AmLog.HttpLogger()
            level = LogLevel.ALL
        }

        //Print other logs
        install(ResponseObserver) {
            onResponse { response ->
                AmLog.d("HTTP status: ${response.status.value}")
            }
        }

        HttpResponseValidator {
            this.handleResponseExceptionWithRequest { _, _ ->
                validateResponse { response ->
                    try {
                    } catch (e: Throwable) {
                        throw ResponseException(response, e.message.toString())
                    }
                }
            }
        }

    }

    fun client() = client
    fun server(path: String) = "$server/$path"

}