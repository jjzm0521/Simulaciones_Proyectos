plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.curso_simulaciones.micuadragesimacuartaapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.curso_simulaciones.micuadragesimacuartaapp"
        minSdk = 21
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // implementation(files("libs/org.eclipse.paho.client.mqttv3-1.2.0.jar")) 
    // Commented out to avoid duplicate classes with the Paho Android Service library below, 
    // which includes the client and fixes for Android 12+ (required for Android 16).
    implementation("com.github.hannesa2:paho.mqtt.android:3.3.5")
    
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
}