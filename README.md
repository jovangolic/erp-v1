ERP Backend System G-soft ERP for micro, small and medium-sized enterprises
Description:
G-soft is an ERP software developed with the aim of helping micro, small and medium-sized enterprises to improve, offer and expand their business.
It was developed using SOLID principles of software development, which makes it sustainable, easily upgradeable and suitable for long-term maintenance. It is these features that make G-soft a valuable product for IT companies and potential partners.

Current functionalities:
• Storage
• Logistics
• Accounting
• Material management
• Production planning
• Quality control
Disclaimer:

G-soft is not a copy of existing ERP systems, but an original solution tailored to local market needs.


    ⚠️ Disclaimer: This system is not a copy of any existing ERP solution, including SAP. SLAM is an original software product designed and developed from scratch.

    🎯 Slogan: “SLAM — Control your storage, move with logic, and balance with clarity.”

---

## 📦 Features

- 🔐 User authentication with role-based access control

- 🏬 Warehouse and inventory management

- 📦 Product tracking with real-time stock monitoring

- 📊 Employee shift reporting

- 🔄 Token-based authentication (with refresh token support)

- 🌐 RESTful API endpoints for all modules

- 🚚 Logistics tracking: orders, vehicle dispatching, shipment management

- 💰 Accounting features: invoicing, payment records, and reporting

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

- 🔐 Authentication Module – Manages user login, token lifecycle, and access security

- 👥 User & Role Management – Handles users, roles, and permission logic

- 🏬 Warehouse Module – Manages physical storage units and stored products

- 📦 Inventory Module – Tracks product quantities and locations

- 🕒 Shift Reports – Records employee work shifts and warehouse activity

- 🚚 Logistics Module – Handles transport orders, vehicle allocation, driver assignments, shipment tracking, and route planning

- 💰 Accounting Module – Manages invoices, payments, expense tracking, and balance sheets

---

## 📁 Project Structure

- 📁 model/ → JPA entities (User, Warehouse, Product, TransportOrder, Invoice...)
- 📁 controller/ → REST API endpoints for external communication
- 📁 service/ → Business logic and transaction handling
- 📁 repository/ → Spring Data JPA interfaces
- 📁 dto/ → Data Transfer Objects for safe data exchange
- 📁 security/ → JWT utilities and security configuration
- 📁 mapper/ → DTO <-> Entity mapping logic
- 📁 config/ → Application and security configuration files
- 📁 util/ → Utility classes (e.g., ApiMessages, CredentialGenerator, JsonUtil)

---

## 🧱 Architecture Overview

This backend follows a **layered architecture**:

- The backend follows a layered architecture:

- Controller Layer – Exposes RESTful endpoints for client communication

- Service Layer – Contains core business logic and orchestrates data flow

- Repository Layer – Interfaces with the DB using JPA/Hibernate

- Entity Layer – Models the domain with entities such as User, Warehouse, InventoryItem, etc.

🔐 Role-based Access Control is enforced using:

- ROLE_SUPERADMIN

- ROLE_ADMIN

- ROLE_STORAGE_FOREMAN

- ROLE_STORAGE_EMPLOYEE

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

3. **User Setup & Login Flow (using Postman)
   This section explains how to create users and log into the system using **Postman\*\*.

---

### 1. **Create Superadmin**

Use **Postman** to send a `POST` request to the appropriate user registration endpoint.

In the request body (JSON format), provide the following fields:

User Setup & Login Flow (using Postman)

This section explains how to create users and log in using Postman.

📍 Step 1: Create Superadmin

Send a POST request to the superadmin creation endpoint:

Endpoint: POST http://localhost:8080/users/create-superadmin

Request Body:

{
"firstName": "John",
"lastName": "Doe",
"address": "Main Street 1",
"phoneNumber": "0648888123",
"types": "SUPERADMIN"
}

📥 Response:

You will receive:

Auto-generated email (e.g., john.doe@firma.rs)

Auto-generated username (e.g., john_doe123)

Auto-generated password (10 characters, mix of uppercase, lowercase, digits, and symbols)

⚠️ Passwords are hashed and not recoverable. Copy and store them securely.

Step 2: Login as Superadmin

Endpoint: POST http://localhost:8080/auth/login

Request Body:
{
"identifier": "john.doe@firma.rs",
"password": "<generated_password>"
}
Once logged in, copy the returned JWT token and add it to the Authorization header:
Authorization: Bearer <your_token>

Admin & Employee Creation

Once logged in as Superadmin, you can create new users.

🧑‍💼 Admin

Endpoint: POST http://localhost:8080/users/create-admin

```json
{
  "firstName": "Dragan",
  "lastName": "Torbica",
  "phoneNumber": "0647654321",
  "address": "Detelinara 100, Novi Sad",
  "types": "ADMIN"
}
```

Login as Admin

Endpoint: POST http://localhost:8080/auth/login

```json
{
  "identifier": "dragan.torbica@firma.rs",
  "password": "<generated_password>"
}
```

Create Storage Foreman

Endpoint: POST http://localhost:8080/users/admin/create-user

```json
{
  "firstName": "Djordje",
  "lastName": "Cvarkov",
  "phoneNumber": "0641234123",
  "address": "Pejicevi salasi, Novosadski put 1",
  "types": "STORAGE_FOREMAN"
}
```

Create Storage Employee

Endpoint: POST http://localhost:8080/users/admin/create-user

```json
{
  "firstName": "Bosko",
  "lastName": "Boskic",
  "phoneNumber": "0634567891",
  "address": "Vase Stajica 10, Novi Sad",
  "types": "STORAGE_EMPLOYEE"
}
```

2. \*\*Postman Testing Summary

All major user roles (SUPERADMIN, ADMIN, STORAGE_FOREMAN, STORAGE_EMPLOYEE) can be created via Postman

JWT-based login is required to access protected endpoints

Credentials are returned only once upon creation — store them securely

Login uses identifier = generated email, and password = generated password

3. **Use the received JWT token**
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

# Jovan Golić - Author of this project.

📫 Contact

For questions, feel free to open an issue or reach out via GitHub:
🔗 github.com/jovangolic
For any inquiries or issues, please open an issue in the repository or contact me at jovangolic19@gmail.com.

# Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

# License

This project is licensed under the MIT License. See the LICENSE file for details.

# © 2025 SLAM ERP – All rights reserved.
