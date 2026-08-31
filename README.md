# 🚖 Uber Review Service

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Data JPA](https://img.shields.io/badge/Spring%20Data-JPA-blue.svg)](https://spring.io/projects/spring-data-jpa)
[![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1.svg)](https://www.mysql.com/)
[![Flyway](https://img.shields.io/badge/Migration-Flyway-CC0200.svg)](https://flywaydb.org/)
[![Gradle](https://img.shields.io/badge/Build-Gradle-02303A.svg)](https://gradle.org/)

A backend microservice for the **Uber System Architecture** responsible for managing ride reviews, driver feedback, and passenger ratings. Built with **Java 21**, **Spring Boot 3.5.5**, and **Spring Data JPA**.

---

## 📖 Table of Contents

- [Overview](#-overview)
- [Key Features & Architectural Patterns](#-key-features--architectural-patterns)
- [Technology Stack](#-technology-stack)
- [Project Architecture & Directory Structure](#-project-architecture--directory-structure)
- [REST API Reference](#-rest-api-reference)
- [Database & Shared Entity Ecosystem](#-database--shared-entity-ecosystem)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Configuration](#configuration)
  - [Build and Run](#build-and-run)
- [Running Tests](#-running-tests)

---

## 🌟 Overview

The **Uber Review Service** is a dedicated microservice in the Uber backend ecosystem designed to:
- Collect ratings and feedback submitted for completed ride bookings.
- Maintain auditing metadata (`createdAt`, `updatedAt`) via Spring Data JPA Auditing.
- Provide CRUD capabilities and rich querying interfaces for review analytics (rating thresholds, time-based queries, booking associations).
- Decouple review operations from the core booking and dispatch systems while referencing centralized domain models.

---

## 🏗️ Key Features & Architectural Patterns

- **Layered Microservice Architecture**: Clean separation of concerns between Controllers, Services, Repositories, Adapters, and DTOs.
- **Adapter Design Pattern**: Implements [`CreateReviewDtoToReviewAdaptor`](file:///d:/IdeaProjects/UberReviewService/UberReviewService/src/main/java/com/example/UberReviewService/adapters/CreateReviewDtoToReviewAdaptor.java) to decouple HTTP request payloads ([`CreateReviewDto`](file:///d:/IdeaProjects/UberReviewService/UberReviewService/src/main/java/com/example/UberReviewService/dtos/CreateReviewDto.java)) from internal database models ([`Review`](file:///d:/IdeaProjects/UberReviewService/UberReviewService/src/main/java/com/example/UberReviewService/controllers/ReviewController.java#L7)), resolving relational entities (e.g., [`Booking`](file:///d:/IdeaProjects/UberReviewService/UberReviewService/src/main/java/com/example/UberReviewService/repositories/BookingRepository.java)) seamlessly during transformation.
- **Shared Entity Service Integration**: Utilizes a shared library dependency (`UberProject-EntityService`) with cross-service entity scanning (`@EntityScan`).
- **JPA Auditing**: Automatically tracks creation and update timestamps across entities with `@EnableJpaAuditing`.
- **Unit Testing with Mockito & JUnit 5**: Comprehensive unit tests covering controller and service layers using Mockito mocks (`@ExtendWith(MockitoExtension.class)`).

---

## 💻 Technology Stack

| Component | Technology / Version |
| :--- | :--- |
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.5.5 |
| **Persistence** | Spring Data JPA / Hibernate |
| **Database** | MySQL 8.x (`mysql-connector-j`) |
| **Database Migrations** | Flyway (`flyway-core`, `flyway-mysql`) |
| **Build Tool** | Gradle |
| **Monitoring** | Spring Boot Actuator |
| **Boilerplate Reduction** | Project Lombok |
| **Testing** | JUnit 5, Mockito 5.11 |

---

## 📂 Project Architecture & Directory Structure

```
UberReviewService/
├── src/
│   ├── main/
│   │   ├── java/com/example/UberReviewService/
│   │   │   ├── adapters/                  # Adaptor pattern implementations for DTO -> Entity conversion
│   │   │   │   ├── CreateReviewDtoToReviewAdaptor.java
│   │   │   │   └── CreateReviewDtoToReviewAdaptorImpl.java
│   │   │   ├── controllers/               # REST Controller endpoints
│   │   │   │   └── ReviewController.java
│   │   │   ├── dtos/                      # Data Transfer Objects (Request/Response contracts)
│   │   │   │   ├── CreateReviewDto.java
│   │   │   │   └── ReviewDto.java
│   │   │   ├── repositories/              # Spring Data JPA repositories
│   │   │   │   ├── BookingRepository.java
│   │   │   │   ├── DriverRepository.java
│   │   │   │   └── ReviewRepository.java
│   │   │   ├── services/                  # Business logic layer
│   │   │   │   ├── ReviewService.java
│   │   │   │   └── ReviewServiceImpl.java
│   │   │   └── UberReviewServiceApplication.java # Spring Boot entry point
│   │   └── resources/
│   │       ├── application.properties     # App & Database configurations
│   │       └── db/                        # Flyway database migration scripts
│   └── test/
│       └── java/com/example/UberReviewService/
│           ├── ReviewControllerTest.java  # Mockito unit tests for ReviewController
│           └── UberReviewServiceApplicationTests.java
├── build.gradle                           # Gradle dependencies and build configuration
└── settings.gradle
```

---

## 🚀 REST API Reference

**Base URL**: `http://localhost:7474/api/v1/reviews`

### 1. Create a Review
Creates a new review associated with a specific booking.

- **Endpoint**: `POST /api/v1/reviews/create`
- **Content-Type**: `application/json`
- **Request Body**:
  ```json
  {
    "content": "Great ride! The driver was punctual and driving was smooth.",
    "rating": 4.8,
    "bookingId": 101
  }
  ```
- **Response**: `201 Created`
  ```json
  {
    "id": 1,
    "content": "Great ride! The driver was punctual and driving was smooth.",
    "booking": 101,
    "rating": 4.8,
    "createdAt": "2026-08-31T15:00:00.000+00:00",
    "updatedAt": "2026-08-31T15:00:00.000+00:00"
  }
  ```

---

### 2. Get Review by ID
Fetches an existing review by its unique identifier.

- **Endpoint**: `GET /api/v1/reviews/{id}`
- **Path Variable**: `id` (Long) - Review ID
- **Response**:
  - `200 OK` with Review object
  - `404 Not Found` if review does not exist

---

### 3. Get All Reviews
Fetches a list of all reviews in the system.

- **Endpoint**: `GET /api/v1/reviews`
- **Response**: `200 OK`
  ```json
  [
    {
      "id": 1,
      "content": "Great ride!",
      "rating": 4.8,
      "createdAt": "2026-08-31T15:00:00.000+00:00",
      "updatedAt": "2026-08-31T15:00:00.000+00:00"
    }
  ]
  ```

---

### 4. Update a Review
Updates the content and/or rating of an existing review.

- **Endpoint**: `PUT /api/v1/reviews/update?id={id}`
- **Query Parameter**: `id` (Long) - Review ID to update
- **Content-Type**: `application/json`
- **Request Body**:
  ```json
  {
    "content": "Updated review content",
    "rating": 5.0
  }
  ```
- **Response**: `200 OK` with updated Review details.

---

### 5. Delete a Review
Removes a review by its ID.

- **Endpoint**: `DELETE /api/v1/reviews/{id}`
- **Path Variable**: `id` (Long) - Review ID
- **Response**: `200 OK` with `true` (if deleted) or `false` (if not found/error).

---

## 🗄️ Database & Shared Entity Ecosystem

### Database Configuration
The service connects to a MySQL database configured in [`src/main/resources/application.properties`](file:///d:/IdeaProjects/UberReviewService/UberReviewService/src/main/resources/application.properties):
- **Port**: `7474`
- **Database URL**: `jdbc:mysql://localhost:3306/Uber_Db_local`
- **Hibernate DDL**: `validate`

### Shared Entity Service (`UberProject-EntityService`)
This service consumes shared core entities (`Review`, `Booking`, `Driver`, base models) published via the Maven local repository:
```groovy
implementation 'com.example:UberProject-EntityService:0.0.9-SNAPSHOT'
```
Entity packages are scanned at startup via:
```java
@EntityScan("com.example.uberprojectentityservice.models")
```

---

## 🛠️ Getting Started

### Prerequisites
1. **Java Development Kit (JDK) 21** or later installed.
2. **MySQL Server 8.x** running locally on port `3306`.
3. The shared entity library `UberProject-EntityService` installed in your local Maven repository (`~/.m2/repository`).

### Configuration
1. Create the MySQL database:
   ```sql
   CREATE DATABASE Uber_Db_local;
   ```
2. Update your credentials in [`src/main/resources/application.properties`](file:///d:/IdeaProjects/UberReviewService/UberReviewService/src/main/resources/application.properties):
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/Uber_Db_local
   spring.datasource.username=YOUR_MYSQL_USERNAME
   spring.datasource.password=YOUR_MYSQL_PASSWORD
   ```

### Build and Run

Inside the project directory:

```bash
# Build the project
./gradlew build

# Run the Spring Boot service
./gradlew bootRun
```

The application will start on port **`7474`**.

---

## 🧪 Running Tests

Unit tests are written with **JUnit 5** and **Mockito**. To run all tests:

```bash
./gradlew test
```
