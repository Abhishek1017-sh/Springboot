# Volunteer Management & Task Allocation System

This is the fully functional Spring Boot backend for the Volunteer Management System, developed for the hackathon.

## Features Included
1. **Core Matching Engine**: Match volunteer skills with tasks (`matchScore = matchedSkills / requiredSkills`).
2. **Scheduling Engine**: Conflict detection logic `(start1 < end2) AND (start2 < end1)` to prevent overlapping assignments.
3. **JWT Authentication**: Full role-based security (`ADMIN`, `VOLUNTEER`).
4. **Attendance System**: Check-in via location simulation, check-out with automatic total hour calculation.
5. **Certificate Generation**: Uses `iText7` to automatically generate a PDF upon check-out.

## Instructions to Run Locally

### 1. Database Setup
1. Ensure you have **PostgreSQL** installed and running on `localhost:5432`.
2. Create a database named `vms_db`.
3. The username/password is `postgres`/`postgres`.
   (You can modify this in `backend/src/main/resources/application.yml`).

### 2. Run Spring Boot Backend
1. Open your terminal and navigate to the `backend` directory.
2. Build the project using Maven:
   ```bash
   mvn clean install -DskipTests
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
   *The server will start on `http://localhost:8080`.*
   *The tables will be automatically created (`ddl-auto: update`).*

### 3. API Testing
Refer to the `docs/API_RESPONSES.md` file for example requests and payloads.

### 4. Mobile App (React Native)
1. Navigate to the `mobile` directory.
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run Expo:
   ```bash
   npx expo start
   ```
