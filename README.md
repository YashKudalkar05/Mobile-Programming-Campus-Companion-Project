

# Lakehead Buddy App

Welcome to the Lakehead Buddy App! This application is designed to enhance your campus experience by providing features such as viewing events, joining study groups, and navigating the campus with ease. It also includes user authentication via Firebase.

## Features

- **View Events:** Browse through upcoming events on campus.
- **Study Groups:** Find and join study groups to collaborate with peers.
- **Campus Navigation:** Easily navigate the campus.
- **User Profile:** View and manage your profile information.
- **User Authentication:** Secure login and signup using Firebase.

## Screenshots



## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/YashKudalkar05/Mobile-Programming-Campus-Companion-Project.git
    ```

2. Open the project in Android Studio.

3. Add your Firebase configuration file (`google-services.json`) to the `app` directory.

4. Build and run the project on an emulator or physical device.

## Configuration

### Firebase Setup

1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Create a new project or use an existing one.
3. Add an Android app to your project.
4. Follow the instructions to download the `google-services.json` file.
5. Place the `google-services.json` file in the `app` directory of your project.

### Dependencies

Make sure the following dependencies are added in your `build.gradle` (Module: app) file:

```groovy
dependencies {
    implementation 'com.google.firebase:firebase-auth:21.0.1'
    implementation 'com.google.firebase:firebase-firestore:23.0.3'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    // Other dependencies
}
```

## Usage

### Authentication

The app provides Firebase Authentication for user login and signup.

#### Signup

Users can sign up by providing their email, password, student ID, program, and name.

#### Login

Users can log in using their registered email and password.

### Navigation

The app uses a `BottomNavigationView` to navigate between different sections:

- **Home**
- **Profile**
- **Log Out**

### Logout

Users can log out by clicking the logout button in the bottom navigation bar.

## Code Structure

### Activities

- `AddEventActivity.kt`: Handles adding new events.
- `EventsActivity.kt`: Displays a list of upcoming events.
- `LoginActivity.kt`: Handles user login.
- `MainActivity.kt`: The main activity that hosts the bottom navigation.
- `NavigationActivity.kt`: Provides campus navigation.
- `ProfileActivity.kt`: Displays user profile information.
- `SignupActivity.kt`: Handles user signup.
- `StudyGroupsActivity.kt`: Displays a list of study groups.

### Adapters

- `EventAdapter.kt`: Adapter for displaying event items.
- `StudyGroupAdapter.kt`: Adapter for displaying study group items.

### Layouts

- `activity_events.xml`: The events activity layout.
- `activity_login.xml`: The login activity layout.
- `activity_main.xml`: The main activity layout.
- `activity_navigation.xml`: The navigation activity layout.
- `activity_profile.xml`: The profile activity layout.
- `activity_signup.xml`: The signup activity layout.
- `activity_study_groups.xml`: The study groups activity layout.
- `item_event.xml`: Layout for an individual event item.
- `item_study_group.xml`: Layout for an individual study group item.
- `new_event.xml`: Layout for adding a new event.

### Menu

- `bottom_nav_menu.xml`: The bottom navigation menu.

### Styles

- `styles.xml`: Defines the overall theme and styles for the app.
- `colors.xml`: Defines the color scheme for the app.
