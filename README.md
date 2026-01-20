# 📦 SmartStock - Inventory Management System

![Project Status](https://img.shields.io/badge/status-active-success.svg)
![Database](https://img.shields.io/badge/database-PostgreSQL-blue.svg)


**SmartStock** is a comprehensive **Inventory Management System** designed to streamline the process of tracking stock levels, managing product databases, and organizing supplier relationships. It provides a robust backend architecture to ensure data integrity and efficient stock control for businesses.

---

## 📋 Table of Contents
- [Overview](#-overview)
- [Key Features](#-key-features)
- [Database Structure](#-database-structure)
- [Technologies Used](#-technologies-used)
- [Installation & Setup](#-installation--setup)
- [Usage](#-usage)
- [License](#-license)

---

## 🎯 Overview

Efficient stock management is critical for minimizing loss and maximizing operational efficiency. **SmartStock** solves common inventory challenges by offering a centralized database to monitor product lifecycle, from procurement to sale.

**Core Objectives:**
* Real-time tracking of inventory levels.
* Preventing stockouts and overstocking.
* Managing supplier and customer data efficiently.
* Generating insightful reports on stock movements.

---

## 🚀 Key Features

* **Product Management:** Add, update, delete, and categorize products with ease.
* **Stock Tracking:** Real-time monitoring of current stock quantities and reorder levels.
* **Supplier Database:** Maintain detailed records of suppliers and purchase history.
* **Transaction Logging:** Record all incoming and outgoing stock movements for audit trails.
* **Low Stock Alerts:** (Optional) Notification system for items falling below safety stock levels.

---

## 🗂 Database Structure

The core of SmartStock relies on a relational database design (ERD) including:
* **Products Table:** Stores SKU, name, price, and category.
* **Suppliers Table:** Contact info and supply history.
* **Inventory Table:** Current stock counts and warehouse locations.
* **Transactions Table:** Logs of sales and purchases.

---

## 🛠 Technologies Used

| Category | Technology |
|----------|------------|
| **Database** | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white) |
| **Language** | ![Java] / SQL |
| **Tools** | pgAdmin 4, VS Code |

---

## 💻 Installation & Setup

Follow these steps to set up the database locally:

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/BayramEnesAtay/SmartStock.git](https://github.com/BayramEnesAtay/SmartStock.git)
    ```

2.  **Navigate to the project directory:**
    ```bash
    cd SmartStock
    ```

3.  **Database Configuration:**
    * Ensure **PostgreSQL** is installed and running.
    * Create a new database named `smartstock_db`.

4.  **Import Schema:**
    Run the SQL script to create tables and relationships:
    ```bash
    psql -U your_username -d smartstock_db -f schema.sql
    ```
    *(Note: Replace `schema.sql` with the actual name of your SQL dump file).*

---

## ⚙️ Usage

Once the database is set up:

1.  **Connect** to the database using your preferred tool (e.g., Python script or pgAdmin).
2.  **Execute Queries** to manage inventory:
    * *Add new stock:*
        ```sql
        INSERT INTO products (name, quantity, price) VALUES ('Wireless Mouse', 50, 25.00);
        ```
    * *Check low stock:*
        ```sql
        SELECT * FROM products WHERE quantity < 10;
        ```

---
