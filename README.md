# Driving School Management Information System

A comprehensive full-stack application for managing all aspects of a driving school, including user roles, course sessions, appointments, vehicle management, document handling, and security.

---

## Table of Contents

* [Introduction](#introduction)
* [Features](#features)
* [Technology Stack](#technology-stack)
* [Project Structure](#project-structure)
* [Prerequisites](#prerequisites)
* [Setup & Installation](#setup--installation)
* [Configuration](#configuration)
* [Running the Application](#running-the-application)
* [API Endpoints](#api-endpoints)
* [Design Patterns](#design-patterns)
* [Future Enhancements](#future-enhancements)
* [Contributing](#contributing)
* [License](#license)

---

## Introduction

This project implements a **Driving School Management Information System**. It provides distinct user experiences for **Admin**, **Employee**, **Instructor**, and **Student** roles, enabling:

* User registration, authentication & authorization
* Management of theoretical, simulation, and practical driving sessions
* Appointment booking and status updates
* Vehicle and license-class CRUD
* Document upload/download for students
* Secure JWT-based API access

---

## Features

1. **User Management** (Admin/Employee/Instructor/Student)
2. **Course Session Management** (create, activate/deactivate, assign students)
3. **Appointment System** (booking, updating, canceling)
4. **Vehicle & License Class CRUD**
5. **Document Handling** (upload, list, download)
6. **Exams & Terms** management
7. **Role-Based Access Control** with Spring Security (JWT)
8. **RESTful API** with DTOs
9. **Frontend Application** in React + TypeScript

---

## Technology Stack

* **Backend**: Java 17, Spring Boot, Spring Security (JWT), Spring Data JPA/Hibernate, ModelMapper
* **Frontend**: React 18, TypeScript, Vite, Tailwind CSS, Axios
* **Database**: MySQL
* **Build & Tools**: Maven, npm/yarn, Postman, Git/GitHub

---

## Project Structure

```text
drivingschool-backend/
├── src/main/java/dev/emir/DrivingSchoolManagementInformation
│   ├── api               # REST controllers
│   ├── service           # Business logic interfaces & implementations
│   ├── dao               # Spring Data JPA repositories
│   ├── models            # JPA entities
│   ├── dto               # Request/Response DTOs
│   ├── config            # Security & mapping configuration
│   └── security          # JWT filters & utils
└── src/main/resources
    └── application.yml   # Application configuration

frontend/
├── src
│   ├── api               # Axios instance & interceptors
│   ├── services          # API service wrappers
│   ├── context           # React Contexts (Auth, Search)
│   ├── components        # Reusable UI components
│   ├── pages             # Route views
│   └── hooks             # Custom hooks
└── vite.config.ts        # Vite configuration
```

---

## Prerequisites

* Java 17+
* Maven 3.6+
* Node.js 16+ and npm/yarn
* MySQL server

---

## Setup & Installation

1. **Clone repository**

   ```bash
   git clone https://github.com/bobosii/DrivingSchoolManagementInformation.git
   cd DrivingSchoolManagementInformation
   ```

2. **Backend**

   * Configure database credentials in `application.yml`
   * Build and run:

     ```bash
     cd drivingschool-backend
     mvn clean install
     mvn spring-boot:run
     ```

3. **Frontend**

   ```bash
   cd frontend
   npm install
   npm run dev
   ```

---

## Configuration

Edit `src/main/resources/application.yml` (backend) to set:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/drivingschooldb
    username: <DB_USER>
    password: <DB_PASS>
jwt:
  secret: <YOUR_SECRET_KEY>
  expiration: 86400000
```

On the frontend, create a `.env` at project root:

```
VITE_API_BASE_URL=http://localhost:8080/api
```

---

## Running the Application

* **Backend**: `mvn spring-boot:run` (default port 8080)
* **Frontend**: `npm run dev` (default port 5173)

Open `http://localhost:5173` in your browser.

---

## API Endpoints

Refer to the generated Swagger/OpenAPI docs (if enabled) or Postman collection for complete list.

Examples:

* `POST /api/auth/register`
* `POST /api/auth/login`
* `GET /api/course-sessions`
* `POST /api/appointments`

---

## Design Patterns

* **Layered Architecture** (Controller → Service → Repository)
* **Repository Pattern** (Spring Data JPA)
* **Service Layer Pattern**
* **DTO Pattern**
* **Dependency Injection (IoC)**
* **Singleton** (Spring Beans)
* **Interceptor** (JWT filter)
* **Factory/Builder** (ModelMapper bean)
* **Context & Hook** (React global state)
* **Module Pattern** (organizing frontend folders)

---

## Future Enhancements

* Real-time **Notifications** (WebSocket)
* **Analytic Dashboard** (charts & reports)
* **Payment Integration** (Stripe, PayPal)
* **Mobile Client** (React Native / Flutter)
* **Internationalization** (i18n)

---

## Contributing

1. Fork repository
2. Create feature branch
3. Commit & push changes
4. Open Pull Request

Please follow the existing code style and include tests where appropriate.

---

## License

This project is licensed under the MIT License.

