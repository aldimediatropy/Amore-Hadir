package com.setalis.amorehr.applications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mediatropy.eureka.data.network.AmResponse

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */

fun <T> MutableLiveData<T>.immutable(): LiveData<T> = this

fun <T : Any> AmResponse<T>.failure(block: (AmResponse.Error) -> Unit) {
    if(this is AmResponse.Error) {
        block(this)
    } else {
        this
    }
}

fun <T : Any> AmResponse<T>.success(block: (T) -> Unit): AmResponse<T> {
    return if(this is AmResponse.Success) {
        block.invoke(this.response)
        this
    } else {
        this
    }
}