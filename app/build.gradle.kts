plugins {
    id("com.android.application") version "8.2.2" apply true
    id("androidx.navigation.safeargs") version "2.7.7" apply true

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.networker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.networker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("io.reactivex.rxjava3:rxjava:3.0.0")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("me.legrange:mikrotik:3.0.7")

    implementation("com.jjoe64:graphview:4.2.2")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.preference:preference:1.2.1")
    implementation("androidx.fragment:fragment:1.6.2")

    // Used by at least bottomNavBar
    implementation("com.google.android.material:material:1.11.0")

    // Navigation component/graph
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.7")
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")
    //implementation("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")

    compileOnly("com.android.tools.build:gradle:8.3.2")
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    // https://firebase.google.com/docs/android/setup#available-libraries

    ////implementation(platform("com.google.firebase:firebase-auth:22.3.1"))
    //implementation(platform("com.google.android.gms:play-services-auth:21.1.0"))
    implementation("com.google.firebase:firebase-auth")
    //implementation("com.google.android.gms:play-services-auth")

    implementation("com.google.firebase:firebase-firestore")
    //implementation("com.android.support:multidex:1.0.3")
    implementation("com.google.firebase:firebase-database")
}