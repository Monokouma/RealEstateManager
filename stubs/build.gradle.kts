plugins {
    id("com.android.library")
    kotlin("android")
}


android {
    namespace = "com.despaircorp.stubs"
    compileSdk = 34
    
    defaultConfig {
        minSdk = 26
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        isCoreLibraryDesugaringEnabled = true
        
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    
    
}

dependencies {
    implementation(project(":ui"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("androidx.arch.core:core-testing:2.1.0") {
        // Removes the Mockito dependency bundled with arch core (wtf android ??)
        exclude("org.mockito", "mockito-core")
    }
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
    implementation("org.slf4j:slf4j-nop:2.0.7")
}