# EventBlank for Android

This is the android version of the EventBlankApp. Find the iOS version [here](https://github.com/icanzilb/EventBlankApp)

It's an open source blueprint, which will allow you to create your own event/conference Android app in a matter of ~ 1 hour. The project will aim to have as few as possible dependanies so that it can be cloned from GitHub and built immediately.

## Getting started

### 1. Start The project
Download the latest version of Android Studio. Download and add this project in Android Studio.

### 2. Add database
Download the sqlite schema [here](https://www.dropbox.com/s/t3wea9u9ye7ad68/EventBlank.eventblank?dl=1), and fill it with your event data using SQLite Browser or any other SQLite bowsing programs.
You may also use the EventBlankProducer to generate the file or if you already have your iOS version App use the same SQLite file here.
Copy the file in the specific directory:("yourProjectDirectory\EventBlank\app\src\main\assets\databases\").

### 3. Change names
Open the project in Android Studio. In the package explorer find the values folder ("src\main\res\values\") and open the strings.xml file. Put the correct name of your app under the app_name tag and the correct name of your sqlite file under dataase_name.

### 4. Launcher icon
Setup your launcher icons (you can use[Launcher Icon Generator](http://romannurik.github.io/AndroidAssetStudio/icons-launcher.html#foreground.space.trim=1&foreground.space.pad=0&foreColor=607d8b%2C0&crop=0&backgroundShape=square&backColor=ffffff%2C100&effects=none)). Upload your icon and download the new generated icons. Copy the generated icons in the specific mipmap folders ("src\main\res\mipmap-*") and delete the default existing ones. 

### 5. Run App
Run the app in Android Studio and enjoy your own application!!

Image:
![alt text](http://i.imgur.com/dsKRoiS.png "Logo Title Text 1")
