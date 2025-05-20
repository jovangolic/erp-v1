# 🏭 ERP Backend System for Warehouse Management

This is the backend component of a modular ERP system tailored for **micro**, **small**, and **medium-sized enterprises (MSMEs)** to efficiently manage warehouse operations.  
> ⚠️ *Disclaimer: This is not a copy of SAP.*

---

## 📦 Features

- ✅ User authentication and role-based access control
- ✅ Warehouse and inventory management
- ✅ Product tracking and stock monitoring
- ✅ Shift and employee activity reporting
- ✅ Token-based session handling with support for refresh tokens
- ✅ RESTful API endpoints for all core features

---

## 🏗️ Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Security + JWT**
- **Hibernate (JPA)**
- **MySQL**
- **Lombok**

---

## 🧩 Modules

- **User & Role Management** – Handles users, roles, and permissions.
- **Warehouse Module** – Manages warehouses and products stored in them.
- **Inventory Module** – Tracks inventory items and stock counts.
- **Shift Reports** – Records employee shifts and activities.
- **Authentication Module** – Manages token lifecycle and access control.

---

## 📁 Project Structure
- **model/ --> JPA Entities (User, Warehouse, Product, Inventory...)
- **controller/ --> REST API entry points
- **service/ --> Business logic
- **repository/ --> Spring Data JPA Repositories
- **dto/ --> Data Transfer Objects
- **security/ --> JWT handling and auth config
- **mapper/ --> Custom mappers for DTO conversion

---

## 🧱 Architecture Overview

This backend follows a **layered architecture**:

- **Controller Layer (REST API)**  
  Exposes endpoints for clients to interact with resources like users, products, inventories, etc.

- **Service Layer**  
  Contains the business logic and orchestrates operations between controllers and repositories.

- **Repository Layer**  
  Interfaces with the database using Spring Data JPA.

- **Entity Layer**  
  Contains all domain models that represent database tables (e.g., `User`, `Warehouse`, `InventoryItem`, `Token`, etc.)

🔐 The system enforces **secure role-based access control** using:
- `ROLE_SUPERADMIN`
- `ROLE_ADMIN`
- `ROLE_STORAGE_FOREMAN`
- `ROLE_STORAGE_EMPLOYEE`

---

## ⚙️ Setup Instructions

### 📌 Prerequisites

- Java 17
- MySQL
- Maven
- Postman (for testing)

---

### 🔑 Authentication Setup (Using Postman)

To interact with protected endpoints, you must first **authenticate using JWT**.

#### Steps:

1. **Locate the JWT secret key**
   - Open `.env` file and copy the value of `JWT_SECRET`.

2. **Configure Postman Authorization**
   - Go to the **Authorization** tab.
   - Select `Bearer Token` or `JWT Bearer` (depending on Postman).
   - Set:
     - **Algorithm**: `HS512`
     - **Secret**: paste the copied JWT key
   - Leave other fields as default.

3. **User Creation Flow**
   - First, create a **Superadmin** account.
   - Then use that role to create Admins and regular employees.

4. **Use the received JWT token**
   - In every authorized request, set:
     ```
     Authorization: Bearer <your_token>
     ```

---

## ▶️ Run & Test

bash
mvn clean install -DskipTests
mvn spring-boot:run

Clone the repository: 
https://github.com/jovangolic/erp-v1.git
cd erp-v1

🧪 Postman Examples
👤 Superadmin Registration

Endpoint: POST http://localhost:8080/users/create-superadmin
Body:
🧑‍💼 Superadmin Registration
{
    "firstName": "Super",
    "lastName": "Admin",
    "email": "superadmin@example.com",
    "username": "superadmin",
    "password": "adminpassword",
    "phoneNumber": "123456789",
    "address": "Super Admin Address",
    "roleIds": [1]  
}
🔐 Login as Superadmin
Endpoint: POST http://localhost:8080/auth/login
{
  "identifier": "superadmin@example.com",
  "password": "yourpassword"
}

👤 Admin Registration

Endpoint: POST http://localhost:8080/users/create-admin
Body:
{
  "firstName": "Dragan",
  "lastName": "Torbica",
  "email": "dragan@gmail.com",
  "username": "draganGolf",
  "password": "dragan10",
  "phoneNumber": "0647654321",
  "address": "Detelinara 100, Novi Sad",
  "roleIds": [2]
}
🔐 Login as Admin
Endpoint: POST http://localhost:8080/auth/login
{
  "identifier": "dragan@gmail.com",
  "password": "dragan10"
}

👤 Users Registration
Login as Admin  POST http://localhost:8080/auth/login
{
  "identifier": "dragan@gmail.com",
  "password": "dragan10"
}
Then create employee and foreman
Endpoint: POST http://localhost:8080/users/admin/create-user
Body:
For ROLE_STORAGE_FOREMAN
{
    "firstName": "Djorje",
    "lastName": "Cvarkov",
    "email": "cvarkov@gmail.com",
    "username": "Papadubi",
    "password": "pilicar10",
    "phoneNumber": "0641234123",
    "address": "Pejicevi salasi, Novosadski put 1",
    "roleIds": [3]	
}
Endpoint: POST http://localhost:8080/users/admin/create-user
Body:
For ROLE_STORAGE_EMPLOYEE
{
    "firstName": "Bosko",
    "lastName": "Boskic",
    "email": "boskic@gmail.com",
    "username": "boskicUDB",
    "password": "boskic0",
    "phoneNumber": "0634567891",
    "address": "Vase Stajica 10, Novi Sad",
    "roleIds": [4]	
}

#Author

# Jovan Golić - Author of this project.

📫 Contact

For questions, feel free to open an issue or reach out via GitHub:
🔗 github.com/jovangolic
For any inquiries or issues, please open an issue in the repository or contact me at jovangolic19@gmail.com.


# Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

# License

This project is licensed under the MIT License. See the LICENSE file for details.
