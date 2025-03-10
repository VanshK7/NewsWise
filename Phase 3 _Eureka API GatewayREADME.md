# News Aggregation and Recommendation System - Comprehensive README

This document provides a detailed overview of a news aggregation and recommendation system built using a microservices architecture. It covers the system architecture, individual service details, deployment instructions, configuration, technology choices, and important considerations for development and operation.

## 1. System Architecture

The system is designed as a set of independent microservices, each responsible for a specific function.  This architecture promotes:

*   **Scalability:** Individual services can be scaled independently based on demand. For example, if the `Recommendation-Service` is heavily used, you can scale it up without affecting other services.
*   **Maintainability:**  Changes to one service are less likely to impact others. Teams can work on different services concurrently, and updates can be rolled out incrementally.
*   **Fault Isolation:**  Failure of one service does not necessarily bring down the entire system.  If the `NewsSourceApplication` temporarily fails, user registration and recommendation retrieval might still function.
*   **Technology Diversity:**  Different services can be built using the most appropriate technologies. For instance, you could use Python and a different database for the `Recommendation-Service` if it suits your machine learning algorithms better, while keeping Java/Spring Boot for other services.

**Communication:** Services communicate primarily through synchronous REST APIs over HTTP.  Service discovery is handled by Netflix Eureka. The API Gateway serves as the single entry point for external clients, acting as a reverse proxy and routing requests to the appropriate backend services. Load balancing across service instances is implicitly handled by the API Gateway when using Eureka service IDs.

**Data Flow:**

1.  **User Registration:**  A user initiates registration through the API Gateway, which routes the request to the `UserService`. The `UserService` handles user registration, storing user credentials, email, and a comma-separated list of interests in a MySQL database.
2.  **News Fetching:**  The `NewsSourceApplication` is scheduled (e.g., using Spring's `@Scheduled` annotation) to periodically fetch top headlines from the News API (`newsapi.org`). The fetched news data (likely in JSON format) is processed and potentially stored locally or directly passed for contextual analysis.
3.  **Contextual Analysis (Optional but Recommended):** The `NewsSourceApplication` can send news article content to the `Context-Service` via REST API calls. The `Context-Service` leverages Google Gemini and OpenAI APIs to perform contextual analysis (e.g., summarization, sentiment analysis, topic extraction). This enriched context can significantly improve the quality of news recommendations.
4.  **Recommendation Generation:** When a user requests news recommendations (via the API Gateway to the `Recommendation-Service`), the `Recommendation-Service` retrieves the user's interests from the `UserService` and relevant news data (potentially enriched by the `Context-Service` and fetched from `NewsSourceApplication`). It then applies a recommendation algorithm (implementation-specific) to generate a personalized list of news articles or headlines.
5.  **API Gateway Routing and Client Access:**  All external client requests (from web browsers, mobile apps, etc.) first hit the API Gateway. The Gateway inspects the request path and uses configured routes (based on Eureka service IDs) to forward the request to the appropriate microservice. The client receives the response from the Gateway, which is relayed from the backend service.

**Diagram:**

![Microservices Architecture Diagram](https://www.lucidchart.com/publicSegments/view/f5751535-c744-4d99-a5b2-9d7e29494478/image.png)

## 2. Services Overview

This section provides in-depth details for each microservice, including API endpoints with request/response examples.

### 2.1. UserService

*   **Purpose:** Manages user accounts and their associated data, primarily user registration and retrieval of user interests.  Authentication and authorization are assumed to be handled by the API Gateway or a dedicated authentication service in a production system (not detailed in the provided code).
*   **Technology Stack:**
    *   **Spring Boot:**  Core framework.
    *   **Spring Data JPA:**  Database access layer.
    *   **MySQL:**  Persistent data storage.
    *   **Lombok:**  (Optional - if used) Reduces boilerplate code.
    *   **Maven:**  Build and dependency management.
    *   **Spring Cloud (Eureka Client):** Service discovery registration.
*   **Port:** 8081
*   **Database:** MySQL (`userdb`) - Configuration in `application.properties`.
*   **API Endpoints:**
    *   **1. Register a New User (`/users/register` - POST)**
        *   **Description:** Registers a new user account.
        *   **Request Body:** (JSON)
            ```json
            {
                "username": "string",  // Unique username, alphanumeric and underscores
                "email": "string",    // Valid email address format
                "password": "string",  // Password - plaintext in example, should be hashed in production
                "interest": "string"   // Comma-separated interests (e.g., "Technology,Politics,Sports")
            }
            ```
        *   **Example Request:**
            ```bash
            curl -X POST \
              http://localhost:8081/users/register \
              -H 'Content-Type: application/json' \
              -d '{
                "username": "tech_enthusiast",
                "email": "tech@example.com",
                "password": "securePassword123",
                "interest": "Technology,Gadgets,AI"
              }'
            ```
        *   **Response Body:** (JSON, on success - HTTP 200 OK)
            ```json
            {
                "id": 123,             // Auto-generated user ID (Long)
                "username": "tech_enthusiast",
                "email": "tech@example.com",
                "interest": "Technology,Gadgets,AI"
            }
            ```
        *   **Response Codes:**
            *   `200 OK`: Registration successful.
            *   `400 Bad Request`: Invalid request body (missing fields, invalid email format, etc.).
            *   `409 Conflict`: Username or email already exists (unique constraint violation).
            *   `500 Internal Server Error`: Unexpected server-side error.

    *   **2. Get User Interest by User ID (`/users/{userId}/interest` - GET)**
        *   **Description:** Retrieves the comma-separated interests of a user given their ID.
        *   **Path Parameters:**
            *   `userId`:  The unique identifier of the user (Long integer).
        *   **Example Request:**
            ```bash
            curl http://localhost:8081/users/123/interest
            ```
        *   **Response Body:** (Plain text, on success - HTTP 200 OK)
            ```
            Technology,Gadgets,AI
            ```
        *   **Response Codes:**
            *   `200 OK`: Successfully retrieved user interest.
            *   `404 Not Found`: User with the given `userId` does not exist.
            *   `500 Internal Server Error`: Unexpected server-side error.

### 2.2. Context-Service

*   **Purpose:** Provides contextual analysis of text, leveraging external AI APIs (Google Gemini and OpenAI).  In this example, we'll assume it offers a summarization endpoint.
*   **Technology Stack:**
    *   **Spring Boot:** Core framework.
    *   **Spring Cloud OpenFeign:** (If used to call other services, e.g., for more complex context enrichment - not shown in provided code, but good practice).
    *   **Maven:** Build and dependency management.
    *   **REST APIs (Google Gemini, OpenAI):** Integration with external AI services.
    *   **Spring Cloud (Eureka Client):** Service discovery registration.
*   **Port:** 8089
*   **API Keys (Configuration):**
    *   **Google Gemini API Key:** Configured via environment variable `GEMINI_API_KEY`.
    *   **OpenAI API Key:** Configured via environment variable `OPENAI_API_KEY`.
*   **API Endpoints:**
    *   **1. Summarize Text (`/api/context/summarize` - POST)**
        *   **Description:**  Summarizes the input text using an AI model (e.g., Gemini or OpenAI).
        *   **Request Body:** (JSON)
            ```json
            {
                "text": "string" // The text to be summarized (e.g., news article content)
            }
            ```
        *   **Example Request:**
            ```bash
            curl -X POST \
              http://localhost:8089/api/context/summarize \
              -H 'Content-Type: application/json' \
              -d '{
                "text": "Long article text goes here..."
              }'
            ```
        *   **Response Body:** (JSON, on success - HTTP 200 OK)
            ```json
            {
                "summary": "string" // The summarized text
            }
            ```
        *   **Response Codes:**
            *   `200 OK`: Text summarization successful.
            *   `400 Bad Request`: Invalid request body (missing 'text' field).
            *   `500 Internal Server Error`: Error during AI API call or internal processing.

### 2.3. NewsSourceApplication

*   **Purpose:** Fetches news headlines from the News API (`newsapi.org`) and makes them available to other services.
*   **Technology Stack:**
    *   **Spring Boot:** Core framework.
    *   **Spring Cloud OpenFeign:** (If used to call Context-Service for enrichment).
    *   **Maven:** Build and dependency management.
    *   **REST API (News API):** Integration with News API.
    *   **Spring Cloud (Eureka Client):** Service discovery registration.
*   **Port:** 9090
*   **API Keys (Configuration):**
    *   **News API Key:** Configured in `application.properties` as `news.api.key`. **Secure this in production using environment variables or secrets management.**
*   **API Endpoints:**
    *   **1. Get Top Headlines (`/api/news/top-headlines` - GET)**
        *   **Description:** Retrieves top news headlines based on optional filters.
        *   **Query Parameters:**
            *   `country`: (Optional) ISO 3166-1 alpha-2 country code (e.g., "us", "gb"). Defaults to a global or configured default.
            *   `category`: (Optional) News category (e.g., "business", "entertainment", "technology", "sports", "general", "health", "science").
        *   **Example Request:**
            ```bash
            curl "http://localhost:9090/api/news/top-headlines?country=us&category=technology"
            ```
        *   **Response Body:** (JSON, on success - HTTP 200 OK)
            ```json
            [
                {
                    "title": "Article Title 1",
                    "description": "Article description...",
                    "url": "article_url_1",
                    "source": "Source Name 1",
                    "publishedAt": "2024-01-01T10:00:00Z"
                },
                {
                    "title": "Article Title 2",
                    "description": "Article description...",
                    "url": "article_url_2",
                    "source": "Source Name 2",
                    "publishedAt": "2024-01-01T11:00:00Z"
                },
                // ... more articles
            ]
            ```
        *   **Response Codes:**
            *   `200 OK`: Successfully retrieved headlines.
            *   `400 Bad Request`: Invalid query parameters (e.g., invalid country code).
            *   `401 Unauthorized`: Invalid or missing News API key.
            *   `500 Internal Server Error`: Error fetching from News API or internal processing.

    *   **2. Search News Articles (`/api/news/search` - GET)**
        *   **Description:** Searches for news articles based on a keyword query.
        *   **Query Parameters:**
            *   `query`: (Required) Search keyword(s).
        *   **Example Request:**
            ```bash
            curl "http://localhost:9090/api/news/search?query=artificial+intelligence"
            ```
        *   **Response Body:** (JSON, on success - HTTP 200 OK) -  Similar structure to `/top-headlines` response, containing an array of news articles matching the query.
        *   **Response Codes:** Same response codes as `/top-headlines`.

### 2.4. Recommendation-Service

*   **Purpose:** Generates personalized news recommendations for users based on their interests and available news data.
*   **Technology Stack:**
    *   **Spring Boot:** Core framework.
    *   **Maven:** Build and dependency management.
    *   **Spring Cloud (Eureka Client):** Service discovery registration.
    *   **(Potentially) Spring Data:** If you decide to persist recommendation data or user preferences beyond interests.
    *   **(Potentially) Machine Learning Libraries:** If implementing a more sophisticated recommendation algorithm.
*   **Port:** 8090
*   **API Endpoints:**
    *   **1. Get Recommendations for User (`/recommendations/{userId}` - GET)**
        *   **Description:** Retrieves personalized news recommendations for a specific user.
        *   **Path Parameters:**
            *   `userId`: The ID of the user for whom to generate recommendations (Long integer).
        *   **Example Request:**
            ```bash
            curl http://localhost:8090/recommendations/123
            ```
        *   **Response Body:** (JSON, on success - HTTP 200 OK)
            ```json
            [
                {
                    "title": "Recommended Article Title 1",
                    "url": "recommended_article_url_1",
                    "source": "Source Name 3",
                    "relevanceScore": 0.85 // Example relevance score (optional)
                },
                {
                    "title": "Recommended Article Title 2",
                    "url": "recommended_article_url_2",
                    "source": "Source Name 4",
                    "relevanceScore": 0.78
                },
                // ... more recommended articles
            ]
            ```
        *   **Response Codes:**
            *   `200 OK`: Recommendations successfully generated and retrieved.
            *   `404 Not Found`: User with the given `userId` not found in the `UserService`.
            *   `500 Internal Server Error`: Error during recommendation generation or data retrieval.

### 2.5. API Gateway

*   **Purpose:**  Provides a single entry point for all external client requests.  Handles routing, load balancing, and potentially cross-cutting concerns like authentication, authorization, rate limiting, and request logging (though these are not fully implemented in the provided code).  **From a client's perspective, the API Gateway *is* the API.**  Clients interact with the system solely through the Gateway's endpoints, and the Gateway handles routing those requests to the appropriate backend microservices.
*   **Technology Stack:**
    *   **Spring Boot:** Core framework.
    *   **Spring Cloud Gateway:**  API Gateway implementation.
    *   **Spring Cloud Netflix Eureka Client:** Service discovery client for dynamic routing.
    *   **Maven:** Build and dependency management.
*   **Port:** 8080
*   **API Gateway Endpoints (Exposed to Clients):**

    Clients access the system through the following base URL for the API Gateway: `http://localhost:8080`

    The following **paths** under this base URL are the API endpoints exposed by the Gateway, which are then routed to the respective backend services:

    *   **User Service Endpoints (Routed to `UserService`):**
        *   `POST /users/register`: Register a new user.  (See UserService endpoint details for request/response)
        *   `GET /users/{userId}/interest`: Get user interest by ID. (See UserService endpoint details for request/response)

        **Example Client Request (Register User via Gateway):**
        ```bash
        curl -X POST \
          http://localhost:8080/users/register \  # Accessing Gateway endpoint
          -H 'Content-Type: application/json' \
          -d '{
            "username": "gateway_user",
            "email": "gateway@example.com",
            "password": "securePass",
            "interest": "Gaming,Movies"
          }'
        ```

    *   **News Service Endpoints (Routed to `NewsSourceApplication`):**
        *   `GET /api/news/top-headlines`: Get top news headlines. (See NewsSourceApplication endpoint details for parameters and response)
        *   `GET /api/news/search`: Search news articles. (See NewsSourceApplication endpoint details for parameters and response)

        **Example Client Request (Get Top Headlines via Gateway):**
        ```bash
        curl "http://localhost:8080/api/news/top-headlines?country=gb&category=sports" # Accessing Gateway endpoint
        ```

    *   **Context Service Endpoints (Routed to `Context-Service`):**
        *   `POST /api/context/summarize`: Summarize text. (See Context-Service endpoint details for request/response)

        **Example Client Request (Summarize Text via Gateway):**
        ```bash
        curl -X POST \
          http://localhost:8080/api/context/summarize \ # Accessing Gateway endpoint
          -H 'Content-Type: application/json' \
          -d '{
            "text": "Article text to summarize..."
          }'
        ```

    *   **Recommendation Service Endpoints (Routed to `Recommendation-Service`):**
        *   `GET /recommendations/{userId}`: Get recommendations for user. (See Recommendation-Service endpoint details for request/response)

        **Example Client Request (Get Recommendations via Gateway):**
        ```bash
        curl http://localhost:8080/recommendations/456 # Accessing Gateway endpoint
        ```

    *   **Routing Mechanism:** Spring Cloud Gateway's DiscoveryLocatorClient auto-routes requests based on service IDs registered in Eureka.  The `lb://` prefix in route definitions indicates load-balanced routing through Eureka.  **Clients are not directly aware of the backend microservices' ports or internal structure. They only interact with the API Gateway.**

### 2.6. Eureka (Service Registry)

*   **Description:**  Service registry that allows microservices to register themselves and discover the network locations (host and port) of other services. Enables dynamic and loosely coupled communication between services.
*   **Technology Stack:**
    *   **Spring Boot:** Core framework.
    *   **Spring Cloud Netflix Eureka Server:** Eureka server implementation.
    *   **Maven:** Build and dependency management.
*   **Port:** 8761
*   **Dashboard:** Accessible via web browser at `http://localhost:8761`.  Provides a UI to view registered services and their instances.
*   **Configuration:**  Configured as a standalone Eureka server (does not register itself as a client and does not fetch registry from peers).

## 3. Running the Application

**(Detailed steps to build and run all services are provided in the previous comprehensive README.  Refer to the "Running the Application" section there for detailed instructions on prerequisites, cloning, environment variables, database setup, build process, and startup order.)**

**Key Points for Running:**

*   **Startup Order is Important:** Start Eureka first, then other services in an order that ensures dependencies are met (e.g., `UserService` before `Recommendation-Service` if the latter depends on the former).
*   **Environment Variables:** Properly set `GEMINI_API_KEY` and `OPENAI_API_KEY` environment variables before running the `Context-Service`.
*   **Database Setup:** Ensure MySQL is running and the `userdb` database is created before starting `UserService`.
*   **API Gateway Access:**  Access the system through the API Gateway at `http://localhost:8080`. Use the defined routes to reach backend services.

## 4. Configuration Files

**(Configuration details for each service's `application.properties` file are provided in the previous comprehensive README.  Refer to the "Configuration Files" section there for details on key properties.)**

**General Configuration Notes:**

*   **`application.properties`:**  Used for service-specific configuration (ports, database connections, API keys, Eureka settings, etc.).
*   **Service Discovery (`eureka.client.service-url.defaultZone`):**  Crucially configured in each microservice to point to the Eureka server (`http://localhost:8761/eureka/`).
*   **API Keys:**  Handle API keys securely.  Environment variables are a good starting point for development, but consider more robust secrets management solutions for production.
*   **Logging:** Configure logging levels appropriately (e.g., `DEBUG` for development, `INFO` or `WARN` for production) using `logging.level.*` properties in `application.properties`.

## 5. Technologies Used

**(A consolidated list of technologies is provided in the previous comprehensive README.  Refer to the "Technologies Used" section there.)**

**Key Technologies Summary:**

*   **Java & Spring Boot:**  Primary development platform and framework.
*   **Spring Cloud:**  For microservices architecture components (Gateway, Eureka, OpenFeign).
*   **Maven:** Build tool.
*   **MySQL:** Relational database.
*   **REST APIs:** Communication protocol.
*   **External AI APIs (Gemini, OpenAI, News API):** Integration with external services.

## 6. Important Notes and Best Practices

**(Important notes on security, error handling, scalability, API documentation, and production considerations are provided in the previous comprehensive README.  Refer to the "Important Notes" section there.)**

**Key Best Practices Summary:**

*   **Security First:** Secure API keys, database credentials, and user data. Use HTTPS, input validation, and appropriate authentication/authorization mechanisms in a production system.
*   **Robust Error Handling:** Implement proper exception handling, logging, and error responses in each service. Use HTTP status codes semantically.
*   **Monitoring and Logging:** Set up monitoring and logging infrastructure to track service health, performance, and errors in production.
*   **Scalability and Resilience:** Design services to be stateless where possible.  Plan for horizontal scaling and fault tolerance.
*   **API Documentation:** Use tools like Swagger/OpenAPI to automatically generate and maintain up-to-date API documentation for each service. This is crucial for discoverability and usability.
*   **Production Readiness:**  Thoroughly test and harden services before deploying to production. Consider aspects like performance testing, security audits, and deployment automation.
*   **Database User Privileges:** Never use the `root` MySQL user in production. Create dedicated database users with minimal necessary privileges for each service.
*   **Password Handling:** Never store or transmit passwords in plaintext. Use robust hashing algorithms (e.g., bcrypt) with salts.  Do not return passwords in API responses.

