plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.randomuserexplorer"
    compileSdk = 35


    defaultConfig {
        applicationId = "com.example.randomuserexplorer"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Networking (Retrofit + Gson)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    // Dependency Injection (Hilt)
    implementation(libs.hilt.android.v250) // ✅ Latest Hilt
    kapt(libs.hilt.compiler.v250) // ✅ Annotation processor

    // Hilt for Jetpack Compose Navigation
    implementation(libs.androidx.hilt.navigation.compose.v110)


    // Image Loading (Coil)
    implementation (libs.coil.compose)

    // Coroutines
    implementation (libs.kotlinx.coroutines.android)

    // Testing
    testImplementation (libs.junit)
    androidTestImplementation (libs.androidx.junit.v115)

    //Coil For Image Loading
    implementation(libs.coil.compose)

    //Gson For JSON Converter
    implementation(libs.gson)

    // JUnit
    testImplementation (libs.junit)

    testImplementation(libs.mockito.core.v520)
    testImplementation(libs.mockito.kotlin.v510)
    testImplementation(libs.kotlinx.coroutines.test.v173)



    // Coroutines testing (if needed)
    testImplementation(libs.kotlinx.coroutines.test)


}