# Task & Notes Manager

A modern Android application built with Kotlin, Firebase, and MVVM architecture to manage tasks and notes efficiently. The app features user authentication, CRUD operations for tasks and notes, and a polished Material Design UI with Intent-based navigation.

## Features

- **User Authentication**:
  - Sign up and log in with email and password using Firebase Authentication
  - Automatic redirection to Main screen if already authenticated
  - Logout functionality via menu

- **Task Management**:
  - Create, Read, Update, Delete (CRUD) tasks
  - Each task includes a title, description, and completion status
  - Swipe-to-delete with confirmation dialog
  - Edit existing tasks with pre-filled data

- **Notes Management**:
  - Create, Read, Update, Delete (CRUD) notes
  - Each note includes a title and content
  - Swipe-to-delete with confirmation dialog
  - Edit existing notes with pre-filled data

- **UI Design**:
  - Material Design components (Toolbar, FABs, TextInputLayout, CardView)
  - Fixed-height RecyclerViews for tasks and notes with scrollable content
  - Clean, responsive layout with proper spacing and colors

- **Architecture**:
  - MVVM pattern with ViewModel and LiveData
  - Repository pattern for Firebase interactions
  - Intent-based navigation between activities

- **Data Storage**:
  - Firebase Firestore for storing user-specific tasks and notes
  - Real-time updates via snapshot listeners

## Screenshots

*(Add screenshots here once you capture them)*  
- Splash Screen  
- Login/Register Screens  
- Main Dashboard with Tasks and Notes  
- Add/Edit Task Screen  
- Add/Edit Note Screen  

## Prerequisites

- Android Studio (latest stable version recommended)
- Firebase project setup with Authentication and Firestore enabled
- An Android device/emulator running API 21 (Lollipop) or higher

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/task-notes-manager.git
   cd task-notes-manager
   ```

2. **Firebase Configuration**:
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Enable Email/Password Authentication in the Authentication section
   - Enable Firestore Database
   - Download `google-services.json` and place it in the `app/` directory

3. **Open in Android Studio**:
   - Open the project in Android Studio
   - Sync the project with Gradle files

4. **Build and Run**:
   - Connect an Android device or start an emulator
   - Click `Run > Run 'app'` in Android Studio

## Dependencies

Add these to your `app/build.gradle`:
```gradle
dependencies {
    // Firebase
    implementation platform('com.google.firebase:firebase-bom:32.7.0')
    implementation 'com.google.firebase:firebase-auth-ktx'
    implementation 'com.google.firebase:firebase-firestore-ktx'

    // Material Design
    implementation 'com.google.android.material:material:1.9.0'

    // ViewModel and LiveData
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.6.2"

    // RecyclerView and CardView
    implementation "androidx.recyclerview:recyclerview:1.3.2"
    implementation "androidx.cardview:cardview:1.0.0"

    // Coroutines
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"
}
```

Also, ensure you have the Google Services plugin:
```gradle
apply plugin: 'com.google.gms.google-services'
```

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/tasknotesapp/
│   │   │   ├── adapter/           # RecyclerView adapters
│   │   │   ├── model/             # Data models (TaskModel, NoteModel, User)
│   │   │   ├── repository/        # Firebase interaction logic
│   │   │   ├── ui/
│   │   │   │   ├── activity/      # Activities (Splash, Login, Register, Main, AddTask, AddNote)
│   │   │   ├── viewmodel/         # ViewModels for authentication and data management
│   │   ├── res/
│   │   │   ├── layout/            # XML layouts for UI
│   │   │   ├── menu/              # Menu resources
│   │   │   ├── values/            # Colors, styles, and strings
```

## Usage

1. **Launch the App**:
   - Starts with a splash screen (2-second delay)
   - Redirects to Login if not authenticated, or Main if authenticated

2. **Authentication**:
   - Register with email/password or log in with existing credentials
   - Redirects to Main screen upon successful authentication

3. **Main Dashboard**:
   - View tasks and notes in fixed-height RecyclerViews
   - Swipe right to delete (with confirmation)
   - Click "Edit" to modify existing items
   - Use FABs to add new tasks/notes
   - Logout via the toolbar menu

4. **Add/Edit Screens**:
   - Fill in fields to add new tasks/notes
   - Edit screens pre-populate with existing data
   - Save changes with the "Add" or "Update" button

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -m 'Add YourFeature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Built with Kotlin and Android Jetpack
- Powered by Firebase Authentication and Firestore
- Inspired by Material Design guidelines
