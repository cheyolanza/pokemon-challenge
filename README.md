# Pokémon Challenge

Full-stack Pokémon management application developed with **Java 21, Spring Boot 4, React, TypeScript, PostgreSQL, and Docker**.

The application integrates with the public PokéAPI, allowing users to explore Pokémon and manage a local collection stored in PostgreSQL.

The backend follows Clean Architecture principles, with JWT-based authentication and RESTful APIs.

## 1. Features

### Pokémon Exploration
- Browse Pokémon retrieved from PokéAPI.
- View Pokémon details, including name, image, weight, and abilities.
- Paginated Pokémon listing.
- Public access without authentication.

### Pokémon Management
- Synchronize Pokémon from PokéAPI to PostgreSQL.
- View locally stored Pokémon.
- Edit Pokémon information.
- Delete Pokémon with confirmation.
- JWT authentication for write operations.

### User Authentication
- User registration.
- Login with email and password.
- JWT-based authentication.
- Protected frontend routes.
- Username displayed in the navigation bar.
- Logout functionality.
- Session management using sessionStorage.

## 2. Technology Stack

| Component | Technology |
|---|---|
| Backend | Java 21 |
| Framework | Spring Boot 4.0.8 |
| Security | Spring Security, JWT |
| Persistence | Spring Data JPA, Hibernate |
| Database | PostgreSQL 16 |
| Database Migration | Flyway |
| External API | PokéAPI |
| Frontend | React, TypeScript |
| Frontend Build | Vite |
| HTTP Client | Axios |
| Routing | React Router |
| Backend Build | Maven |
| Testing | JUnit, Spring Boot Test |
| Containerization | Docker, Docker Compose |
| Web Server | Nginx |

## 3. Project Structure

```text
pokemon-challenge/
├── compose.yaml
├── .env
├── .gitignore
├── README.md
│
├── pokemon-backend/
│   ├── Dockerfile
│   ├── pom.xml
│   ├── mvnw
│   ├── .mvn/
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── com/bla/pokemon/
│       │   └── resources/
│       └── test/
│
└── pokemon-frontend/
    ├── Dockerfile
    ├── .dockerignore
    ├── nginx.conf
    ├── package.json
    ├── package-lock.json
    ├── vite.config.ts
    └── src/
        ├── api/
        ├── components/
        ├── context/
        ├── pages/
        ├── types/
        ├── utils/
        ├── App.tsx
        └── main.tsx
```

## 4. Architecture

The application uses a client-server architecture with an external Pokémon data provider.

```text
                   +----------------------+
                   |       Browser        |
                   |    React Frontend    |
                   +----------+-----------+
                              |
                              | HTTP
                              |
                   +----------v-----------+
                   |        Nginx         |
                   |    Reverse Proxy     |
                   +----------+-----------+
                              |
                              | REST API
                              |
                   +----------v-----------+
                   |     Spring Boot      |
                   |                      |
                   |   Spring Security    |
                   |        JWT           |
                   +-----+-----------+----+
                         |           |
                         |           |
                +--------v----+  +---v----------+
                | PostgreSQL  |  |   PokéAPI    |
                | Local Data  |  | External API |
                +-------------+  +--------------+
```

### Backend Architecture

The backend follows Clean Architecture principles.

- **Domain Layer:** Core business entities and business rules.
- **Application Layer:** Use cases and application services.
- **Infrastructure Layer:** Database persistence, external API integrations, and security.
- **API Layer:** REST controllers and HTTP request handling.

This architecture promotes separation of concerns, maintainability, and testability.

## 5. Prerequisites

To run the complete application:

- Docker
- Docker Compose

For local development:

- Java 21
- Maven
- Node.js 22+
- npm
- PostgreSQL 16

## 6. Environment Configuration

Create a `.env` file in the project root:

```env
POSTGRES_PASSWORD=your_postgres_password
JWT_SECRET=your_secure_jwt_secret
```

The JWT secret must satisfy the signing algorithm's minimum key-length requirements.

Do not commit credentials or secrets to GitHub.

Add `.env` to `.gitignore`.

## 7. Running with Docker Compose

From the project root:

```bash
docker compose up -d --build
```

This starts:

1. PostgreSQL database.
2. Spring Boot backend.
3. React frontend served through Nginx.

### Application URLs

| Service | URL |
|---|---|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
| PostgreSQL | localhost:5432 |

### Check Containers

```bash
docker compose ps
```

### View Logs

```bash
docker compose logs -f backend
docker compose logs -f frontend
docker compose logs -f postgres
```

### Stop Containers

```bash
docker compose down
```

The PostgreSQL data volume is preserved.

**Warning:** `docker compose down -v` also removes the database volume.

## 8. Local Development

### Backend

Navigate to the backend directory:

```bash
cd pokemon-backend
```

Configure PostgreSQL connection settings and JWT_SECRET.

Start Spring Boot:

```bash
./mvnw spring-boot:run
```

Backend URL:

```text
http://localhost:8080
```

### Frontend

Navigate to the frontend directory:

```bash
cd pokemon-frontend
```

Install dependencies:

```bash
npm ci
```

Start React:

```bash
npm run dev
```

Frontend URL:

```text
http://localhost:5173
```

During development, Vite proxies `/api` requests to Spring Boot.

In Docker, Nginx provides the reverse proxy.

## 9. REST API Endpoints

Base URL:

```text
http://localhost:8080/api/v1
```

### Authentication

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | /auth/register | Register user | Public |
| POST | /auth/login | Authenticate user | Public |

### Pokémon

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | /pokemon | List Pokémon from PokéAPI | Public |
| GET | /pokemon/{id} | Pokémon details from PokéAPI | Public |
| GET | /pokemon/local | List Pokémon from PostgreSQL | Public API |
| GET | /pokemon/local/{id} | Local Pokémon details | Public API |
| POST | /pokemon/{id}/sync | Synchronize Pokémon | JWT |
| PUT | /pokemon/local/{id} | Update Pokémon | JWT |
| DELETE | /pokemon/local/{id} | Delete Pokémon | JWT |

The My Pokémon frontend routes require authentication. Under the current backend configuration, read-only Pokémon endpoints are public, while write operations require JWT.

### Pagination

Example:

```http
GET /api/v1/pokemon?page=0&size=12
```

Example response:

```json
{
  "content": [
    {
      "id": 25,
      "name": "pikachu",
      "imageUrl": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png",
      "weight": 60,
      "abilities": [
        "static",
        "lightning-rod"
      ]
    }
  ],
  "page": 0,
  "size": 12,
  "totalElements": 1,
  "totalPages": 1
}
```

This is an illustrative response, not the complete Pokémon catalog.

## 10. Authentication

The application uses JWT authentication with Spring Security.

### Register

```http
POST /api/v1/auth/register
Content-Type: application/json
```

Request:

```json
{
  "username": "ash",
  "email": "ash@example.com",
  "password": "SecurePassword123"
}
```

### Login

```http
POST /api/v1/auth/login
Content-Type: application/json
```

Request:

```json
{
  "email": "ash@example.com",
  "password": "SecurePassword123"
}
```

Response:

```json
{
  "accessToken": "<jwt-token>",
  "tokenType": "Bearer"
}
```

### Authenticated Requests

Protected operations require:

```http
Authorization: Bearer <jwt-token>
```

The JWT includes:

- User UUID as the subject.
- Username as an additional claim.
- Issued-at and expiration information.

The frontend stores the token in sessionStorage.

Spring Security validates JWT tokens for protected operations.

## 11. Pokémon Synchronization

The application distinguishes between external and local Pokémon.

### External Pokémon

Retrieved from PokéAPI through the backend.

Accessible from the Explore Pokémon page.

### Local Pokémon

Stored in PostgreSQL.

Accessible from the My Pokémon page.

### Synchronization Flow

```text
Explore Pokémon
       |
       v
Select Pokémon
       |
       v
View Details
       |
       v
Save to My Pokémon
       |
       v
Check Authentication
       |
       v
POST /api/v1/pokemon/{id}/sync
       |
       v
Spring Boot
       |
       v
PokéAPI Integration
       |
       v
PostgreSQL
       |
       v
My Pokémon
```

## 12. Frontend Pages

| Page | Route | Access |
|---|---|---|
| Explore Pokémon | / | Public |
| Pokémon Details | /pokemon/:id | Public |
| Login | /login | Public |
| Register | /register | Public |
| My Pokémon | /my-pokemon | Authenticated |
| Local Pokémon Details | /my-pokemon/:id | Authenticated |
| Edit Pokémon | /my-pokemon/:id/edit | Authenticated |

### Navigation

Unauthenticated users:

```text
Pokédex | Explore Pokémon | Login | Register
```

Authenticated users:

```text
Pokédex | Explore Pokémon | My Pokémon | Welcome, Username | Logout
```

## 13. Database

PostgreSQL is used for persistent storage.

The application stores:

- Registered users.
- Authentication-related user information.
- Synchronized Pokémon.
- Pokémon names, images, weights, and abilities.

Flyway manages database schema migrations.

Database data is persisted using a Docker volume.

## 14. Security

Security mechanisms include:

- JWT-based authentication.
- Spring Security authorization.
- Stateless backend authentication.
- Protected frontend routes.
- Password handling through the backend security layer.
- Environment-based secret configuration.
- Authorization headers for protected API requests.
- Authentication checks before modifying Pokémon.

Frontend route protection improves the user experience but does not replace backend authorization.

The backend is responsible for validating JWT signatures, expiration, and access permissions.

For production deployments, additional hardening should include HTTPS, secure token storage, secret management, and appropriate network restrictions.

## 15. Testing

### Backend Tests

Run:

```bash
cd pokemon-backend
./mvnw clean verify
```

The backend includes automated tests covering application behavior and Spring Security integration.

### Frontend Build Validation

Run:

```bash
cd pokemon-frontend
npm run build
```

### Manual Functional Tests

| Test | Expected Result |
|---|---|
| Explore Pokémon | Loads Pokémon from PokéAPI |
| Pokémon Details | Displays Pokémon information |
| Pagination | Navigates between pages |
| Register | Creates a new user |
| Login | Returns a valid JWT |
| Invalid Login | Displays authentication error |
| My Pokémon without Login | Redirects to Login |
| Synchronize Pokémon | Saves Pokémon in PostgreSQL |
| Edit Pokémon | Updates stored Pokémon |
| Delete Pokémon | Removes stored Pokémon |
| Logout | Removes session token |
| Protected API without JWT | Returns HTTP 401 |

## 16. Useful Docker Commands

Build and start:

```bash
docker compose up -d --build
```

List containers:

```bash
docker compose ps
```

View logs:

```bash
docker compose logs -f
```

Restart services:

```bash
docker compose restart
```

Rebuild frontend:

```bash
docker compose up -d --build frontend
```

Stop services:

```bash
docker compose down
```

## 17. Future Improvements

Potential improvements include:

- Automated frontend tests using Vitest and React Testing Library.
- End-to-end tests using Playwright.
- Centralized API error handling.
- Improved form validation.
- JWT refresh token support.
- Role-based authorization.
- Search and filtering for Pokémon.
- Improved responsive design.
- CI/CD pipelines using GitHub Actions.
- Production deployment to AWS.

## 18. External API

Pokémon data is retrieved from the public PokéAPI.

Website: https://pokeapi.co/

Documentation: https://pokeapi.co/docs/v2

## 19. Repository

GitHub:

https://github.com/cheyolanza/pokemon-challenge

## 20. Author

Developed as a full-stack technical challenge demonstrating:

- Java and Spring Boot backend development.
- REST API design.
- Clean Architecture.
- PostgreSQL persistence.
- JWT authentication.
- React and TypeScript frontend development.
- External API integration.
- Docker-based application deployment.