package com.collebra.digital.newsapptask.utils

class Constants {
    companion object {
        const val API_KEY = "7e33a327002c4030a5b136c6367d055b"
        const val BASE_URL = "https://newsapi.org"
        const val searchTimeDelay = 500L
        const val QUERY_PER_PAGE = 10
        const val DEFAULT_PAGE_INDEX = 1
        const val CountryCode = "us"
        const val Category = "business"
        const val DB_NAME = "news_db"
        const val TB_NAME = "news_articles"


        const val AUTHENTICATION_FAILED = "Authentication failed"
        const val AUTHENTICATION_SUCCEEDED = "Authentication succeeded"
        const val AUTHENTICATION_ERROR = "Authentication error"

        const val BIOMETRIC_AUTHENTICATION = "Biometric Authentication"
        const val USE_DEVICE_PASSWORD = "Use device password"
        const val BIOMETRIC_AUTHENTICATION_SUBTITLE = "Use your fingerprint to authenticate"
        const val BIOMETRIC_AUTHENTICATION_DESCRIPTION = "This app uses your makes use of device biometrics (user fingerprint) to authenticate the dialog."

        const val AUTHENTICATE_OTHER="Authenticate using Device Password/PIN"
        const val AUTHENTICATE_FINGERPRINT="Authenticate using Fingerprint"

        const val AVAILABLE="Available"
        const val UNAVAILABLE="Unavailable"
        const val TRUE="True"
        const val FALSE="False"
        const val CANCEL="Cancel"

        const val PASSWORD_PIN_AUTHENTICATION = "Password/PIN Authentication"
        const val PASSWORD_PIN_AUTHENTICATION_SUBTITLE = "Authenticate using Device Password/PIN"
        const val PASSWORD_PIN_AUTHENTICATION_DESCRIPTION = "This app uses your makes use of device password/pin to authenticate the dialog."

    }
}