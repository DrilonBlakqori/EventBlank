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

Finaly your app should look something like this:

![alt text](http://i.imgur.com/uz97UOA.gif "Demo app")  
  
##Database info
If you already have the database from the iOS version skip this part. 
The database consists of eight tables: events, locations, meta, sessions, speakers, sqlite_sequence, texts, tracks. Here's a detailed description of each of them.

###events
Contains general information of the event:

  * title - title of the event
  * subtitle - title of the event
  * begin_date - begin date of the event in milliseconds (or seconds). You can use [this](http://currentmillis.com/) to convert dates to milliseconds
  * end_date - end date of the event in milliseconds (or seconds)
  * organizer - organizer of the event
  * logo - logo of the event
  * twitter_tag - twitter tag of the event (ex. #besteventever)
  * twitter_admin - twitter timeline (ex. @bestEventEver)
  * main_color - color of toolbars, a few titles and icons
  * secondary_color - color of tab indicators and some texts
  * twitter_chatter - you can skip this
  * ternary_color - color of toolbar texts and icons
  * update_file\_url - direct download link of your updated database file. Use this to update the database.

###locations
Contains the locations of the sessions:

  * id_location - id of the location
  * location - name of the location
  * location_description - brief description of the location
  * location_map - you can skip this
  * lat, lng - latitude and longtitude of the location. Setting those will allow opening Google maps after clicking on a location.

###meta
  * version - the version of the database (change this every time you upload a new version on the download link)

###sessions
Contains the sessions of the event

  * id_session - id of the session
  * title - title of the session
  * description - description of the session
  * begin_time - begin time in milliseconds (or seconds)
  * fk_track - id of the track (take from tracks table)
  * fk_location - id of the location (take from locations table)
  * is_favorite - you can skip this (it's used by the user)
  * fk_speaker - id of the speaker (take from speakers table)

###speakers
Contains the speakers of the event

  * id_speaker - id of the speaker
  * speaker - name of the speaker
  * speaker_twitter - twitter timeline of the speaker (ex. @firstSpeaker)
  * speaker_url - website of the speaker
  * photo - photo of the speaker
  * bio - brief bio of the speaker

###sqlite_sequence
Skip this table

###texts
Contains the texts displayed at the About, Code of Conduct and Sponsors options in the More tab. Use markdown language to style the text.

###tracks
Contains the tracks of the event

  * id_track - id of the track
  * track - name of the track
  * track_description - description of the track


For further informations or bug reports contact me at: drilon.blak@hotmail.com
