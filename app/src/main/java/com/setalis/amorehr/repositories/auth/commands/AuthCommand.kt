package com.setalis.amorehr.repositories.auth.commands

import com.mediatropy.eureka.data.network.AmResponse
import com.setalis.amorehr.commons.managers.SessionManager
import com.setalis.amorehr.data.api.auth.LoginRequest
import com.setalis.amorehr.data.api.auth.LoginResponse
import com.setalis.amorehr.data.dao.UserDao
import com.setalis.amorehr.data.entities.User
import com.setalis.amorehr.utils.networks.AmContract
import com.setalis.amorehr.utils.networks.BaseCommand
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.component.inject

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
class AuthCommand : BaseCommand() {

    private val userDao by inject<UserDao>()
    private val session by inject<SessionManager>()

    companion object Route {
        const val LOGIN = "auth/login"
    }

    suspend fun user(user: User) = userDao.insert(user)
    suspend fun user() = userDao.user()

    suspend fun login(email: String, password: String): AmResponse<AmContract<LoginResponse>> {
        val data = LoginRequest(email, password)

        return makeCall(call = {
            network.client().post(network.server(LOGIN)) {
                setBody(data)
            }
        }) { response ->
            (AmResponse.Success(response.body()))
        }
    }
}
