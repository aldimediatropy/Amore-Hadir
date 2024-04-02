package com.setalis.amorehr.utils.commons


object Constants {

    object Header {
        const val ApiVersion = "/v1"
    }

    object Duration {
        const val SPLASH: Long = 600
        const val LOCATION_UPDATE: Long = 5000
    }

    object ConstantSessions {
        const val OFFLINE_KEY = "offline_key"
        const val LOGIN_KEY = "login_key"
        const val TOKEN_KEY = "token_key"
    }

    object REQUEST {
        const val PERMISSION_RECORD_AUDIO = 1001
        const val PERMISSION_POST_NOTIFICATION = 1002
        const val PERMISSION_LOCATION = 1003
        const val PERMISSION_CAMERA = 1004
    }

    object Preference {
        const val UserToken = "constPrefUserToken"
        const val Username = "constPrefUsername"
    }

    object RESPONSE_TYPE {
        const val NOT_ALLOWED = "NOT_ALLOWED"
        const val SUBSCRIPTION_NOT_ACTIVE = "SUBSCRIPTION_NOT_ACTIVE"
        const val UNKNOWN_HOST = "UNKNOWN_HOST"
        const val ONLINE = "ONLINE"
    }

    val adzan = arrayOf("Shubuh", "Syuruq", "Dzuhur", "Ashr", "Maghrib", "Isya")

}