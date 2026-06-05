# Health Seed Tool 🧬🌱

A standalone developer tool for Android designed to seed **Android Health Connect** with realistic health data for testing and debugging purposes.

## 🚀 Goal
Facilitate the testing of applications that consume data from Health Connect by allowing quick generation of up to 90 days of simulated data history, eliminating the need to generate data manually through exercise or other apps.

## 🛠 Tech Stack
- **Language:** Kotlin
- **Architecture:** Single Activity
- **UI:** ViewBinding & Material Design
- **Concurrency:** Kotlin Coroutines
- **Health SDK:** `androidx.health.connect:connect-client:1.1.0-alpha12`
- **Minimum Version:** Android 8.0 (API 26)
- **Target SDK:** 35

## ✨ Features
- **SDK Status Check:** Automatically verifies if Health Connect is available or requires an update.
- **Permission Management:** Integrated flow to request necessary Health Connect write permissions.
- **Customizable Seeding:** Individual buttons to populate specific data categories.
- **Bulk Seed:** A single button to populate all categories at once (90 days of data).
- **Clear All Data:** Button to delete all records created by the tool within the last 90-day window.
- **Real-time Logs:** Visual feedback area showing insertion progress.

## 📊 Supported Data
The tool generates realistic variations (using `Random`) for:
1.  **Steps:** 4,000 to 14,000 steps per day.
2.  **Heart Rate:** Samples every 2 hours (58 to 110 BPM).
3.  **Sleep:** Nightly sessions between 5.5h and 8.5h.
4.  **Calories Burned:** 1,800 to 3,200 kcal per day.
5.  **Weight:** Daily fluctuations between 68.0kg and 75.0kg.
6.  **Exercises:** 4 sessions per week (Running and Strength Training).

## 🚀 How to Use
1.  Clone the repository:
    ```bash
    git clone git@github.com:HugoMadMatos/AndroidHealthPopulate.git
    ```
2.  Open the project in **Android Studio**.
3.  Build and install on your physical device or emulator with Health Connect.
4.  In the app, click **Seed Data** (or an individual button).
5.  Grant permissions in the Health Connect screen (toggle "Allow All").
6.  Monitor progress in the log area.

## 📄 License
This project was developed for developer tool purposes. Feel free to fork and modify as needed.
