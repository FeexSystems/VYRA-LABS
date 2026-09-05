# VYRA ProGuard Rules
# Optimizes and obfuscates the release build

# Keep Compose classes
-keep class androidx.compose.** { *; }
-keep class kotlin.Metadata { *; }

# Keep Room database entities
-keep class com.example.vyra.data.db.** { *; }
-keep @androidx.room.Entity class * { *; }

# Keep data classes used in ViewModels
-keep class com.example.vyra.ui.viewmodels.** { *; }
-keep class com.example.vyra.data.models.** { *; }

# Keep serialization
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses

# Keep ElevenLabs service
-keep class com.example.vyra.service.** { *; }

# Keep navigation
-keep class androidx.navigation.** { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Optimize
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
