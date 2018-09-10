# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/jacky_zhou/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes *Annotation*

# 项目通用混淆配置
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
#-flattenpackagehierarchy
#-repackageclasses ''
-allowaccessmodification
-keepattributes SourceFile,LineNumberTable
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * implements java.io.Serializable{*;}
-keep public class * implements android.view.View.OnTouchListener
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
  }

-keepclassmembers class * implements android.os.Parcelable {
     static ** CREATOR;
 }


# 不混淆，兼容包代码
-keep class android.support.** {*;}

# Gson specific classes
 -keepattributes Signature
 -keep class com.google.gson.stream.** { *; }
 -keep class com.google.gson.Gson {*;}

 # for GreenDAO
 -keepclasseswithmembernames class * {
     public <init>(android.content.Context, android.util.AttributeSet);
 }
 -keepclasseswithmembernames class * {
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }
 -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
     public static java.lang.String TABLENAME;
 }
 # If you do not use SQLCipher:
 -dontwarn org.greenrobot.greendao.database.**

 # OkHttp3
 -keep class okhttp3.**{*;}
 -keep interface okhttp3.**{*;}
 -dontwarn okhttp3.**
 # okio
 -dontwarn okio.**