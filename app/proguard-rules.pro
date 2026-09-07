# ProGuard rules for Moctest GPSC Android App

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-dontwarn retrofit2.**

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Gson
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }
-dontwarn com.google.gson.**

# Kotlin
-keepclassmembers class ** {
    *** Companion;
}
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.**
