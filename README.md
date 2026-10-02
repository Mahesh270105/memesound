# MemeSound Android App

A simple Android soundboard that previews sounds and shares them through the Android share sheet, including WhatsApp.

## Current state

The project contains one generated `Test Sound` so the APK can be built immediately.

To add your own sounds:

1. Put owned or properly licensed `.wav`/supported audio files in:
   `app/src/main/res/raw/`
2. Use lowercase filenames with underscores, for example:
   `vine_boom.wav`
3. Add an entry in `MainActivity.kt`:
   `MemeSound("Vine Boom", R.raw.vine_boom)`

## GitHub Actions

The workflow installs Gradle 8.7 and Java 17 and builds:

`gradle assembleDebug`

The generated APK is uploaded as the `meme-sound-debug-apk` artifact.

## Important

Do not redistribute copyrighted audio clips unless you have the necessary rights or permission.
