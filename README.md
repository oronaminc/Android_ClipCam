# Android_ClipCam
Android_ClipCam with SK CloudCam API 공모전

## Function

1. InitialScreen Activity
2. Login & Access Activity (Using SK Broadband CloudCam API)

## ScreenShot
<div>
<img width="200" src="https://user-images.githubusercontent.com/37185394/54339374-feb77800-4677-11e9-9844-70a77608cb63.jpg"/>
<img width="200" src="https://user-images.githubusercontent.com/37185394/54339376-feb77800-4677-11e9-97ba-c9c190715768.jpg"/>
<img width="200" src="https://user-images.githubusercontent.com/37185394/54339377-ff500e80-4677-11e9-9515-c67e70c1cdce.jpg"/>
<img width="200" src="https://user-images.githubusercontent.com/37185394/54339378-ff500e80-4677-11e9-85f6-e2271c1b93a8.jpg"/>
</div>

## Contest Prize
<div>
<img width="200" src="https://user-images.githubusercontent.com/37185394/55533677-0dc89d80-56ed-11e9-9bb0-562184cb0144.jpg"/>
<img width="200" src="https://user-images.githubusercontent.com/37185394/55533679-0f926100-56ed-11e9-9486-7a6b30a905f8.jpg"/>
<img width="200" src="https://user-images.githubusercontent.com/37185394/55533720-2df85c80-56ed-11e9-84e3-bba2d83e5f9d.jpg"/>
</div>

## Video
Clip Cam Application : https://www.youtube.com/watch?v=zUBYpvNQ0Ps
Clip Cam Service : https://www.youtube.com/watch?v=2umsT3-t6wg

## Prototype
https://ovenapp.io/view/37E7yQCX8pFaXYr0Xn9dQCPkizEMMPOi/wsrLa

### dependencies
```java
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation 'com.android.support:appcompat-v7'
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'
    testImplementation 'junit:junit:4.12'
    //Permission
    implementation 'gun0912.ted:tedpermission:2.2.0'
    //ImageLoader
    implementation 'com.github.bumptech.glide:glide:3.8.0'
    // MediaPlayer
    implementation files("libs/ti2me_rplayer")
    // API SDK
    implementation 'com.skbb.api:skb-release:1.0@aar'
    implementation 'com.android.support:design:27.0.0'
    implementation 'com.google.firebase:firebase-core:16.0.1'
    implementation 'com.google.firebase:firebase-auth:16.0.2'
    implementation 'com.google.firebase:firebase-database:16.0.1'
    implementation 'com.firebaseui:firebase-ui-auth:4.1.0'
}
```

### AndroidManifest.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.tisquare.cloud">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

    <application
        android:name=".ApplicationClass"
        android:allowBackup="true"
        android:hardwareAccelerated="true"
        android:icon="@drawable/cloudlogo"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme"
        tools:replace="android:icon">
        <activity android:name=".Intro">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity
            android:name=".Login"
            android:launchMode="singleInstance"
            android:screenOrientation="portrait" />
        <activity
            android:name=".CameraListActivity"
            android:launchMode="singleInstance"
            android:screenOrientation="portrait" />
        <activity
            android:name=".CameraPlayBackActivity"
            android:launchMode="singleInstance"
            android:screenOrientation="landscape" />
        <activity
            android:name=".CameraLiveActivity"
            android:launchMode="singleInstance"
            android:screenOrientation="landscape" />
    </application>

</manifest>
```
