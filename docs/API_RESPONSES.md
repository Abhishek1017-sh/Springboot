# API Documentation & Responses

## 1. Authentication
`POST /api/auth/signup`
**Request:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "VOLUNTEER",
  "skills": ["Java", "Spring Boot"]
}
```
**Response (200 OK):**
```
User registered successfully
```

`POST /api/auth/login`
**Request:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```
**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "role": "ROLE_VOLUNTEER"
}
```

## 2. Volunteer Module (Requires VOLUNTEER Role)
`GET /api/volunteer/recommendations`
**Response (200 OK):**
```json
[
  {
    "taskId": 1,
    "taskTitle": "Backend Development",
    "eventId": 10,
    "matchScore": 100.0
  },
  {
    "taskId": 2,
    "taskTitle": "Database Design",
    "eventId": 10,
    "matchScore": 50.0
  }
]
```

`POST /api/volunteer/apply`
**Request:**
```json
{
  "taskId": 1
}
```
**Response (200 OK):**
```
Successfully applied for task
```
*(409 Conflict if scheduling overlap exists)*

## 3. Admin Module (Requires ADMIN Role)
`POST /api/admin/task`
**Request:**
```json
{
  "eventId": 1,
  "title": "Backend Development",
  "description": "Develop Spring Boot API",
  "startTime": "2026-05-10T10:00:00",
  "endTime": "2026-05-10T14:00:00",
  "requiredSkills": ["Java", "Spring Boot"]
}
```
**Response (200 OK):**
```json
{
  "id": 1,
  "eventId": 1,
  "title": "Backend Development",
  "description": "Develop Spring Boot API",
  "startTime": "2026-05-10T10:00:00",
  "endTime": "2026-05-10T14:00:00",
  "skills": ["Java", "Spring Boot"]
}
```

`POST /api/admin/assign`
**Request:**
```json
{
  "volunteerId": 5,
  "taskId": 1
}
```
**Response (200 OK):**
```
Successfully assigned volunteer to task
```
*(409 Conflict if scheduling overlap exists)*

## 4. Attendance
`POST /api/attendance/check-in`
**Request:**
```json
{
  "taskId": 1,
  "location": "Event Hall A"
}
```
**Response (200 OK):**
```
Checked in successfully
```

`POST /api/attendance/check-out`
**Request:**
```json
{
  "taskId": 1
}
```
**Response (200 OK):**
Returns a `.pdf` file of the generated certificate.
