package com.setalis.amorehr.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.setalis.amorehr.base.AmViewModel
import com.setalis.amorehr.data.entities.User
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
class UserViewModel(
    private val handle: SavedStateHandle,
    private val authRepository: AuthRepository
) : AmViewModel() {

    private val _user = MutableLiveData<AmEvent<User>>()
    val user = this._user.immutable()

    private val _update: MutableLiveData<AmEvent<Boolean>> = MutableLiveData()
    val update: LiveData<AmEvent<Boolean>> = this._update.immutable()

    fun user() {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postEvent(authRepository.user())
        }
    }

    fun logout() {

    }

    fun update(data: User) {

    }

    fun editUser(user: User) {

    }
}
