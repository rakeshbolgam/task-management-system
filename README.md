# Task Management System (Spring Boot)

A backend REST API application for managing tasks with secure authentication, role-based access, and scalable architecture.

##Overview

This project is designed to simulate a real-world task management system where:

* Users can create and manage tasks
* Admins can manage all users and tasks
* Secure APIs are implemented using JWT authentication

##Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Security (JWT Authentication)**
* **Spring Data JPA**
* **MySQL**
* **Maven**

## Key Features

* User Authentication & Authorization (JWT)
* Role-Based Access (ADMIN / USER)
* CRUD Operations for Tasks
* Pagination & Sorting
* DTO Pattern Implementation
* Global Exception Handling
* Clean layered architecture (Controller → Service → Repository)

## Project Structure

```
src/main/java/com/rakesh/task_manager
│
├── controller        # REST Controllers
├── service           # Interfaces
├── serviceimpl       # Business logic
├── repository        # JPA Repositories
├── entity            # Database models
├── dto               # Data Transfer Objects
├── security          # JWT + Security config
└── exception         # Global exception handling
```

## API Endpoints

### Authentication

* `POST /auth/register`
* `POST /auth/login`

### Tasks

* `POST /tasks` → Create task
* `GET /tasks` → Get all tasks (with pagination)
* `GET /tasks/{id}` → Get task by ID
* `PUT /tasks/{id}` → Update task
* `DELETE /tasks/{id}` → Delete task

## Configuration

Update your `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/task_db
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
```

## How to Run

```bash
git clone https://github.com/rakeshbolgam/task-management-system.git
cd task-management-system
mvn spring-boot:run
```

## Testing

Use Postman or any API client to test endpoints.

## (Optional – Add Screenshots)

*Add Postman screenshots here for better presentation*

## Highlights for Recruiters

* Implements secure REST APIs using JWT
* Follows clean architecture and best practices
* Handles pagination, sorting, and validation
* Demonstrates backend development skills with Spring Boot

## Author

**Rakesh Bolgam**

## If you like this project, give it a star!
