// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.kotlin.kapt.plugin) apply false
    alias(libs.plugins.hilt.plugin) apply false
//    alias(libs.plugins.navigation.safeargs.plugin) apply false
//    alias(libs.plugins.navigation.safeargs.kotlin.plugin) apply false

}