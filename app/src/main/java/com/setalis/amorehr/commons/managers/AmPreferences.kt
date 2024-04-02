package com.setalis.amorehr.commons.managers

import android.content.Context
import androidx.preference.PreferenceManager

/**
 * Created by Al
 * on 22/11/18 | 15:03
 */
class AmPreferences(private val context: Context?, private val name: String = "") {
    private var sharedPref = context?.let {
        PreferenceManager.getDefaultSharedPreferences(context)
    } ?: run {
        null
    }

    fun setString(value: String) {
        with (sharedPref?.edit()) { this?.putString(name, value.encrypt())?.apply() }
    }
    fun setBoolean(value: Boolean) {
        with (sharedPref?.edit()) { this?.putString(name, value.toString().encrypt())?.apply() }
    }

    fun setInt(value: Int) {
        with (sharedPref?.edit()) { this?.putString(name, value.toString().encrypt())?.apply() }
    }

    fun setLong(value: Long) {
        with (sharedPref?.edit()) { this?.putString(name, value.toString().encrypt())?.apply() }
    }

    fun setFloat(value: Float) {
        with (sharedPref?.edit()) { this?.putString(name, value.toString().encrypt())?.apply() }
    }

    fun getString() : String? {
        return sharedPref?.getString(name, null)?.decrypt()
    }

    fun getBoolean() : Boolean {
        return (sharedPref?.getString(name, true.toString()) ?: true.toString()).decrypt().toBoolean()
    }
    fun getInt() : Int {
        return (sharedPref?.getString(name, 0.toString()) ?: 0.toString()).decrypt().toInt()
    }
    fun getLong() : Long {
        return (sharedPref?.getString(name, 0.toString()) ?: 0.toString()).decrypt().toLong()
    }
    fun getFloat() : Float {
        return (sharedPref?.getString(name, 0.toString()) ?: 0.toString()).decrypt().toFloat()
    }
    fun remove(): Boolean {
        return sharedPref?.edit()?.remove(name)?.commit() ?: false
    }

    fun removeAll() {
        sharedPref?.edit()?.clear()?.apply()
    }

    fun toggleBoolean() {
        this.apply {
            setBoolean(!getBoolean())
        }
    }

    private fun String.decrypt() = this
    private fun String.encrypt() = this
}
