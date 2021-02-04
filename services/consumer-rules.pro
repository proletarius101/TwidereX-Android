
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt # core serialization annotations

# kotlinx-serialization-json specific. Add this if you have java.lang.NoClassDefFoundError kotlinx.serialization.json.JsonObjectSerializer
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class com.twidere.services.**$$serializer { *; }
-keepclassmembers class com.twidere.services.** {
    *** Companion;
}
-keepclasseswithmembers class com.twidere.services.** {
    kotlinx.serialization.KSerializer serialier(...);
}

-keep class com.jakewharton.retrofit2.**

-keep class com.twidere.services.**