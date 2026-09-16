

# RentalX

RentalX is a car rental management REST API built with Spring Boot.

## Features

- User registration and JWT-based authentication
- Vehicle management
- Reservation creation and cancellation
- Payment flow
- Rental start and return operations
- Vehicle status management
- Reservation status management
- Redis caching
- RabbitMQ messaging
- PostgreSQL persistence
- Swagger / OpenAPI documentation
- Unit tests
- Integration tests
- Docker support

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Security
- Spring Data JPA
- PostgreSQL
- Redis
- RabbitMQ
- JWT
- Swagger / OpenAPI
- Docker
- Docker Compose
- Maven
- JUnit
- Mockito

## Main Application Flow

```text
User Registration / Login
        ↓
Browse Vehicles
        ↓
Create Reservation
        ↓
PENDING_PAYMENT
        ↓
Payment
        ↓
CONFIRMED
        ↓
Rental Started
        ↓
Vehicle Status: RENTED
        ↓
Vehicle Returned
        ↓
Rental Status: RETURNED
        ↓
Reservation Status: COMPLETED
        ↓
Vehicle Status: AVAILABLE
```

## Docker

The project can be started with Docker Compose.

The following services are included:

- RentalX application
- PostgreSQL
- Redis
- RabbitMQ

### Build the application

```bash
./mvnw clean package -DskipTests
```

### Start all services

```bash
docker compose up -d --build
```

### Check running containers

```bash
docker ps
```

### Stop all services

```bash
docker compose down
```

## Swagger / OpenAPI

After starting the application, Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

Swagger supports JWT Bearer authentication through the **Authorize** button.

## RabbitMQ Management UI

RabbitMQ Management UI is available at:

```text
http://localhost:15673
```

## Database

PostgreSQL runs through Docker Compose.

Host port:

```text
5433
```

Internal Docker port:

```text
5432
```

## Redis

Redis is used for caching vehicle data.

Host port:

```text
6380
```

Internal Docker port:

```text
6379
```

## RabbitMQ

RabbitMQ is used for asynchronous messaging.

A payment success event is published after a successful payment and consumed by the notification flow.

Host AMQP port:

```text
5673
```

Internal Docker port:

```text
5672
```

## Tests

The project includes unit tests for:

- VehicleService
- ReservationService
- PaymentService
- RentalService

It also includes integration tests for vehicle endpoints.

Run tests with:

```bash
./mvnw test
```

## Project Structure

```text
src
├── main
│   ├── java
│   │   └── com.rentalx
│   └── resources
│       └── application.properties
│
└── test
    └── java
        └── com.rentalx
```

## API Documentation

API endpoints are grouped in Swagger by controller.

Main modules include:

- Users
- Vehicles
- Reservations
- Payments
- Rentals

## Security

JWT is used for authentication and authorization.

Protected endpoints require a valid Bearer token.

