package com.setalis.amorehr.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.setalis.amorehr.commons.managers.SessionManager
import com.setalis.amorehr.data.network.AmEvent
import com.setalis.amorehr.data.network.postEvent
import com.setalis.components.extensions.emptyString
import com.setalis.components.extensions.immutable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
open class AmViewModel : ViewModel(), KoinComponent {

    internal val session by inject<SessionManager>()

    private val _loading: MutableLiveData<AmEvent<Boolean>> = MutableLiveData()
    val loading: LiveData<AmEvent<Boolean>> = this._loading.immutable()

    private val _error: MutableLiveData<AmEvent<String>> = MutableLiveData()
    val error: LiveData<AmEvent<String>> = this._error.immutable()

    fun loading(state: Boolean) {
        if(state) error(emptyString())
        _loading.postEvent(state)
    }

    fun error(info: String) {
        _error.postEvent(info)
    }
}