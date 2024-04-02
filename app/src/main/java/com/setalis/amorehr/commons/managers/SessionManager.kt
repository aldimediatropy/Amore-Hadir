package com.setalis.amorehr.commons.managers

import android.content.Context
import androidx.preference.PreferenceManager
import com.setalis.amorehr.utils.commons.Constants.ConstantSessions.LOGIN_KEY
import com.setalis.amorehr.utils.commons.Constants.ConstantSessions.OFFLINE_KEY
import com.setalis.amorehr.utils.commons.Constants.ConstantSessions.TOKEN_KEY
import com.setalis.components.extensions.emptyString
import com.setalis.components.extensions.getValueOrEmpty

/**
 * Crafted by Al (ismealdi) on 10/02/24.
 * Setalis Digital
 */
class SessionManager(
    private val context: Context
) {

    private val preferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this.context)
    }

    var isOffline: Boolean
        get() = this.preferences
            .getBoolean(OFFLINE_KEY, false)
        set(value) = this.preferences
            .edit()
            .putBoolean(OFFLINE_KEY, value)
            .apply()

    var isLogin: Boolean
        get() = this.preferences
            .getBoolean(LOGIN_KEY, false)
        set(value) = this.preferences
            .edit()
            .putBoolean(LOGIN_KEY, value)
            .apply()

    var tokenAccess: String
        get() = this.preferences
            .getString(TOKEN_KEY, emptyString())
            .getValueOrEmpty()
        set(value) = this.preferences
            .edit()
            .putString(TOKEN_KEY, value)
            .apply()
}