// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false

    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.1" apply false
}

buildscript {
    repositories {
        // other repositories...
        mavenCentral()
        //google()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
        //classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
    }
}