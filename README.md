# NewsWise



# UserService Application

## Description

This microservice is responsible for managing user information. It provides functionalities for user registration and retrieving user interests. This service is built using Spring Boot and utilizes a MySQL database for data persistence.

## API Endpoints

The following API endpoints are available for interacting with the User Service:




### 1. Register a New User

**Endpoint:** `/users/register`

**Method:** `POST`

**Request Body:**

The request body should be a JSON object representing the user to be registered. The following fields are expected:

```json
{
    "username": "string",
    "email": "string",
    "password": "string",
    "interest": "string"
}
```

**Example Request:**

```bash
curl -X POST \
  http://localhost:8081/users/register \
  -H 'Content-Type: application/json' \
  -d '{
    "username": "newswise",
    "email": "newswise@example.com",
    "password": "12",
    "interest": "Technology,AI"
}'
```

**Response Body:**

Upon successful registration, the service returns a JSON object representing the newly registered user, including a generated `id`.

```json
{
    "username": "newswise",
    "email": "newswise@example.com",
    "password": "12",
    "interest": "Technology,AI"
}
```

**Response Codes:**

* `200 OK`: User registration successful.

### 2. Get User Interest by User ID

**Endpoint:** `/users/{userId}/interest`

**Method:** `GET`

**Path Parameters:**

* `{userId}`:  The unique identifier of the user (a Long integer).

**Example Request:**

```bash
curl http://localhost:8081/users/1/interest
```

**Response Body:**

The service returns a plain text string representing the user's interest.

```
Technology
```

**Response Codes:**

* `200 OK`: Successfully retrieved user interest.
* `404 Not Found`: User with the given ID not found. (Implementation detail not shown in provided code, but good practice to mention)

## How to Run the Application

1. **Prerequisites:**
    * Java 23 (or compatible version)
    * Maven
    * MySQL Database Server running on `localhost:3306`
    * Database named `userdb` created in MySQL.
    * MySQL user `root` with password `Dhruvi1505` (as configured in `application.properties`).

2. **Clone the repository:**
  

3. **Build the application using Maven:**

4. **Run the application:**
   The application will start on port `8081`.

## Technologies Used

* Spring Boot
* Spring Data JPA
* MySQL
* Lombok
* Maven


# News Source Application  

## Description  

The *News Source Application* is responsible for fetching news articles from various sources, summarizing the content, categorizing them, and delivering structured responses. This microservice integrates with *NewsAPI* to fetch news articles and communicates with the *ContextService* to generate summaries and categorize news.  

## API Endpoints  

The following API endpoints are available for interacting with the News Source Application:  

### 1. Fetch and Summarize News  

*Endpoint:* /api/news/summarized-news  

*Method:* GET  

*Query Parameters:*  

* sources (string) - The news source ID (e.g., bbc-news, cnn).  

*Example Request:*  

bash
curl -X GET "http://localhost:9090/api/news/summarized-news?sources=bbc-news"
  

*Response Body:*  

Upon success, the API returns a JSON object containing summarized and categorized news articles.  

json
{
    "status": "ok",
    "totalResults": 10,
    "articles": [
        {
            "title": "AI Breakthrough in Healthcare",
            "description": "Scientists have developed an AI system capable of early disease detection.",
            "content": "This AI model helps in predicting diseases...",
            "category": "Technology",
            "bias": "Neutral",
            "complexExplanation": "The model uses deep learning algorithms..."
        }
    ]
}
  

*Response Codes:*  

* 200 OK: Successfully retrieved summarized news.  
* 400 Bad Request: Missing or invalid request parameters.  
* 500 Internal Server Error: Issue with fetching or processing news.  

---

### *2. Get Raw News from Source*  

*Endpoint:* /api/news/raw  

*Method:* GET  

*Query Parameters:*  

* sources (string) - The news source ID.  

*Example Request:*  

bash
curl -X GET "http://localhost:9090/api/news/raw?sources=bbc-news"
  

*Response Body:*  

json
{
    "status": "ok",
    "totalResults": 5,
    "articles": [
        {
            "title": "New Space Mission Announced",
            "description": "NASA has announced a new mission to explore Mars.",
            "content": "NASA plans to launch a spacecraft...",
            "source": "BBC News"
        }
    ]
}
  

*Response Codes:*  

* 200 OK: Successfully retrieved raw news.  
* 400 Bad Request: Missing or invalid request parameters.  

---

## How to Run the Application  

### *1. Prerequisites*  

* Java 23 (or compatible version)  
* Maven  
* Active Internet Connection (for fetching news from APIs)  

### *2. Clone the Repository*  

bash
git clone https://github.com/your-repo/news-source-application.git
cd news-source-application


### *3. Build the Application*  

bash
mvn clean package


### *4. Run the Application*  

bash
mvn spring-boot:run


The application will start on **port 9090**.  

---

## Technologies Used  

* *Spring Boot* - Backend framework  
* *Spring Cloud OpenFeign* - API communication  
* *NewsAPI* - Fetching news articles  
* *ContextService (LLM)* - Summarization & Categorization  
* *Maven* - Dependency management  

---


# Context Service  

## Description  

The **Context Service** is responsible for processing news articles using a language model (LLM). It extracts key insights, identifies biases, summarizes content, and categorizes articles based on predefined topics. This microservice is designed to work with the **News Source Application** to enhance the readability and organization of news articles.  

## API Endpoints  

The following API endpoints are available for interacting with the Context Service:  

### 1. Generate AI Response  

**Endpoint:** `/api/context/generate`  

**Method:** `POST`  

**Request Body:**  

The request body should contain a text input that the AI model will process.  

json
{
    "message": "Explain the significance of AI in modern healthcare."
}
  

**Example Request:**  

bash
curl -X POST "http://localhost:8089/api/context/generate" \
     -H "Content-Type: application/json" \
     -d '{"message": "Explain the significance of AI in modern healthcare."}'
  

**Response Body:**  

json
{
    "response": "AI in healthcare enables early disease detection, personalized treatment, and improved patient outcomes."
}
  

**Response Codes:**  

* `200 OK`: Successfully generated AI response.  
* `400 Bad Request`: Missing or invalid request data.  

### 2. Summarize an Article  

**Endpoint:** `/api/context/summarize`  

**Method:** `POST`  

**Request Body:**  

json
{
    "article": "Artificial Intelligence is transforming the healthcare industry by enabling faster diagnosis..."
}
  

**Example Request:**  

bash
curl -X POST "http://localhost:8089/api/context/summarize" \
     -H "Content-Type: application/json" \
     -d '{"article": "Artificial Intelligence is transforming the healthcare industry by enabling faster diagnosis..."}'
  

**Response Body:**  

json
{
    "summary": "AI is revolutionizing healthcare by improving diagnosis and patient care efficiency."
}
  

**Response Codes:**  

* `200 OK`: Successfully summarized article.  
* `400 Bad Request`: Invalid article format.  

### 3. Categorize an Article  

**Endpoint:** `/api/context/topic`  

**Method:** `POST`  

**Request Body:**  

json
{
    "article": "NASA has announced a new mission to explore Mars."
}
  

**Example Request:**  

bash
curl -X POST "http://localhost:8089/api/context/topic" \
     -H "Content-Type: application/json" \
     -d '{"article": "NASA has announced a new mission to explore Mars."}'
  

**Response Body:**  

json
{
    "topic": "Space"
}
  

**Response Codes:**  

* `200 OK`: Successfully categorized article.  
* `400 Bad Request`: Invalid input data.  

## How to Run the Application  

### 1. Prerequisites  

* Java 23 (or compatible version)  
* Maven  
* API Key for **Gemini AI** (configured in `application.properties`)  

### 2. Clone the Repository  

bash
git clone https://github.com/your-repo/context-service.git
cd context-service


### 3. Build the Application  

bash
mvn clean package


### 4. Run the Application  

bash
mvn spring-boot:run


The application will start on **port `8089`**.  

## Technologies Used  

* **Spring Boot** - Backend framework  
* **Spring Cloud OpenFeign** - API communication  
* **Gemini AI / OpenAI API** - AI processing and summarization  
* **Maven** - Dependency management


# Recommendation Service

## Description
The **Recommendation Service** is a microservice responsible for providing personalized news recommendations to users based on their interests. It interacts with other microservices such as **User Service** and **News Service** to fetch user preferences and relevant news articles. This service is built using **Spring Boot** and utilizes **OpenFeign** for inter-service communication.

## API Endpoints
The following API endpoints are available for interacting with the Recommendation Service:

### 1. Get Personalized News Feed
- **Endpoint:** `/recommendations/feed/{userId}`
- **Method:** `GET`
- **Path Parameter:**
  - `{userId}`: The unique identifier of the user (a Long integer).
- **Example Request:**

  ```sh
  curl -X GET http://localhost:8090/recommendations/feed/1
  ```

- **Response Body:**

  ```json
  [
    {
      "title": "AI Advances in Healthcare",
      "description": "New AI models improving diagnostics...",
      "content": "Details about AI applications...",
      "category": "Technology",
      "bias": "Neutral",
      "complexExplanation": "AI impact on healthcare explained..."
    }
  ]
  ```

- **Response Codes:**
  - `200 OK`: Successfully retrieved the news feed.
  - `404 Not Found`: No matching articles found.

### 2. Get User Preferences
- **Endpoint:** `/recommendations/feed/pref/{userId}`
- **Method:** `GET`
- **Path Parameter:**
  - `{userId}`: The unique identifier of the user.
- **Example Request:**

  ```sh
  curl -X GET http://localhost:8090/recommendations/feed/pref/1
  ```

- **Response Body:**

  ```json
  "Technology|AI|Finance"
  ```

- **Response Codes:**
  - `200 OK`: Successfully retrieved user preferences.
  - `404 Not Found`: User preferences not found.

## How to Run the Application
### Prerequisites:
- **Java 23** (or a compatible version)
- **Maven**
- Other required microservices (**User Service** & **News Service**) running.

### Steps:
1. **Clone the repository:**
   ```sh
   git clone <repository-url>
   cd recommendation-service
   ```
2. **Build the application using Maven:**
   ```sh
   mvn clean install
   ```
3. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```
   The application will start on **port 8090**.

## Technologies Used
- **Spring Boot**
- **Spring Cloud OpenFeign**
- **RESTful APIs**
- **Maven**
