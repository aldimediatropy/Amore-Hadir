package com.setalis.amorehr.repositories.auth

import com.setalis.amorehr.data.entities.User
import com.setalis.amorehr.repositories.auth.commands.AuthCommand
import org.koin.core.component.KoinComponent

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
class AuthRepository : KoinComponent {

    suspend fun login(email: String, password: String) = AuthCommand().login(email, password)

    suspend fun user(user: User) = AuthCommand().user(user)

    suspend fun user() = AuthCommand().user()

}