plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    alias(libs.plugins.jetbrains.kotlin.kapt)

}

android {
    namespace = "com.example.shoppinglist"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.shoppinglist"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Корутины (основная библиотека)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

// Корутины для Android (с поддержкой жизненного цикла)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

// Корутины для ViewModel / LiveData (если работаешь с ними)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.4")

    // Основная библиотека Room
    implementation("androidx.room:room-runtime:2.8.2")

// Компилятор аннотаций (обязательно)
    kapt("androidx.room:room-compiler:2.8.2")

// Поддержка корутин (чтобы можно было писать suspend-функции)
    implementation("androidx.room:room-ktx:2.8.2")


}