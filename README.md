# Spring Cloud Microservices Architecture

## Project Description
This project involves creating two microservices based on Spring Boot and Spring Cloud that communicate via REST API. The system consists of four services:

1. Service Registry (Eureka Server) – Manages the registration of microservices.
2. Gateway – Acts as a proxy, directing requests to services based on their names.
3. External Service – Exposes a REST API to users and integrates with the internal service.
4. Internal Service – Handles an in-memory database and provides data to the external service.

## Architecture
The system is built using the following components:

- Service Registry (Netflix Eureka): Microservices register themselves to Eureka, enabling discovery by other services using their service names.
- Spring Cloud Gateway: Provides a reverse proxy to route requests to appropriate services, hiding direct access to the external service.
- RestTemplate/WebClient with @LoadBalanced: Facilitates communication between services using service names instead of IP addresses or ports.
- FlywayDB: The internal service manages the database schema and populates it with test data.
- OpenAPI/Swagger: Generates API documentation for the external service.
- Keycloak: Security for the external service using Keycloak and OAuth2/JWT.

## Features
1. Service Registry (Eureka Server):

- Centralized registry for all microservices.
- Enables service discovery using service names.

2. Gateway:

- Acts as a reverse proxy for routing HTTP requests.
- Secures direct access to the external service.

4. External Service:

- Exposes a REST API for CRUD operations.
- Retrieves data from the internal service using REST communication.
- Supports pagination, sorting, and filtering for retrieving records.
- Provides API documentation using Swagger.

5. Internal Service:

- Manages an in-memory database with Flyway for schema migration.
- Contains an entity with at least three attributes (besides ID).
- Prepopulates the database with sample data for testing.

6. Additional Features:

- Advanced filtering in the external service, allowing API clients to filter records by any attribute (e.g., using JPA Criteria API, Spring Data Specification, or QueryDSL).
- Authentication and authorization via Keycloak and OAuth2/JWT.

## Prerequisites

1. Docker and Docker Compose installed.
2. Java 17 or higher installed.
3. Postman or cURL for API testing.
4. Maven installed for dependency management.

---

## Setup and Run the Application

### Step 1: Clone the Repository

```bash
git clone https://github.com/Quilsen/SpringCloudExercise.git
cd project
```

### Step 2: Build and Run the Application

1. Build the project:

```bash
mvn clean install
```
2. Start the application:

```bash
mvn spring-boot:run
```
3. Run individual services:

- Service Registry: ``mvn spring-boot:run`` in the EurekaServer directory.
- Gateway: ``mvn spring-boot:run`` in the Gateway directory.
- External Service: ``mvn spring-boot:run`` in the ExternalService directory.
- Internal Service: ``mvn spring-boot:run`` in the InternalService directory.

### Step 3: Start Keycloak service and apply real-export.json

Run the following command to start Keycloak

```bash
docker-compose up -d
```

## API Endpoints

### External Service
- POST /account/register/parking-user: Register a new user.
- POST /api/products: Create a new product.
- GET /api/products/{id}: Retrieve a product by ID.
- GET /api/products: Retrieve all products.
- GET /api/products/filter: Filter products with pagination and sorting.
- PUT /api/products/{id}: Update a product by ID.
- DELETE /api/products/{id}: Delete a product by ID.


## How to Test
1. Use tools like Postman or Curl to test the REST endpoints of the external service.
2. Access the Swagger UI for API documentation at /swagger-ui.html for the external service.