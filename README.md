# EventBlank for Android

This is the android version of the EventBlankApp for iOS. Find the iOS version here: https://github.com/icanzilb/EventBlankApp

It's an open source blueprint, which will allow creating an event/conference Android app in a matter of ~ 1 hour. The project The project will aim to have as few as possible dependanies so that it can be cloned from GitHub and built immediately.

## Getting started

### 1. Start The project
Download the latest version of Android Studio. Download and add this project in Android Studio.

### 2. Add database
If you already have the sqlite file generated from the iOS version, you can use that file for the Android version too.
Othervise download the sqlite schema from here https://www.dropbox.com/s/t3wea9u9ye7ad68/EventBlank.eventblank?dl=1 , and fill it with your event data.
Put the file in the specific directory:"yourProjectDirectory\EventBlank\app\src\main\assets\databases\".

### 3. Change names
Open the project in Android Studio. In the package explorer find the values folder (src\main\res\values\) and open the strings.xml file. Put the correct name of your app under the app_name tag and the correct name of your sqlite file under dataase_name.

### 4. Launcher icon
Get your launcher icon, rename it to "ic_launcher" and put it in any of the mipmap folders (src\main\res\mipmap). If you the icon in different resolutions, put it in the different mipmap folders (mipmap-mdpi is the lowest resolution folder, mipmap-xxxhdpi is the highest resolution folder).

### 5. Run App
Run the app in Android Studio and enjoy your own application!!
