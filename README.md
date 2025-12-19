# Hilos Verdes Clothing Store 🧵🌿

A full-stack e-commerce backend API built with **Java** and **Spring Boot** for a sustainable online clothing store. This project was developed as a capstone and simulates real-world shopping cart and order checkout functionality.

---

## 📌 Table of Contents

- About
- Features
- Tech Stack
- Getting Started
- Database Setup
- Running the Application
- API Endpoints
- Testing
- Future Improvements
- License

---

## 📘 About

**Hilos Verdes Clothing Store** is a RESTful backend application that supports core e-commerce features such as product browsing, shopping cart management, and order checkout. The application enforces authentication so only logged-in users can interact with protected resources like carts and orders.

---

## 🚀 Features

- User authentication and authorization
- Product listing and product detail retrieval
- Shopping cart creation and management
- Add, update, and remove cart items
- Checkout process that converts a cart into an order
- Order line items generated from cart contents
- Automatic cart clearing after checkout

---

## 🧰 Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- JDBC Template
- MySQL
- Maven

---

## 🧩 Getting Started

### Prerequisites

Make sure you have the following installed:

- Java SDK 17 or higher
- MySQL Server
- Maven
- An IDE (IntelliJ recommended)

---

## 🗄 Database Setup

## 🗄 Database Setup

1. Import the provided database schema and seed data (if applicable).

2. Update application.properties:

    spring.datasource.url=jdbc:mysql://localhost:3306/hilos_verdes_db  
    spring.datasource.username=YOUR_USERNAME  
    spring.datasource.password=YOUR_PASSWORD  
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver  

---

## ▶️ Running the Application

From the root directory of the project, run:

    mvn clean install
    mvn spring-boot:run

The application will start at:

    http://localhost:8080

---

## 📡 API Endpoints

### Products

    GET /products
    GET /products/{id}

### Shopping Cart (Authenticated)

    GET /cart
    POST /cart/{productId}
    PUT /cart/{productId}
    DELETE /cart/{productId}

### Orders (Checkout)

    POST /orders
    GET /orders
    GET /orders/{id}

---

## 🛠 Testing

You can test this API using Insomnia or Postman.

1. Set the base URL:

    http://localhost:8080

2. Authenticate as a user

3. Include authorization headers where required

4. Test cart and order workflows:
    - Add items to cart
    - Update quantities
    - Remove items
    - Checkout to create an order

---

## 💡 Future Improvements

- Frontend UI integration (React or Vue)
- Pagination and filtering for products
- Improved error handling and validation
- Order history and tracking
- Unit and integration testing

