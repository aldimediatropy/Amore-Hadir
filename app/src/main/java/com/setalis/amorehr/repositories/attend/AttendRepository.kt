package com.setalis.amorehr.repositories.attend

import com.setalis.amorehr.data.api.attend.AttendRequest
import com.setalis.amorehr.repositories.auth.commands.AttendCommand
import org.koin.core.component.KoinComponent

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
class AttendRepository : KoinComponent {

    suspend fun attend(data: AttendRequest) = AttendCommand().attend(data)
    suspend fun attendances() = AttendCommand().attendances()


}