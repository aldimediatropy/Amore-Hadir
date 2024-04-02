package com.setalis.components.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Crafted by Al (ismealdi) on 07/04/23.
 * Mediatropy
 */

fun <T> MutableLiveData<T>.immutable(): LiveData<T> = this