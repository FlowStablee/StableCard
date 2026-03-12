# Stable App (Flowstable Card Wallet)

Secure Android wallet for managing virtual and physical payment cards, built with **Kotlin**, **Jetpack Compose**, **Hilt**, **Retrofit**, and **Room**.

## Features

- **Authentication**: Email/password login, JWT storage, refresh token flow (backend‑driven).
- **Wallet dashboard**: Wallet balance, cards list, quick access to transactions.
- **Card management**: Virtual card creation hook, masked PAN, status (active/frozen/blocked).
- **Card controls**: Freeze/unfreeze, spending limit, online/international toggles.
- **Transactions**: List + basic amount filtering.
- **Security**: Android Keystore–backed encrypted storage, optional biometrics, screenshot blocking on card detail.
- **Notifications**: Firebase Cloud Messaging hook for transaction/limit alerts.
- **CI/CD**: GitHub Actions build that sends the release APK to a Telegram group.

## Tech Stack

- Kotlin, Coroutines
- Jetpack Compose (Material 3)
- MVVM with ViewModels
- Hilt for dependency injection
- Retrofit + Moshi
- Room Database
- DataStore + EncryptedSharedPreferences (Android Keystore)
- Firebase Cloud Messaging

## Project Structure

- `app/` – Android app module
  - `ui/` – Compose screens, theme, navigation
  - `viewmodel/` – MVVM ViewModels
  - `repository/` – Auth and wallet repositories
  - `network/` – Retrofit API and auth interceptor
  - `database/` – Room database and DAOs
  - `security/` – Token and secure storage helpers
  - `notifications/` – FCM service

## Local Development

1. Open the project in **Android Studio** (Giraffe/Flamingo+ recommended).
2. Make sure you have **Android SDK 26+** and **JDK 17** installed.
3. Build and run:

   ```bash
   ./gradlew assembleDebug
   ```

4. Install the resulting APK from `app/build/outputs/apk/debug/` on a device or emulator.

### Backend configuration

The app is backend‑agnostic but expects a fintech API capable of issuing cards (e.g. Visa/Mastercard via card‑issuing provider).

- Set your API base URL in `AppModule.provideApiService`:
  - `app/src/main/java/com/flowstable/cardwallet/di/AppModule.kt`
- Align the DTOs in `network/ApiService.kt` and `model/Models.kt` with your backend contracts.

### Firebase / push notifications

Firebase is optional for CI builds. The project currently **does not apply** the `com.google.gms.google-services` plugin so that GitHub Actions can build without a `google-services.json` file.

To fully enable Firebase Cloud Messaging:

1. Create a Firebase project and enable Cloud Messaging.
2. Download `google-services.json` into the `app/` module root.
3. Uncomment the `com.google.gms:google-services` classpath and `apply plugin: 'com.google.gms.google-services'` lines in the Gradle files.
4. Configure notification payloads so `FlowstableFirebaseMessagingService` can show useful alerts.

## GitHub Actions & Telegram APK Delivery

The workflow `.github/workflows/android-build-telegram.yml`:

- Runs on **push to `main`** or **manual dispatch**.
- Uses the custom `./gradlew` bootstrap script to:
  - Download a compatible Gradle distribution.
  - Build the **release APK** via `assembleRelease`.
- Uploads `app/build/outputs/apk/release/app-release.apk` as a build artifact.
- Sends that APK to a Telegram group using the Telegram Bot API.

### Required GitHub Secrets

In your repo, go to **Settings → Secrets and variables → Actions** and create:

- `TELEGRAM_BOT_TOKEN` – the bot token (do **not** commit it to the repo).
- `TELEGRAM_CHAT_ID` – the target group/channel chat ID.

Once configured, any successful push to `main` that builds the release APK will automatically send the latest **Stable App** APK to the configured Telegram group.

