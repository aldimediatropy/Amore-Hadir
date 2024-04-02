package com.setalis.amorehr.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.setalis.amorehr.applications.failure
import com.setalis.amorehr.applications.success
import com.setalis.amorehr.base.AmViewModel
import com.setalis.amorehr.data.api.attend.AttendRequest
import com.setalis.amorehr.data.api.auth.LoginResponse
import com.setalis.amorehr.data.models.Attendance
import com.setalis.amorehr.data.network.AmEvent
import com.setalis.amorehr.data.network.postEvent
import com.setalis.amorehr.repositories.attend.AttendRepository
import com.setalis.components.extensions.immutable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
class AttendViewModel(
    private val handle: SavedStateHandle,
    private val attendRepository: AttendRepository
) : AmViewModel() {

    private val _attend = MutableLiveData<AmEvent<LoginResponse>>()
    val attend = this._attend.immutable()

    private val _attendances = MutableLiveData<AmEvent<List<Attendance>>>()
    val attendances = this._attendances.immutable()

    fun attend(latitude: Double, longitude: Double, image: String) {
        val data = AttendRequest(latitude, longitude, image)
        viewModelScope.launch(Dispatchers.IO) {
            loading(true)

            attendRepository.attend(data).success { response ->
                loading(false)
                _attend.postEvent(response.data)
            }.failure { response ->
                loading(false)
                error(response.message)
            }
        }
    }

    fun attendances() {
        viewModelScope.launch(Dispatchers.IO) {
            loading(true)

            attendRepository.attendances().success { response ->
                loading(false)
                _attendances.postEvent(response.data)
            }.failure { response ->
                loading(false)
                error(response.message)
            }
        }
    }

}
