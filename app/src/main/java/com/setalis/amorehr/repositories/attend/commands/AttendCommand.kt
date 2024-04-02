package com.setalis.amorehr.repositories.auth.commands

import com.mediatropy.eureka.data.network.AmResponse
import com.setalis.amorehr.commons.managers.SessionManager
import com.setalis.amorehr.data.api.attend.AttendRequest
import com.setalis.amorehr.data.api.auth.LoginResponse
import com.setalis.amorehr.data.dao.UserDao
import com.setalis.amorehr.data.models.Attendance
import com.setalis.amorehr.utils.networks.AmContract
import com.setalis.amorehr.utils.networks.BaseCommand
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.koin.core.component.inject

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
class AttendCommand : BaseCommand() {

    private val userDao by inject<UserDao>()
    private val session by inject<SessionManager>()

    companion object Route {
        const val ATTEND = "attend"
        const val ATTENDANCES = "attendances"
    }

    suspend fun attendances(): AmResponse<AmContract<List<Attendance>>> {
        return makeCall(call = {
            network.client().get(network.server(ATTENDANCES))
        }) { response ->
            (AmResponse.Success(response.body()))
        }
    }

    suspend fun attend(data: AttendRequest): AmResponse<AmContract<LoginResponse>> {
        return makeCall(call = {
            network.client().post(network.server(ATTEND)) {
                setBody(data)
            }
        }) { response ->
            (AmResponse.Success(response.body()))
        }
    }
}
