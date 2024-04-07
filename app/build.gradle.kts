plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlinx.kover")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.despaircorp.realestatemanagerkotlin"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.despaircorp.realestatemanagerkotlin"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
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
    buildFeatures {
        viewBinding = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(project(":ui"))
    implementation(project(":domain"))
    implementation(project(":data"))
    testImplementation(project(":stubs"))
    implementation(project(":shared"))
    
    implementation("androidx.hilt:hilt-work:1.1.0")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
    
    implementation("com.google.dagger:hilt-android:2.48.1")
    ksp("com.google.dagger:hilt-compiler:2.48.1")
    ksp("androidx.hilt:hilt-compiler:1.1.0")
    
    
    testImplementation("androidx.arch.core:core-testing:2.1.0") {
        // Removes the Mockito dependency bundled with arch core (wtf android ??)
        exclude("org.mockito", "mockito-core")
    }
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
    testImplementation("app.cash.turbine:turbine:1.0.0")
}

dependencies {
    kover(project(":ui"))
    kover(project(":domain"))
    kover(project(":data"))
}

koverReport {
    filters {
        excludes {
            annotatedBy(
                "dagger.Module",
                "dagger.internal.DaggerGenerated",
                "androidx.room.Database",
                
                )
            packages(
                "hilt_aggregated_deps", // Hilt: GeneratedInjectors (NOT annotated by DaggerGenerated)
                "com.despaircorp.ui.theme", // ViewBinding
                "dagger.hilt.internal.aggregatedroot.codegen",
            )
            classes(
                "com.despaircorp.ui.login.activity.*",
                "com.despaircorp.ui.main.activity.*",
                // Hilt
                // Delete when this is fixed: https://github.com/Kotlin/kotlinx-kover/issues/331
                "*_*Factory\$*",
                
                // DATA
                // Room
                "*_Impl",
                "*_Impl\$*",
                "com.despaircorp.data.picture.*",
                "com.despaircorp.data.picture.dto.*",
                "com.despaircorp.ui.BuildConfig",
                "com.despaircorp.domain.picture.model.*",
                // Gradle Generated
                "com.despaircorp.data.BuildConfig",
                // Delete when this is fixed: https://github.com/Kotlin/kotlinx-kover/issues/331
                "*AppDatabase\$*",
                
                //Compose Things
                
                "com.despaircorp.realestatemanagerkotlin.main.*",
                // Utils
                "com.despaircorp.ui.utils.CoroutineDispatcherProvider",
                "com.despaircorp.ui.utils.UiProvideModule",
                "com.despaircorp.ui.utils.ProfilePictureRandomizator",
                "com.despaircorp.data.utils.*",
                "com.despaircorp.domain.utils.*",
                // Remove below when Kover can handle Android integration tests!
                "*MainApplication",
                "*MainApplication\$*",
                "*Fragment",
                "*Fragment\$*",
                "*Activity",
                "*Activity\$*",
                "*Adapter",
                "*Adapter\$*",
                
                )
        }
    }
}

