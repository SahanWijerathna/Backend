# ResQdrive - Backend Setup & Run Guide

This is the backend service for the ResQdrive project, built using **Spring Boot**, **Spring Security + JWT**, and **MySQL**.

---

## 🛠️ Prerequisites

Before you start, make sure you have the following installed on your machine:
* **Java Development Kit (JDK 17)** or higher
* **Docker Desktop** (used to run the MySQL database locally without manual configuration)
* **Git**

---

## 🚀 Setup and Running the Project

Follow these steps to get the backend running locally:

### 1. Database Setup (Docker)
We use Docker Compose to run a MySQL database instance. This saves us from having to manually create schemas or install local MySQL servers.

1. Open **Docker Desktop** and make sure it is running.
2. Open your terminal in the backend root directory (`bc/Backend`).
3. Run the following command to start the database container in detached mode:
   ```bash
   docker compose up -d
   ```
   *Note: This spins up a MySQL container on port `3307` with the database name `resqdrive`, username `root`, and password `rootpassword`.*

### 2. Package & Import Guidelines (⚠️ CRITICAL FOR THE TEAM)
Since we are collaborating on this project, we must ensure all package declarations are consistent. 
* Our base package is **`com.example.resqdrive`**.
* Whenever you add or edit files (Controllers, DTOs, Models, Repositories, etc.), make sure the package declaration at the top of the file uses `com.example.resqdrive` and **NOT** `com.resqdrive.backend`.
* Example:
  ```java
  package com.example.resqdrive.controller; // Correct
  ```

### 3. Running the Backend Server
Once the database container is up and running, you can start the Spring Boot application:

* On Windows (Command Prompt / PowerShell):
  ```powershell
  ./mvnw spring-boot:run
  ```
* On macOS / Linux:
  ```bash
  ./mvnw spring-boot:run
  ```

The backend server will start running on: **`http://localhost:8080`**

---

## 📂 Project Architecture

```text
src/main/java/com/example/resqdrive/
 ├── config/       # Security (CORS, SecurityConfig, JWT Filter/Util)
 ├── controller/   # REST Endpoints (Auth, Requests, Transactions, etc.)
 ├── dto/          # Data Transfer Objects (Request/Response schemas)
 ├── model/        # JPA Entities (User, RequestEntity, Transaction, etc.)
 └── repository/   # JPA Repositories (Database interfaces)
```

---

## 🤝 Team Workflow (Git Guidelines)

To prevent code overwrites and merge conflicts, follow this workflow:
1. Always `git pull origin development` before starting your work.
2. If you have local changes that you don't want to commit yet, run:
   ```bash
   git stash
   git pull origin development
   git stash pop
   ```
3. After making changes and verifying they compile successfully (using `./mvnw clean compile`), push your changes:
   ```bash
   git add .
   git commit -m "Your descriptive commit message"
   git push origin development
   ```