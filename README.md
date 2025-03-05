# NewsWise

UserService 
```markdown
# User Service Application

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


```
