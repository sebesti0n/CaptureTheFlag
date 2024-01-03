// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    repositories {
        mavenCentral()
    }
    dependencies{
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.2")
        classpath("com.google.gms:google-services:4.4.0")
    }
}
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.5.3" apply false

}