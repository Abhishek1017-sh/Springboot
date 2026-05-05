# Volunteer Management System - Mobile App

This is a React Native app built with Expo for the Volunteer Management System.

## Features
- **Login/Signup**: JWT-based authentication.
- **Task Dashboard**: Browse available tasks matched to your skills.
- **Task Details**: View specific task information and apply.
- **Check-in System**: Dual-mode attendance tracking:
  - **QR Scan**: Scan the event QR code for instant check-in.
  - **Geo-location**: fallback/verification using GPS coordinates.
- **Profile**: Track your volunteering hours and earned badges.

## Tech Stack
- React Native (Expo)
- React Navigation
- Axios (API integration)
- Expo Barcode Scanner
- Expo Location
- Expo Secure Store

## Getting Started

1. **Install dependencies**:
   ```bash
   cd mobile
   npm install
   ```

2. **Set API URL**:
   Update the `API_BASE_URL` in `src/api/apiClient.js` to point to your backend (e.g., `http://YOUR_IP:8080`).

3. **Run the app**:
   ```bash
   npx expo start
   ```

## Check-in Logic
The app prioritizes QR scanning for check-in. If the user chooses or if QR fails, it uses the device's GPS to verify proximity to the event location before sending the check-in request to the backend.
