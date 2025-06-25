# Project Setup Guide

## 1\. Clone the repository

```sh
git clone https://github.com/kesavan-selvaraj-aqilea/threadpilot-insurance-service.git
cd threadpilot-insurance-service
```
## 2\. Build the project with Maven
```
mvn clean install
```
## 3\. Run the Spring Boot application
```
mvn spring-boot:run
```

## 4\. API Documentation and Status check
Open your web browser and navigate to Swagger UI:
```
http://localhost:8081/swagger-ui/index.html
```

Open your web browser and navigate to health endpoint to verify the application is running:

```
http://localhost:9001/actuator/health
```
## 5\. Configure 'business-campaign-discount' toggle with unleash.api-url

```properties
# Configure Unleash with application.properties : (Example)
unleash.app-name=unleash-onboarding-java
unleash.instance-id=unleash-onboarding-instance
unleash.api-url=https://eu.app.unleash-hosted.com/demo/api/
unleash.api-key=threadpilot:development.4548f91137a26cdcae9b52c5f61c641f8f944ad0072d7860d34d56a8
```

## 6\. Personal Reflection

# ThreadPilot Integration Layer

This project, named "ThreadPilot," serves as a foundational integration layer designed to connect a new core system with multiple legacy systems. It demonstrates a microservice architecture built with Spring Boot, showcasing inter-service communication, robust error handling, and runtime configurable feature toggling.

## Project Highlights

This assignment allowed for the exploration and implementation of several key architectural and development practices common in modern distributed systems:

* **Microservice Architecture:** Implemented as two distinct Spring Boot applications (`vehicle-service` and `insurance-service`), demonstrating clear separation of concerns and independent deployability.
    * **`vehicle-service`**: Exposes an endpoint to retrieve vehicle information based on registration numbers.
    * **`insurance-service`**: Exposes an endpoint to retrieve a person's insurance details, including monthly costs. Crucially, it integrates with `vehicle-service` to enrich car insurance data.
* **RESTful API Design:** Both services expose their functionalities via well-defined RESTful endpoints, ensuring standard and accessible communication.
* **Runtime Feature Toggling:** A "business campaign discount" feature in `insurance-service` is controlled by a runtime-configurable toggle. This demonstrates how new features can be deployed safely and gradually rolled out to specific subsets of customers (e.g., based on personal identification numbers) without affecting core functionalities.
* **Centralized Exception Handling:** Utilizes `@RestControllerAdvice` and `@ExceptionHandler` to provide consistent and user-friendly error responses (e.g., 404 Not Found) across the `insurance-service`, preventing the exposure of internal server details.
* **Data Transfer Objects (DTOs) with Java Records:** Leveraged modern Java `record` types for DTOs within the `insurance-service`. This significantly improves type safety, code readability, and immutability for data passed between layers and exposed via the API.

## Challenges & Learnings

The most challenging and interesting aspects of this project included:

* **Inter-Service Communication:** Setting up `insurance-service` to reliably call `vehicle-service` using `RestTemplate` and handling potential communication failures.
* **Dynamic Feature Control:** Implementing the logic for feature toggling, specifically controlling feature exposure based on both a global switch and customer-specific eligibility criteria.  
* **API Robustness:** Ensuring a polished API experience through structured error responses via global exception handling, crucial for external consumers.

## Future Enhancements

Given more time, the following improvements and extensions would further enhance the "ThreadPilot" integration layer:

* **Advanced Feature Flagging:** Improve the existing implementation for more sophisticated feature toggle management.
* **Resilient Communication:** Implement `WebClient` or Feign Client for inter-service communication, incorporating resilience patterns like retries, circuit breakers (e.g., Resilience4j), and timeouts.
* **Security Implementation:** Introduce Spring Security for API authentication (e.g., JWTs) and authorization to secure endpoints.
* **Data Persistence:** Enhance Spring Data JPA to be more robust to seamlessly adopt/change different databases without having much change in implementation.
* **Monitoring & Observability:** Integrate Spring Boot Actuator with Prometheus for metrics collection and Grafana for comprehensive dashboards, providing insights into application health and performance.
* **Containerization & Orchestration:** Dockerize both microservices and orchestrate their deployment using Docker Compose or Kubernetes for a production-ready setup.

