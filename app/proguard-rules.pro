# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# --- Marginal: keep Firestore data classes intact ---
# Firestore uses reflection to map documents onto these classes by field name.
# R8 doesn't know that — without these rules, it will rename/strip fields in
# release builds, and reads will silently come back empty or wrong even
# though everything works fine in debug builds.
-keep class com.example.marginal.data.remote.dto.** { *; }
-keepclassmembers class com.example.marginal.data.remote.dto.** { *; }

# Keep domain model enums intact - referenced via NoteCategory.valueOf(...)
-keep class com.example.marginal.domain.model.** { *; }