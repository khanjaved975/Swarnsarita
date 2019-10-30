# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/javedkhan/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interf
# class:
#-keepclassmembers class fqcn.of.javascript.interf.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn twitter4j.internal.logging.**
-dontwarn org.apache.http.**
-dontwarn com.squareup.okhttp.**

#### -- Picasso --
 -dontwarn com.squareup.picasso.**

 -keep public class * implements com.bumptech.glide.module.GlideModule
 -keep public class * extends com.bumptech.glide.module.AppGlideModule
 -keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
 }

 # for DexGuard only
 #keepresourcexmlelements manifest/application/meta-data@value=GlideModule


-keep class okhttp3.internal.publicsuffix.PublicSuffixDatabase


 #### -- OkHttp --

 -dontwarn com.squareup.okhttp.internal.**

 #### -- Apache Commons --

 -dontwarn org.apache.commons.logging.**

-keep class .R
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
#-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.**
-dontwarn okio.**
-dontwarn com.squareup.okhttp3.**
-dontwarn retrofit2.Platform$Java8
#-keep class com.squareup.okhttp3.** { ; }
-keep interface com.squareup.okhttp3.* { *; }
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }

-keepclassmembers class **.R$* {
 public static <fields>;
}



-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
}

-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(...);
}

-keepclasseswithmembernames class * {
 native <methods>;
}

-dontwarn javax.annotation.GuardedBy

-dontwarn com.fasterxml.jackson.**

-keepnames class com.amazonaws.**
# Request handlers defined in request.handlers
-keep class com.amazonaws.services.**.*Handler
# The following are referenced but aren't required to run
-dontwarn com.amazonaws.http.**
-dontwarn com.amazonaws.metrics.**

-dontwarn javax.annotation.**

-dontwarn sun.misc.Unsafe
-dontwarn com.instagram.common.json.annotation.processor.**

#-dontwarn com.facebook.infer.annotation.ReturnsOwnership
#-dontwarn com.facebook.infer.annotation.Functional
#-dontwarn com.facebook.infer.annotation.ThreadSafe

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.DialogFragment
-keep public class * extends android.app.Fragment

-keepclassmembers class * extends com.stephentuso.welcome.WelcomeActivity {
    public static java.lang.String welcomeKey();
}
