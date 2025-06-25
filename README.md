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

## 4\. Access the application
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
