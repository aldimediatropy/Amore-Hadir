package com.setalis.amorehr.data.network

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */
@Parcelize
open class AmEvent<out T>(private val content: @RawValue T?) : Parcelable {

    var hasBeenHandled = false
        private set

    fun getEventIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T? = content

    companion object {
        fun <T> of(event: T?): AmEvent<T> {
            return AmEvent(event)
        }
    }
}

// Post event without wrapping data inside Event in related code
fun <T> MutableLiveData<AmEvent<T>>.postEvent(t: T) {
    this?.postValue(AmEvent.of(t))
}

fun <T> MutableLiveData<T>?.repost() {
    this?.postValue(this.value)
}