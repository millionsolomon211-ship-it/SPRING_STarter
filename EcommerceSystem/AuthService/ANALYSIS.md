# Auth Service Implementation & Analysis

## 1. Architectural Overview
The Auth Service is built using **Hexagonal Architecture** (also known as Ports and Adapters). This ensures that the business logic is decoupled from infrastructure concerns like databases, messaging queues, and web frameworks.

### Layers:
*   **Domain Layer**: Pure business logic (User entity, Domain services).
*   **Application Layer**: Use cases (Register, Login, Refresh) and Port definitions.
*   **Infrastructure Layer**: Adaptors for JPA, Security, and RabbitMQ.
*   **API Layer**: REST Controllers (the entry point).

---

## 2. Key Libraries & Technologies
Here are the libraries and tools used in this implementation:

| Library | Purpose |
| :--- | :--- |
| **Spring Boot 3.2.5** | Base framework for the microservice. |
| **Spring Web** | REST API development and MVC support. |
| **Spring Data JPA** | ORM for database interaction. |
| **PostgreSQL** | Relational database for user persistence. |
| **Spring Security** | Used specifically for **BCrypt** password hashing. |
| **JJWT (io.jsonwebtoken)** | Generation and validation of JSON Web Tokens. |
| **Spring AMQP (RabbitMQ)** | Publishing events (`UserRegistered`, `UserLoggedIn`) to other services. |
| **Lombok** | Boilerplate reduction (Getters, Builders, etc.). |
| **MapStruct** | Efficient mapping between Entities and DTOs. |

---

## 3. How it Works (Analysis)

### A. Authentication Flow
1.  **Registration**:
    *   API receives `RegisterUserRequest`.
    *   Application checks if email already exists via `UserRepository`.
    *   Password is encrypted using `BCrypt` via `PasswordEncoderPort`.
    *   User is saved to PostgreSQL.
    *   A `UserRegisteredEvent` is sent to RabbitMQ.
    *   A JWT token is returned to the user.

2.  **Login**:
    *   API receives `LoginRequest`.
    *   Application finds user by email.
    *   Matches password hash via `PasswordEncoderPort`.
    *   If successful, a `UserLoggedInEvent` is published.
    *   A new JWT token is generated and returned.

### B. Security & Tokenization
The service uses **JWT** for stateless authentication.
*   **JwtUtil**: Handles secret keys, expiration, and clinical claims.
*   **JwtProviderAdapter**: Implements the application port to bridge logical token creation with the library specific implementation.

### C. Event Driven Architecture
The Auth service doesn't just manage users; it informs the rest of the system.
*   **RabbitMQ**: Used as the message broker.
*   **Events**: Other services (like `NotificationService`) listen for `UserRegisteredEvent` to send welcome emails.

---

## 4. API Documentation
The API is exposed at `/api/auth`:
*   `POST /register`: Creates a new account.
*   `POST /login`: Validates credentials and returns a token.
*   `POST /refresh`: Issues a new token if the current one is valid.

---

## 5. Recent Improvements
*   **Global Exception Handling**: Added a robust error handling mechanism to ensure consistent JSON error responses for `400`, `401`, and `500` status codes.
*   **Clean Separation**: Ensured that the `Application` layer only talks to interfaces (Ports), allowing us to swap the database or messaging provider without changing business code.
