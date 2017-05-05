# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/xiaoyunfei/Library/Android/sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile



-optimizationpasses 5                   # 指定代码的压缩级别
-dontusemixedcaseclassnames             # 混淆时不会产生形形色色的类名
-dontskipnonpubliclibraryclasses        # 指定不忽略非公共的库类
-dontpreverify                          # 不预校验
-dontshrink                             #
-dontoptimize
-verbose                                # 混淆时是否记录日志

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    # 混淆时所采用的算法

