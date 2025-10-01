# ExpensePilot Backend

ExpensePilot is a Spring Boot-based backend application for managing personal expenses with user authentication and role-based access control. This project provides RESTful APIs for user registration, login, and expense management, and is designed for extensibility and security.

## Table of Contents
- [Features](#features)
- [Data Modeling](#data-modeling)
- [Services](#services)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)
- [Setup & Running](#setup--running)
- [Security](#security)
- [Future Inspection](#future-inspection)

---

## Features
- User registration and login with encrypted passwords
- Role-based access control (USER, ADMIN, etc.)
- CRUD operations for expenses
- Global exception handling
- Secure endpoints using Spring Security

## Data Modeling

### UserEntity
- Represents an application user
- Fields: `id`, `username`, `password`, `roles` (list of Role)

### Role
- Represents a user role (e.g., USER, ADMIN)
- Fields: `id`, `name`

### Expense
- Represents an expense record
- Fields: `id`, `amount`, `description`, `date`, `user` (owner)

## Services

### AuthController
- Handles user registration and login
- Validates credentials and manages authentication context

### ExpenseController
- Manages CRUD operations for expenses
- Secured endpoints (only authenticated users can access)

### ExpenseService
- Business logic for expense management
- Interacts with ExpenseRepo for data persistence

### CustomUserDetailsService
- Loads user-specific data for authentication

### GlobalExceptionHandler
- Handles exceptions and provides meaningful error responses

## API Endpoints

### Authentication
- `POST /api/auth/register` — Register a new user
- `POST /api/auth/login` — Login and authenticate user

### Expenses
- `GET /api/expenses` — List all expenses for the authenticated user
- `POST /api/expenses` — Add a new expense
- `PUT /api/expenses/{id}` — Update an existing expense
- `DELETE /api/expenses/{id}` — Delete an expense

## Project Structure
```
src/main/java/com/project/expensepilot/
  controller/         # REST controllers (Auth, Expense, etc.)
  dto/                # Data Transfer Objects (LoginDto, RegisterDto)
  model/              # Entity models (UserEntity, Role, Expense)
  repo/               # Spring Data JPA repositories
  security/           # Security configuration and user details service
  service/            # Business logic services
resources/
  application.properties  # App configuration
  data.sql                # Initial data
```

## Setup & Running

1. **Clone the repository**
2. **Configure the database** in `src/main/resources/application.properties`
3. **Build the project**:
   ```
   ./mvnw clean install
   ```
4. **Run the application**:
   ```
   ./mvnw spring-boot:run
   ```
5. **API Documentation**: Use Postman or Swagger to test endpoints.

## Security
- Passwords are encrypted using `PasswordEncoder`
- Authentication managed by Spring Security
- Role-based access enforced in controllers

## Future Inspection
- Inspect `model/` for entity relationships and fields
- Review `controller/` for available endpoints and logic
- Check `service/` for business rules
- See `repo/` for data access patterns
- Security configuration is in `security/SecurityConfig.java`
- Exception handling is centralized in `controller/GlobalExceptionHandler.java`

---

For further details, inspect the codebase as per the structure above. Contributions and improvements are welcome!

