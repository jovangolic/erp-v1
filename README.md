ERP Backend System — SLAM (Storage, Logistics and Accounting Management)

This is the backend component of SLAM, a modular ERP system designed specifically for micro, small, and medium-sized enterprises (MSMEs) to streamline warehouse, logistics, and accounting operations.

    ⚠️ Disclaimer: This system is not a copy of any existing ERP solution, including SAP. SLAM is an original software product designed and developed from scratch.

    🎯 Slogan: “SLAM — Control your storage, move with logic, and balance with clarity.”

---

## 📦 Features

- ✅ User authentication and role-based access control

- ✅ Warehouse and inventory management

- ✅ Product tracking and real-time stock monitoring

- ✅ Shift and employee activity reporting

- ✅ Token-based session handling with refresh token support

- ✅ RESTful API endpoints for all major operations

- ✅ Logistics management — transport orders, shipments, vehicle tracking

- ✅ Accounting module — invoices, payments, and financial reporting

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
- 📁 model/         → JPA entities (User, Warehouse, Product, TransportOrder, Invoice...)
- 📁 controller/    → REST API endpoints for external communication
- 📁 service/       → Business logic and transaction handling
- 📁 repository/    → Spring Data JPA interfaces
- 📁 dto/           → Data Transfer Objects for safe data exchange
- 📁 security/      → JWT utilities and security configuration
- 📁 mapper/        → DTO <-> Entity mapping logic
- 📁 config/        → Application and security configuration files
- 📁 util/          → Utility classes (e.g., ApiMessages, CredentialGenerator, JsonUtil)

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
```json
{
    "firstName":"Milan",
    "lastName":"Torbica",
    "address":"Koste Racina 11, Novi Sad",
    "phoneNumber":"0648888123",
    "types":"SUPERADMIN"
}
When you populated necessary fields as superadmin using postman, in the postman body you'll get these answers as a response:
generated an email address, generated an username and generated a random password, which is consists of 10 characters (including All caps Alphabet, small caps Alphabet, numbers 0-9, and special characters) mix together

```
🔐 Login as Superadmin
Endpoint: POST http://localhost:8080/auth/login
```json
{
  "identifier": "milan.torbica@firma.rs",
  "password": use the password which is generated in postman body after filling necessary fields
}
```
👤 Admin Registration

Endpoint: POST http://localhost:8080/users/create-admin
Body:
```json
{
  "firstName": "Dragan",
  "lastName": "Torbica",
  "phoneNumber": "0647654321",
  "address": "Detelinara 100, Novi Sad",
  "types": ADMIN
}
```
🔐 Login as Admin
Endpoint: POST http://localhost:8080/auth/login
```json
{
  "identifier": "dragan.torbica@firma.rs",
  "password": use the password which is generated in postman body after filling necessary fields
}
```

👥 Creating Employees (as Admin)

You must be logged in as Admin to create users with other roles.

📋 ROLE_STORAGE_FOREMAN

Endpoint: POST http://localhost:8080/users/admin/create-user
Body:
For ROLE_STORAGE_FOREMAN
```json
{
    "firstName": "Djorje",
    "lastName": "Cvarkov",
    "phoneNumber": "0641234123",
    "address": "Pejicevi salasi, Novosadski put 1",
    "types": STORAGE_FOREMAN
}
```
👥 Create Storage Employee (ROLE_STORAGE_EMPLOYEE)

Endpoint:
POST http://localhost:8080/users/admin/create-user
Body:
```json
{
    "firstName": "Bosko",
    "lastName": "Boskic",
    "phoneNumber": "0634567891",
    "address": "Vase Stajica 10, Novi Sad",
    "types": STORAGE_EMPLOYEE	
}
```

# Jovan Golić - Author of this project.

📫 Contact

For questions, feel free to open an issue or reach out via GitHub:
🔗 github.com/jovangolic
For any inquiries or issues, please open an issue in the repository or contact me at jovangolic19@gmail.com.


# Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

# License

This project is licensed under the MIT License. See the LICENSE file for details.
