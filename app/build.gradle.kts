plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }

android {
    namespace = "de.ninalex.fotoarchiv"
    compileSdk = 35
    defaultConfig {
        applicationId = "de.ninalex.fotoarchiv"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
        // Enter the PUBLIC Dropbox app key locally. Never add an app secret.
        buildConfigField("String", "DROPBOX_APP_KEY", "\"${project.findProperty("DROPBOX_APP_KEY") ?: ""}\"")
    }
    buildFeatures { buildConfig = true }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
}
