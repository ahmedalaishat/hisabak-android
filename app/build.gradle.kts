import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

// Release signing is read from a gitignored keystore.properties (local) or env vars (CI).
// When neither is present, release builds fall back to the debug key so local/CI builds
// still work without any secrets. The prod → Play pipeline opts into strict signing
// (-PrequireReleaseSigning) so a publishable build can never silently use the debug key.
val keystorePropsFile = rootProject.file("keystore.properties")
val keystoreProps = Properties().apply {
    if (keystorePropsFile.exists()) FileInputStream(keystorePropsFile).use { load(it) }
}
fun signingProp(prop: String, env: String): String? =
    keystoreProps.getProperty(prop) ?: System.getenv(env)
// Real release signing requires a keystore path that actually resolves to a file — a bare env var
// pointing at a missing/empty keystore must not count as "signed".
val hasReleaseSigning = signingProp("storeFile", "RELEASE_KEYSTORE_PATH")
    ?.let { rootProject.file(it).exists() } == true

// When the prod → Play pipeline passes -PrequireReleaseSigning, a missing release keystore is a
// hard error rather than a silent fall back to the debug key: a debug-signed AAB must never reach
// Play. Local builds and the staging (Firebase) build omit the flag and keep the debug-key
// fallback, so they still work without any secrets.
val requireReleaseSigning = providers.gradleProperty("requireReleaseSigning").isPresent
if (requireReleaseSigning && !hasReleaseSigning) {
    throw GradleException(
        "Release signing is required (-PrequireReleaseSigning) but no usable keystore was found. " +
            "Set keystore.properties or the RELEASE_KEYSTORE_* env vars (and verify the keystore " +
            "file exists) before building a publishable release.",
    )
}

android {
    namespace = "com.hisabak"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.hisabak"
        minSdk = 29
        targetSdk = 36
        versionCode = 5
        versionName = "1.4.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            if (hasReleaseSigning) {
                storeFile = file(signingProp("storeFile", "RELEASE_KEYSTORE_PATH")!!)
                storePassword = signingProp("storePassword", "RELEASE_KEYSTORE_PASSWORD")
                keyAlias = signingProp("keyAlias", "RELEASE_KEY_ALIAS")
                keyPassword = signingProp("keyPassword", "RELEASE_KEY_PASSWORD")
            }
        }
    }

    flavorDimensions += "environment"
    productFlavors {
        create("prod") {
            dimension = "environment"
            // applicationId stays com.hisabak (from defaultConfig)
            resValue("string", "app_name", "Hisabak")
            buildConfigField("boolean", "SEED_DATA", "false")
            // Google Play disallows the SMS restricted permission for this use case, so the
            // Play/prod build captures via share + select-text + manual paste only.
            buildConfigField("boolean", "SMS_AUTO_CAPTURE", "false")
        }
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            resValue("string", "app_name", "Hisabak STG")
            buildConfigField("boolean", "SEED_DATA", "true")
            // RECEIVE_SMS auto-capture lives in staging only (distributed off-Play via Firebase).
            buildConfigField("boolean", "SMS_AUTO_CAPTURE", "true")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            // Real release key when configured (keystore.properties / CI secrets); otherwise the
            // debug key so the build still works locally and in CI without secrets.
            signingConfig = if (hasReleaseSigning) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }
}

// Export the Room schema so migrations can be authored and validated against it. The generated
// JSON under app/schemas/ is committed.
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.vico.compose.m3)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
