plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.apollographql.apollo3") version "3.6.2"
    // KSP for Compose-Destinations
    id("com.google.devtools.ksp") version "1.7.0-1.0.6"
    // Dagger-Hilt
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "one.beefsupreme.shibachatandroid"
    compileSdk = 33

    defaultConfig {
        applicationId = "one.beefsupreme.shibachatandroid"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            buildConfigField("String", "SERVER_URL", "\"http://192.168.0.243:9611\"")
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Added for Compose-Destinations to include the generated folders to all variants.
kotlin {
    sourceSets {
        debug {
            kotlin.srcDir("build/generated/ksp/debug/kotlin")
        }
        release {
            kotlin.srcDir("build/generated/ksp/release/kotlin")
        }
    }
}

dependencies {
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0") // hiltViewModel()
    implementation("androidx.navigation:navigation-compose:2.5.2")
    implementation("com.auth0.android:jwtdecode:2.0.1")
    implementation("com.github.franmontiel:PersistentCookieJar:v1.0.1")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.20")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")

    // Apollo
    implementation("com.apollographql.apollo3:apollo-runtime:3.6.2")
    implementation("com.apollographql.apollo3:apollo-normalized-cache:3.6.2")

    // Compose-Destinations
    implementation("io.github.raamcosta.compose-destinations:core:1.6.22-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.6.22-beta")

    // Moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.13.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

    // Dagger-Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-compiler:2.44")

    // Initial
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.activity:activity-compose:1.5.1")
    implementation("androidx.compose.ui:ui:1.2.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.0")
    implementation("androidx.compose.material:material:1.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.2.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.2.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.2.0")
}

apollo {
    packageName.set("one.beefsupreme.shibachatandroid")
    schemaFiles.from(setOf(
        file("src/main/graphql/one/beefsupreme/shibachatandroid/schema.graphqls"),
        file("src/main/graphql/one/beefsupreme/shibachatandroid/extra.graphqls")
    ))
    generateFragmentImplementations.set(true)
}

kapt {
    correctErrorTypes = true
}
