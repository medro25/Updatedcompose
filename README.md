# 📱 ComposeTutorial - Jetpack Compose App

This is a **Jetpack Compose Android application** that implements various functionalities including **Room Database, Camera Capture, Audio Recording, Google Maps, and Notifications**. The app is built using **Jetpack Compose UI**, **Room Database** for local storage, and **Android Jetpack components** for modern app architecture.

---

## 🚀 Features Implemented

### ✅ **1. User Messages with Room Database**
- Users can **add, store, and view messages** with a **profile image**.
- **Room Database** is used for persistent storage.
- LazyColumn ensures smooth scrolling of stored messages.

### ✅ **2. Camera Capture & Image Storage**
- Users can **capture images using the device camera**.
- Images are **stored locally** and retrieved in a **horizontal scrollable list**.
- Uses **FileProvider** to securely store and share images.

### ✅ **3. Audio Recording & Playback**
- Users can **record and play audio files**.
- Audio files are stored in the **device’s storage**.
- Uses **MediaRecorder** for recording and **MediaPlayer** for playback.

### ✅ **4. Google Maps Integration**
- The app includes a **Google Maps screen** to display the user’s location.
- Location permissions are checked before accessing the map.
- Uses `com.google.android.gms:play-services-maps`.

### ✅ **5. Notifications Handling**
- The app **requests notification permissions on Android 13+**.
- The request **appears only AFTER the splash screen**.
- Uses `ActivityResultContracts.RequestPermission()` to handle permissions.

### ✅ **6. Splash Screen**
- The app **starts with a Splash Screen** before loading the Home Screen.
- Ensures a **smooth transition** before showing app content.

### ✅ **7. Bottom Navigation Bar**
- A **persistent bottom navigation bar** for quick access to screens.
- Icons and labels for **Home, Add Entry, Map, Audio, and Camera**.

---


## 📂 Project Structure

```
📦 **com.example.composetutorial**
├── 📂 **Database**  
│   ├── 📜 AppDatabase.kt             - Room Database setup  
│   ├── 📜 UserMessage.kt             - Data model  
│   ├── 📜 UserMessageDao.kt          - Data access object (DAO)  
│   ├── 📜 UserMessageRepository.kt   - Repository layer  
│  
├── 📂 **viewmodel**  
│   ├── 📜 UserMessageViewModel.kt    - ViewModel for managing database operations  
│  
├── 📂 **screens**  
│   ├── 📜 HomeScreen.kt              - Displays stored messages  
│   ├── 📜 AddEntryScreen.kt          - Allows adding new messages with images  
│   ├── 📜 CameraScreen.kt            - Handles camera functionality  
│   ├── 📜 AudioScreen.kt             - Handles audio recording and playback  
│   ├── 📜 MapScreen.kt               - Displays Google Maps  
│   ├── 📜 SplashScreen.kt            - Displays splash screen  
│  
├── 📜 **MainApp.kt**                 - Handles navigation and app structure  
├── 📜 **MainActivity.kt**            - Entry point for the app  
├── 📜 **BottomNavigationBar.kt**     - Bottom navigation bar  
```



---

## 🛠️ Tech Stack
- **Kotlin** - Main programming language.
- **Jetpack Compose** - Declarative UI framework.
- **Room Database** - Local database for storing user messages.
- **Coroutines & Flow** - Asynchronous operations.
- **MediaRecorder & MediaPlayer** - Audio recording & playback.
- **Google Maps API** - Location-based features.
- **FileProvider** - Secure image storage.
- **ViewModel & LiveData** - MVVM architecture.

---

## 🏗️ Setup & Installation

1. **Clone the repository**:
   ```sh
   git clone https://github.com/medro25/Updatedcompose.git
  


