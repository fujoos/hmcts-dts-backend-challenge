# HMCTS Dev Test Backend

Backend API for the HMCTS case management technical test. This service provides a REST API for managing caseworker tasks, including persistence, validation, error handling, and API documentation.

## Overview

This backend exposes a task management API for caseworkers. It supports the full task lifecycle, persists data to a database, applies validation rules, and provides interactive API documentation via Swagger UI.

## Features

- Create a task with:
  - title
  - optional description
  - status
  - due date and time
- Retrieve a task by ID
- Retrieve all tasks, sorted by due date and time
- Update a task
- Update task status only
- Delete a task
- Validation with `400 Bad Request` responses
- Consistent error responses using `ProblemDetail`
- Database persistence with Flyway migrations
- H2 database by default, with optional Postgres profile

## Tech Stack

- **Java 21** (Temurin / OpenJDK)
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **Flyway** for database migrations
- **H2** database for default local development
- **OpenAPI / Swagger UI** via `springdoc`
- **JUnit 5** and **MockMvc** for testing

## Prerequisites

- **Java:** 21.x
- **Gradle:** use the included wrapper:
  - `./gradlew`
  - `gradlew.bat`

## Configuration

The service runs on:

- `http://localhost:4000`

### Profiles

- `h2` — default
- `postgres` — optional

By default, the application uses an in-memory H2 database for local development.

## Getting Started

### 1. Build the application

```bash
./gradlew build