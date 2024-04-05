plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("com.google.devtools.ksp")
}

android {

	namespace = "io.rapidz.assignment1"
	compileSdk = 34
	kotlinOptions { jvmTarget = "1.8" }
	composeOptions { kotlinCompilerExtensionVersion = "1.5.10" }
	buildFeatures { compose = true }

	defaultConfig {
		applicationId = "io.rapidz.assignment1"
		minSdk = 24
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}

	kotlin.sourceSets.all {
		languageSettings.optIn("androidx.compose.foundation.layout.ExperimentalLayoutApi")
		languageSettings.optIn("androidx.compose.material3.ExperimentalMaterial3Api")
	}

}

dependencies {

	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("androidx.activity:activity-ktx:1.8.2")
	implementation("com.google.android.material:material:1.11.0")

	// Coroutine
	val coroutineVersion = "1.8.0"
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

	// Lifecycle
	val lifecycleVersion = "2.7.0"
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
	implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
	implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")

	// Jetpack Compose
	val composeBom = platform("androidx.compose:compose-bom:2024.02.02")
	implementation(composeBom)
	implementation("androidx.compose.ui:ui-tooling-preview")
	debugImplementation("androidx.compose.ui:ui-tooling")
	implementation("androidx.navigation:navigation-compose:2.7.7")
	implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
	implementation("androidx.compose.foundation:foundation")
	implementation("androidx.activity:activity-compose:1.8.2")
	implementation("androidx.compose.ui:ui-viewbinding")
	implementation("androidx.compose.runtime:runtime-livedata")

	// Material 3
	implementation("androidx.compose.material3:material3")
	implementation("androidx.compose.material3:material3-window-size-class")
	implementation("androidx.compose.material:material-icons-core")
	implementation("androidx.compose.material:material-icons-extended")

	// Room Database
	val roomVersion = "2.6.1"
	implementation("androidx.room:room-runtime:$roomVersion")
	implementation("androidx.room:room-ktx:$roomVersion")
	ksp("androidx.room:room-compiler:$roomVersion")

	// DataStore
	implementation("androidx.datastore:datastore-preferences:1.0.0")
	implementation("androidx.datastore:datastore-preferences-core:1.0.0")

}