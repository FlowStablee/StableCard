-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

-keep class dagger.hilt.internal.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent
