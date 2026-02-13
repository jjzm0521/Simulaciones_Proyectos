plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.curso_simulaciones.micuadrigesimaquintaapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.curso_simulaciones.micuadrigesimaquintaapp"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    
    // Incluir todos los archivos JAR y AAR de la carpeta libs
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    
    // implementation(files("libs/org.eclipse.paho.client.mqttv3-1.2.0.jar")) 
    implementation("com.github.hannesa2:paho.mqtt.android:3.3.5")
    
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
}