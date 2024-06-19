package com.collebra.digital.newsapptask.utils

class Constants {
    companion object {
        const val BASE_URL = "https://newsapi.org"
        const val QUERY_PER_PAGE = 10
        const val DEFAULT_PAGE_INDEX = 1
        const val CountryCode = "us"
        const val Category = "business"
        const val DB_NAME = "news_db"
        const val TB_NAME = "news_articles"
        const val DB_VERSION = 1


        const val BIOMETRIC_AUTHENTICATION = "Biometric Authentication"
        const val BIOMETRIC_AUTHENTICATION_SUBTITLE = "Use your fingerprint to authenticate"
        const val BIOMETRIC_AUTHENTICATION_DESCRIPTION = "This app uses your makes use of device biometrics (user fingerprint) to authenticate the dialog."

        const val PASSWORD_PIN_AUTHENTICATION = "Password/PIN Authentication"
        const val PASSWORD_PIN_AUTHENTICATION_SUBTITLE = "Authenticate using Device Password/PIN"
        const val PASSWORD_PIN_AUTHENTICATION_DESCRIPTION = "This app uses your makes use of device password/pin to authenticate the dialog."

    }
}