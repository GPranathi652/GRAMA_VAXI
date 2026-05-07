# 🐄 Grama-Vaxi – Livestock Health Alert App

Grama-Vaxi is an Android app built with Kotlin + XML to help farmers register livestock, track vaccination schedules, and receive timely alerts even when the app is closed.

## Features
- Animal registration (name, breed, age)
- Local offline storage with Room
- Automatic next-vaccination calculation
- Reminder notifications via WorkManager
- Animal ledger in a simple list view

## Tech Stack
- Kotlin (Android)
- XML layouts
- Room Database
- WorkManager

## Project Structure
```
GRAMA-VAXI/
├── app/
│   └── src/main/
│       ├── java/com/gramavaxi/
│       │   ├── data/
│       │   ├── ui/
│       │   └── worker/
│       ├── res/layout/
│       └── AndroidManifest.xml
├── build.gradle.kts
└── settings.gradle.kts
```

## How it works
1. Register animal details.
2. App stores data locally in Room DB.
3. App computes a next vaccine date (+180 days).
4. WorkManager schedules a reminder notification.

## Future Enhancements
- Animal photo support
- Firebase cloud sync
- Kannada + English multilingual UI
- Location-based camp alerts
