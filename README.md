# Pitch Android App
This repository contains the source code of Pitch Android App. It's a mobile application built for event managers, and it helps them in developing mobile app for their event. The pitch Android app acts as a parent app that can produce child apps without any coding. These child apps act as the dedicated app for their (manager's) events.

## Development Setup
1. Go to the project repo and click the `Fork` button
2. Clone your forked repository : `https://github.com/prasang7/Pitch.git`
3. Move to android project folder `cd source-code`
4. Open the project with Android Studio

## How to build
All dependencies are defined in ```source-code/app/build.gradle```. Import the project in Android Studio or use Gradle in command line:
```
./gradlew assembleRelease
```
The result apk file will be placed in ```source-code/app/build/outputs/apk/```.
