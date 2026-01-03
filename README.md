# Blog Management System – Backend (Spring Boot)

A **production-ready, secure Blog Management System backend** developed using **Spring Boot**, implementing **RESTful APIs**, **role-based access control**, **JWT & OAuth2 authentication**, and **enterprise-level backend best practices**.
The system supports blog publishing, commenting, liking, email verification, email notifications, and admin-level user management.

---

## Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Data JPA (Hibernate)**
* **Spring Security**
* **JWT (JSON Web Token)**
* **OAuth2 (GitHub Authentication)**
* **H2 In-Memory Database**
* **Java Mail Sender**
* **JUnit 5**
* **Mockito**
* **Maven**
* **RESTful APIs**
* **Lombok**

---

## Core Features & Functionality

### Authentication & Security

* JWT-based authentication for stateless API security
* OAuth2 authentication using **GitHub login**
* Role-based access control using **roles and permissions**
* Fine-grained authorization using **authorities**
* Method-level security with `@PreAuthorize`
* Password encryption and secure authentication flow

---

### User Management

* Email verification using **Java Mail Sender** before allowing login
* Secure registration and login workflow
* Only **ADMIN** users can delete other users
* Centralized error handling using **Global Exception Handler**
* Custom exceptions for validation, authorization, and resource errors

---

### Blog, Comment & Like Management

* Users can:

  * Create, edit, and delete **their own blog posts**
  * Add, edit, and delete **their own comments**
  * Like a blog post **only once**
  * Unlike a post **only if they have previously liked it**
* Authorization rules enforced at API level:

  * Users cannot modify or delete others’ posts or comments
  * Users can unlike **only their own likes**

---

### Email Notifications

* Email verification during user registration
* Automatic email notification to the post owner when:

  * A new comment is added to their blog post

---

### Pagination & Performance

* Pagination implemented for:

  * Blog posts
* Efficient data retrieval using **Spring Data Pageable**
* Optimized entity relationships to avoid unnecessary database calls

---

### Database & JPA Implementation

* Used **H2 In-Memory Database** for rapid development and testing
* Implemented:

  * `@Transactional` for data consistency
  * Cascading operations
  * `orphanRemoval = true`
* Well-defined JPA entity relationships (One-to-Many, Many-to-One)
* Database-agnostic design (easy migration to MySQL/PostgreSQL)

---

### Testing

* Unit testing using **JUnit 5**
* Dependency mocking with **Mockito**
* Service-layer focused tests
* Improved reliability, maintainability, and code quality

---

### Exception Handling

* Centralized **Global Exception Handling**
* Custom exceptions for:

  * Resource not found
  * Email verification token exception
  * Expired jwt exception
* Consistent and meaningful API error responses

---

## Roles & Access Control

| Role  | Capabilities                                               |
| ----- | ---------------------------------------------------------- |
| USER  | Create/Edit/Delete own posts & comments, Like/Unlike posts |
| ADMIN | Delete any user, full system access                        |

---

## Future Enhancements

* Swagger / OpenAPI documentation
* Database migration to MySQL/PostgreSQL
* Dockerization
* Caching with Redis
* CI/CD pipeline integration
