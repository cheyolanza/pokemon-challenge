
# Pokémon Challenge

A RESTful Pokémon API built with **Java 21, Spring Boot 4, PostgreSQL, and Spring Security**, following Clean Architecture and Test-Driven Development (TDD) principles.

## Tech Stack

- Java 21 / Spring Boot 4
- Spring Security / JWT (HS256)
- Spring Data JPA / PostgreSQL 16
- Flyway Database Migrations
- Maven / JUnit 5 / Mockito / MockMvc
- Docker Compose
- [PokéAPI](https://pokeapi.co/)

## Project Structure

```text
pokemon-challenge/
├── pokemon-backend/
│   └── src/
│       ├── main/
│       │   ├── java/com/bla/pokemon/
│       │   │   ├── domain/
│       │   │   ├── application/
│       │   │   ├── infraestructure/
│       │   │   └── presentation/
│       │   └── resources/db/migration/
│       └── test/
├── pokemon-frontend/
├── compose.yaml
└── README.md
```

## Architecture

The backend follows Clean Architecture:

- **Domain:** Business models and exceptions.
- **Application:** Use cases and repository interfaces.
- **Infrastructure:** PostgreSQL persistence, PokéAPI integration, and JWT security.
- **Presentation:** REST controllers, DTOs, and exception handling.

## Prerequisites

- Java 21
- Docker and Docker Compose
- Maven Wrapper (included)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/cheyolanza/pokemon-challenge.git
cd pokemon-challenge
```

### 2. Start PostgreSQL

```bash
docker compose up -d postgres
```

Default local database configuration:

| Property | Value |
|----------|-------|
| Database | pokemon_db |
| Username | pokemon |
| Password | pokemon_dev |
| Port | 5432 |

### 3. Configure JWT

Generate and export a secure JWT secret:

```bash
export JWT_SECRET="$(openssl rand -base64 32)"
```

The JWT secret must be provided through an environment variable. Do not commit secrets to Git.

### 4. Run the Backend

```bash
cd pokemon-backend
./mvnw spring-boot:run
```

API Base URL:

```text
http://localhost:8080
```

Flyway automatically creates and updates the database schema during application startup.

## API Endpoints

| Method | Endpoint | Description | Authentication |
|--------|----------|-------------|----------------|
| POST | `/api/v1/auth/register` | Register a new user | Public |
| POST | `/api/v1/auth/login` | Authenticate and obtain JWT | Public |
| GET | `/api/v1/pokemon` | List Pokémon from PokéAPI | Public |
| GET | `/api/v1/pokemon/{id}` | Get Pokémon details | Public |
| GET | `/api/v1/pokemon/local` | List saved Pokémon | Public |
| GET | `/api/v1/pokemon/local/{id}` | Get saved Pokémon by ID | Public |
| POST | `/api/v1/pokemon/{id}/sync` | Synchronize Pokémon to PostgreSQL | JWT |
| PUT | `/api/v1/pokemon/local/{id}` | Update a saved Pokémon | JWT |
| DELETE | `/api/v1/pokemon/local/{id}` | Delete a saved Pokémon | JWT |

Pagination is supported using `page` and `size` query parameters.

Example:

```bash
curl "http://localhost:8080/api/v1/pokemon?page=0&size=10"
```

## Authentication

### Register a User

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ash",
    "email": "ash@example.com",
    "password": "Password123!"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "ash@example.com",
    "password": "Password123!"
  }'
```

Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer"
}
```

JWT tokens expire after 60 minutes by default.

### Access a Protected Endpoint

```bash
curl -X POST http://localhost:8080/api/v1/pokemon/25/sync \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### Update a Pokémon

```bash
curl -X PUT http://localhost:8080/api/v1/pokemon/local/25 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "pikachu",
    "imageUrl": "https://example.com/pikachu.png",
    "weight": 60,
    "abilities": ["static", "lightning-rod"]
  }'
```

### Delete a Pokémon

```bash
curl -X DELETE http://localhost:8080/api/v1/pokemon/local/25 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Testing

Run the complete test suite:

```bash
cd pokemon-backend
./mvnw clean verify
```

Run only the security integration tests:

```bash
./mvnw test -Dtest=PokemonSecurityIntegrationTest
```

Tests cover:

- Application use cases
- Pokémon retrieval and synchronization
- Input validation
- JWT authentication and endpoint authorization
- External API behavior

## Security

- BCrypt password hashing
- Stateless JWT authentication
- JWT signature and expiration validation
- Protected write operations (POST, PUT, DELETE)
- Public read-only Pokémon endpoints
- Environment-based secret configuration

## Development Principles

- Clean Architecture
- Dependency Inversion
- Test-Driven Development (TDD)
- Separation of Concerns
- RESTful API Design

## External API

Pokémon information is retrieved from the public [PokéAPI](https://pokeapi.co/).

## License

Developed as a technical coding challenge for demonstration and evaluation purposes.
