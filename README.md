# Real-time Chat App

A production-ready Real-time Chat application consisting of an Android client and a Spring Boot backend, powered by Firebase and PostgreSQL.

## Architecture

*   **Android App**: Java, Firebase Realtime Database (Offline capable), Firebase Auth, FCM.
*   **Backend**: Spring Boot (Java), PostgreSQL, Firebase Admin SDK.

## Prerequisites

*   Java 17+
*   Docker & Docker Compose
*   Android Studio
*   Firebase Project (with Auth, Realtime Database, and FCM enabled)

## Setup Instructions

### 1. Database & Backend

1.  **PostgreSQL**:
    ```bash
    docker-compose up -d
    ```

2.  **Firebase Service Account**:
    *   Go to Firebase Console -> Project Settings -> Service Accounts.
    *   Generate a new private key.
    *   Save it as `serviceAccountKey.json` in `server/src/main/resources/`.

3.  **Run Backend**:
    ```bash
    cd server
    ./mvnw spring-boot:run
    ```

### 2. Android App

1.  Open `android-app` in Android Studio.
2.  Add `google-services.json` (from Firebase Console) to `android-app/app/`.
3.  Build and Run on an Emulator or Device.

## Features

*   **Real-time Messaging**: Uses Firebase Realtime Database with offline support.
*   **Message Status**: Sent -> Delivered -> Read.
*   **Search & Moderation**: Admin backend allows searching chats and reporting messages.
*   **Notifications**: FCM notifications for new messages.

## Security

*   **Backend**: Validates Firebase ID Tokens for secure endpoints. Uses Role-based access for Admin APIs.
*   **Database**: Security rules ensure privacy between users.
