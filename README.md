# ERP Backend System for Warehouse Management

This is the backend component of a modular ERP system tailored for micro, small and medium-sized enterprises (MSMEs) to efficiently manage warehouse operations.

## 📦 Features

- User authentication and role-based access control
- Warehouse and inventory management
- Product tracking and stock levels
- Shift and employee activity reporting
- Token-based session handling with support for refresh tokens
- RESTful API endpoints for all core features

## 🏗️ Tech Stack

- Java 17
- Spring Boot
- Spring Security + JWT
- Hibernate (JPA)
- MySQL
- Lombok

## 🔧 Modules

- **User & Role Management** – Handles users, roles, and permissions.
- **Warehouse Module** – Manages warehouses and products stored in them.
- **Inventory Module** – Tracks inventory items and stock counts.
- **Shift Reports** – Records employee shifts and activities.
- **Authentication Module** – Manages token lifecycle and access control.

## 📁 Project Structure
- **model/ --> JPA Entities (User, Warehouse, Product, Inventory...)
- **controller/ --> REST API entry points
- **service/ --> Business logic
- **repository/ --> Spring Data JPA Repositories
- **dto/ --> Data Transfer Objects
- **security/ --> JWT handling and auth config
- **mapper/ --> Custom mappers for DTO conversion

# Architecture Description (Moderate Complexity)
This backend follows a layered architecture:

    Controller Layer (REST API): Exposes endpoints for clients to interact with resources like users, products, inventories, etc.

    Service Layer: Contains the business logic and orchestrates operations between controllers and repositories.

    Repository Layer: Interfaces with the database using Spring Data JPA.

    Entity Layer: Contains all domain models that represent the database tables (e.g., User, Warehouse, InventoryItem, Token, etc.)

The project enforces separation of concerns, secure access via roles (ROLE_ADMIN, ROLE_STORAGE_EMPLOYEE, ROLE_STORAGE_FOREMAN), and integrates JWT-based authentication for session control.

## Setting up the Project
# Prerequisites
Authentication Setup (Using Postman)

To interact with the API, you must authenticate using JWT. Follow these steps to configure Postman:

    Locate the JWT secret
    Open the .env file in the project root and copy the value of the JWT_SECRET property.

    Open Postman and go to the Authorization tab.

    From the drop-down menu, select "Bearer Token" or "JWT Bearer" (depending on your Postman version and plugins).

    Set the following parameters:

        Algorithm: HS512

        Secret: paste the secret key copied from the .env file

        Leave other fields as default

    Use the appropriate endpoint to create the first user:

        Create a super admin user first (required to manage the system)

        Then proceed to create admin and regular employee accounts

    After logging in, you will receive a JWT token.

        Use this token in the Authorization header as Bearer <token> for all protected routes.

## 🧪 Run & Test

bash
mvn clean install -DskipTests
mvn spring-boot:run

Clone the repository: 
https://github.com/jovangolic/erp-v1.git
cd erp-v1
















































#Author

# Jovan Golić - Author of this project.

# License
