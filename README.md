# Trading Signal Handler
* Assuming a Spring Boot init project with SignalHandler interface and a third party library algorithm Algo.
* API endpoints:
POST: "/api/signals/{signalId}": This endpoint is designed to receive signals, and it allows you to send signals to the application.
POST: "/api/signals": This endpoint is used for creating new signals based on a Signal Specification. The data is saved as JSON in a PostgreSQL JSONB column for better manageability.
* To ensure flexibility and incremental addition of signals, I designed a database structure that allows you to easily add and manage signals as needed.

### Used Technologies

* Java 17
* Spring Boot
* Postgresql (for production level readiness)
* Spring Boot JPA
* Lombok
* Flyway database migration
* Docker

### How to run

### Package the application as a JAR file

```sh
mvn clean install -DskipTests
```

### Run the Spring Boot application and PostgreSQL with Docker Compose
(for docker build change the database config postgresql in application.properties)

```sh
docker-compose up -d --build
```

### Run only test cases

```sh
mvn test -Dspring.profiles.active=test
```
### Docker cleanup

```sh
docker system prune
```

### Testing Coverage

* Implement unit tests, integration tests with JUnit5.
* Implement JaCoCo to measure code coverage and ensure that critical code paths are tested.

### Design patterns

* RESTful API Design Pattern: REST (Representational State Transfer) expose the endpoints restfully way
* Controller-Service-Repository Pattern:
  Controller: Receives incoming HTTP requests, handles request validation, and invokes the appropriate service methods.
  Service: Contains the business logic, including validation and processing, and interacts with the repository.
  Repository: Manages data storage and retrieval.
* DTO (Data Transfer Object) Pattern: Use DTOs to transfer data between your API and the client.
* Error Handling Patterns: Implement a consistent error-handling mechanism using Spring's exception handling. Return meaningful error responses in JSON format.
* Dependency Injection (DI) Pattern: Implement DI with constructor injection

### Production ready

* Database PostgresSQL is configured for dockerige
* Flyway in implement for data migration

### API Definition

OpenAPI implemented for API definition
* http://localhost:8080/api-docs
* http://localhost:8080/swagger-ui/index.html


### Sample API Request:

Creating new Signal from specification:

Endpoint:"/api/signals"

Method type: POST

Request body:

```
{
  "actions": [
    { "name": "setUp", "parameters": [] },
    { "name": "setAlgoParam", "parameters": [1, 60] },
    { "name": "performCalc", "parameters": [] },
    { "name": "submitToMarket", "parameters": [] }
  ]
}
```

Sending signal to execute algo actions

Endpoint:"/api/signals/1"

Method type: POST

END!
