package com.setalis.amorehr.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.setalis.amorehr.applications.failure
import com.setalis.amorehr.applications.success
import com.setalis.amorehr.base.AmViewModel
import com.setalis.amorehr.data.api.auth.LoginResponse
import com.setalis.amorehr.data.network.AmEvent
import com.setalis.amorehr.data.network.postEvent
import com.setalis.amorehr.repositories.auth.AuthRepository
import com.setalis.components.extensions.immutable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
class AuthViewModel(
    private val handle: SavedStateHandle,
    private val authRepository: AuthRepository
) : AmViewModel() {

    private val _login = MutableLiveData<AmEvent<LoginResponse>>()
    val login = this._login.immutable()

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loading(true)

            authRepository.login(email, password).success { response ->
                loading(false)
                handleLogged(response.data)
            }.failure { response ->
                loading(false)
                error(response.message)
            }
        }
    }

    private fun handleLogged(data: LoginResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            data.user?.let { user ->
                data.employee?.let { employee ->
                    user.name = employee.fullname
                    user.telpon = employee.phone
                    user.position = employee.employment?.position?.name.toString()
                    user.company = employee.employment?.company?.name.toString()
                    user.radius = employee.employment?.company?.radius ?: 0
                    user.latitude = employee.employment?.company?.latitude ?: 0.0
                    user.longitude = employee.employment?.company?.longitude ?: 0.0
                    user.shift = employee.shift?.name.toString()
                    user.clock_in = employee.shift?.clockIn.toString()
                    user.clock_out = employee.shift?.clockOut.toString()
                }

                authRepository.user(user)
                data.token?.let { token ->
                    session.isLogin = true
                    session.tokenAccess = token
                }

                _login.postEvent(data)
            }
        }
    }


}
