plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt.plugin)
    alias(libs.plugins.hilt.plugin)

}

android {
    namespace = "com.collebra.digital.newsapptask"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.collebra.digital.newsapptask"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                // Refer https://developer.android.com/jetpack/androidx/releases/room#compiler-options
                arguments(
                    mapOf(
                        "room.schemaLocation" to "$projectDir/schemas",
                        "room.incremental" to "true",
                        "room.expandProjection" to "true"
                    )
                )
            }
        }
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

//RoomDependencies
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)
    testImplementation(libs.room.testing)

//HiltDaggerDependencies
    implementation(libs.hilt.android)
//    kapt(libs.hilt.android.compiler)
//    implementation(libs.hilt.lifecycle.vm)
    kapt(libs.hilt.compiler)
//    implementation(libs.hilt.plugin)
//    kapt(libs.google.dagger.hilt.compiler)

//NetworkingDependencies
    implementation(libs.retrofit.core)
    implementation(libs.okhttp)
    implementation(libs.gson.core)
    implementation(libs.gson.converter)
    implementation(libs.okhttp.logging)
    implementation(libs.okhttp.urlconnection)
    implementation(libs.okhttp.mockwebserver)

//GlideDependencies
    implementation(libs.glide)
    implementation(libs.glide.compiler)

//LifeCycleKtxDependencies
    implementation(libs.lifecycle.extension)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)
    implementation(libs.viewmodel)
    implementation(libs.viewmodel.compose)

//Coroutines
    implementation(libs.kmm.coroutines.core)
    implementation(libs.coroutines.android)

//Mockito
//    implementation(libs.test.mockito.core)
//    implementation(libs.test.mockito.android)
//    implementation(libs.test.mockito.inline)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}