# 🎓 Campus Placement Management System — Backend

Spring Boot 3.2 REST API with JWT authentication, role-based access control, and **zero Lombok dependency**.  
All classes use plain Java constructors, getters/setters, and hand-written builders.

---

## 🏗️ Tech Stack

| Layer       | Technology                          |
|-------------|-------------------------------------|
| Framework   | Spring Boot 3.2.5 · Java 17         |
| Security    | Spring Security + JWT (jjwt 0.11.5) |
| Persistence | Spring Data JPA + Hibernate         |
| Database    | H2 (dev) / MySQL (prod)             |
| Build       | Maven                               |
| Utilities   | **No Lombok** – plain Java          |

---

## 📁 Project Structure

```
src/main/java/com/placement/
├── config/
│   ├── SecurityConfig.java           # Spring Security, CORS, bean wiring
│   └── GlobalExceptionHandler.java   # Centralised error responses
├── controller/
│   ├── AuthController.java           # /api/auth/**
│   ├── StudentController.java        # /api/students/**
│   ├── CompanyController.java        # /api/companies/**
│   ├── JobController.java            # /api/jobs/**
│   └── ApplicationController.java   # /api/applications/**
├── service/                          # Business logic layer
├── repository/                       # Spring Data JPA interfaces
├── entity/                           # JPA entities with built-in builders
├── dto/                              # LoginRequest · LoginResponse · RegisterRequest
├── security/
│   ├── JwtService.java               # Token generation & validation
│   ├── JwtAuthFilter.java            # OncePerRequestFilter
│   └── CustomUserDetailsService.java
└── PlacementApplication.java
```

---

## 🚀 Quick Start

### Run with H2 (zero setup)

```bash
mvn spring-boot:run
```

| Resource     | URL                                          |
|--------------|----------------------------------------------|
| API base     | `http://localhost:8080`                      |
| H2 Console   | `http://localhost:8080/h2-console`           |
| JDBC URL     | `jdbc:h2:mem:placementdb`                    |
| H2 User/Pass | `sa` / *(blank)*                             |

### Switch to MySQL (production)

In `application.properties`, comment out the H2 block and uncomment:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/placement_db?useSSL=false&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

---

## 🔐 Authentication

Every endpoint except `/api/auth/**` requires:

```
Authorization: Bearer <token>
```

### Register (STUDENT example)

```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "alice@student.com",
  "password": "secret123",
  "role": "STUDENT",
  "firstName": "Alice",
  "lastName": "Johnson",
  "rollNumber": "CS2024001",
  "department": "Computer Science",
  "cgpa": 8.5,
  "yearOfGraduation": 2025,
  "phone": "9876543210",
  "skills": "Java,Spring Boot,React"
}
```

For `COMPANY` role, use instead: `companyName`, `industry`, `website`, `contactPerson`, `location`.

### Login

```http
POST /api/auth/login
Content-Type: application/json

{ "email": "alice@student.com", "password": "password" }
```

Response:

```json
{
  "token": "eyJhbGci...",
  "tokenType": "Bearer",
  "email": "alice@student.com",
  "role": "STUDENT",
  "userId": 2,
  "message": "Login successful"
}
```

---

## 📡 API Reference

### Auth  `/api/auth`
| Method | Path        | Auth   |
|--------|-------------|--------|
| POST   | `/register` | Public |
| POST   | `/login`    | Public |
| GET    | `/ping`     | Public |

### Students  `/api/students`
| Method | Path                                  | Role(s)        |
|--------|---------------------------------------|----------------|
| GET    | `/me`                                 | STUDENT        |
| PUT    | `/me`                                 | STUDENT        |
| GET    | `/`                                   | ADMIN, COMPANY |
| GET    | `/{id}`                               | ADMIN, COMPANY |
| GET    | `/department/{dept}`                  | ADMIN, COMPANY |
| GET    | `/eligible?minCgpa=7.5&department=CS` | ADMIN, COMPANY |
| GET    | `/placed`                             | ADMIN          |
| GET    | `/unplaced`                           | ADMIN          |
| GET    | `/stats`                              | ADMIN          |
| PUT    | `/{id}`                               | ADMIN          |
| PATCH  | `/{id}/mark-placed`                   | ADMIN          |
| DELETE | `/{id}`                               | ADMIN          |

### Companies  `/api/companies`
| Method | Path              | Role(s)        |
|--------|-------------------|----------------|
| GET    | `/me`             | COMPANY        |
| PUT    | `/me`             | COMPANY        |
| GET    | `/`               | All auth       |
| GET    | `/{id}`           | All auth       |
| GET    | `/industry/{ind}` | All auth       |
| PUT    | `/{id}`           | ADMIN          |
| DELETE | `/{id}`           | ADMIN          |

### Jobs  `/api/jobs`
| Method | Path                   | Role(s)        |
|--------|------------------------|----------------|
| GET    | `/`                    | All auth       |
| GET    | `/active`              | All auth       |
| GET    | `/{id}`                | All auth       |
| GET    | `/company/{companyId}` | All auth       |
| GET    | `/eligible?cgpa=8.0`   | STUDENT        |
| GET    | `/my-jobs`             | COMPANY        |
| POST   | `/`                    | COMPANY        |
| PUT    | `/{id}`                | COMPANY, ADMIN |
| PATCH  | `/{id}/status`         | COMPANY, ADMIN |
| DELETE | `/{id}`                | COMPANY, ADMIN |

`PATCH /{id}/status` body: `{ "status": "CLOSED" }` — valid values: `OPEN`, `CLOSED`, `DRAFT`

### Applications  `/api/applications`
| Method | Path                | Role(s)        |
|--------|---------------------|----------------|
| GET    | `/`                 | ADMIN          |
| GET    | `/{id}`             | ADMIN, COMPANY |
| DELETE | `/{id}`             | ADMIN          |
| GET    | `/my-applications`  | STUDENT        |
| POST   | `/apply/{jobId}`    | STUDENT        |
| PATCH  | `/{id}/withdraw`    | STUDENT        |
| GET    | `/company`          | COMPANY        |
| GET    | `/job/{jobId}`      | COMPANY, ADMIN |
| PATCH  | `/{id}/status`      | COMPANY, ADMIN |

`PATCH /{id}/status` body: `{ "status": "SHORTLISTED", "feedback": "Great profile." }`

**Status flow:**  
`APPLIED → UNDER_REVIEW → SHORTLISTED → INTERVIEW_SCHEDULED → SELECTED | REJECTED`  
Student may `WITHDRAWN` before `SELECTED`. Selecting an application auto-marks the student as placed.

---

## 👤 Seed Data

| Email                  | Password   | Role    |
|------------------------|------------|---------|
| admin@placement.com    | `password` | ADMIN   |
| alice@student.com      | `password` | STUDENT |
| bob@student.com        | `password` | STUDENT |
| hr@google.com          | `password` | COMPANY |
| hr@microsoft.com       | `password` | COMPANY |

3 jobs pre-loaded (2 Google, 1 Microsoft) ready for testing.

---

## 🧪 Quick cURL Test

```bash
# 1. Login and extract token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"alice@student.com","password":"password"}' \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['token'])")

# 2. View active jobs
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/jobs/active

# 3. Apply for job 1
curl -s -X POST http://localhost:8080/api/applications/apply/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"coverLetter":"I am excited to apply for this role!"}'
```

---

## ⚙️ Key Configuration

```properties
# Change this in production!
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000   # 24 hours in milliseconds
```

> **Note:** Spring Boot 5.0 does not exist yet — this project uses **Spring Boot 3.2.5**,
> which is the latest stable release built on Spring Framework 6 and Jakarta EE 10.
