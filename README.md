# 4156-Miniproject-2025 I2 and I3

# IndividualProject

This is a Spring Boot project that simulates a simple library system. It provides a set of REST API endpoints to view books, check out books, add copies, and get recommended books. The project includes unit tests, code coverage reporting, and static analysis.

## Table of Contents
1. [Prerequisites](#prerequisites)  
2. [Setup](#setup)  
3. [Running the Application](#running-the-application)  
4. [API Endpoints](#api-endpoints)  
5. [Running Tests](#running-tests)  
6. [Code Quality & Coverage](#code-quality--coverage)   
7. [Notes](#notes)

---

## Prerequisites
Before running this project locally, ensure you have the following installed:

- Java 17 or higher  
- Maven 3.8 or higher  
- Git (optional, if cloning from a remote repository)  

---

## Setup
1. Clone the repository (or download the source code):

```bash
git clone <repository-url>
cd individualproject
```

2. Build the project and download dependencies using Maven:

```bash
mvn clean install
```

3. (Optional) Run the application directly from Maven:

bash
mvn spring-boot:run


---

## Running the Application
Once the project is running, you can access the API endpoints using a browser, Postman, or `curl`. By default, Spring Boot runs on `http://localhost:8080`.

Example:

```bash
curl http://localhost:8080/
```

You should see the home page welcome message.

---

## API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/` or `/index` | GET | Returns a welcome message |
| `/book/{id}` | GET | Returns the book with the specified ID |
| `/books/available` | GET | Returns all books that have at least one copy available |
| `/books/recommendation` | GET | Returns a list of recommended books (top 5 by popularity + random books) |
| `/checkout?id={bookId}` | POST | Checks out a copy of a book by ID |
| `/book/{bookId}/add` | PATCH | Adds a copy to the book with the specified ID |

---

## Running Tests
This project uses **JUnit 5** for unit testing. To run all tests:

```bash
mvn test
```

- `RouteControllerTest` covers all REST API logic  
- `MockApiServiceTest` covers service layer logic  
- `BookUnitTests` covers all Book model methods  

---

## Code Quality & Coverage

### PMD
Run static code analysis using PMD:

```bash
mvn pmd:check
```

- PMD will flag any code issues based on the configured rules.  
- Reports can be found under `target/site/pmd.html`.

### JaCoCo
Generate code coverage reports:

```bash
mvn jacoco:report
```

- Reports are located at `target/site/jacoco/index.html`  
- This shows line and branch coverage for all tested classes.

---



## Notes
The project uses a mock dataset; no real database is required.  
`MockApiService` reads from `resources/mockdata/books.json` if available, otherwise initializes an empty list.  
API returns standard HTTP status codes for success, not found, or conflicts.  



