# Ecommerce Web Application with Springboot, Angular, Keycloak, and MySQL

## Overview

This repository contains the **backend** source code for an E-commerce web application built using SpringBoot for the backend, Angular for the frontend, Keycloak for authentication and authorization, and MySQL for the database. The application is specifically tailored for an online bookstore.

## Features

- **User Authentication**: Utilizes Keycloak for secure user authentication and authorization.
- **Product Management**: Allows administrators to manage products, including adding, updating, and deleting products.
- **Shopping Cart**: Users can browse products, add them to a shopping cart, and proceed to checkout.
- **Order Management**: Supports order creation, tracking, and history for users.
- **RESTful APIs**: Backend services are exposed via RESTful APIs, providing flexibility for future enhancements or integrations.

## Technologies Used

- **SpringBoot**: Java-based framework for building backend services.
- **Angular**: TypeScript-based framework for building dynamic web applications.
- **Keycloak**: Open-source Identity and Access Management solution for securing applications and services.
- **MySQL**: Relational database management system for data storage.
- **Angular Material**: Material Design components for Angular applications.
- **Bootstrap**: Frontend framework for responsive design.

## Features Images

Homepage:
![image](https://github.com/jhenals/ecommerce-frontend-angular-v1/assets/77573528/4d7a522e-fab8-4e7f-be54-d776a9b426fc)

Book Catalog:
![image](https://github.com/jhenals/ecommerce-frontend-angular-v1/assets/77573528/74a06461-9da8-496d-848f-3f22765bfb4b)

Special Offers:
![image](https://github.com/jhenals/ecommerce-frontend-angular-v1/assets/77573528/e0205707-6aaa-4757-b672-cd4783937d90)

Best Selling Books:
![image](https://github.com/jhenals/ecommerce-frontend-angular-v1/assets/77573528/44fcf2b1-55e6-4d57-a4c4-a9ffc23fad52)

Login/Registration Page
![image](https://github.com/jhenals/ecommerce-frontend-angular-v1/assets/77573528/c1c81b73-a264-4b6f-bcd7-9ff66d77c7e9)

Cart:
![image](https://github.com/jhenals/ecommerce-frontend-angular-v1/assets/77573528/43608294-1854-46af-8c22-a5b91229f001)

Checkout Form:
![image](https://github.com/jhenals/ecommerce-frontend-angular-v1/assets/77573528/5a2738b7-6079-49de-bdf6-8637d5d9445b)

User's Order History:
![image](https://github.com/jhenals/ecommerce-frontend-angular-v1/assets/77573528/ee0daa12-83f6-4eb3-9a94-3778352050ce)

Admin Dashboard- Manage products:
![image](https://github.com/jhenals/ecommerce-frontend-angular-v1/assets/77573528/2571fc8d-e3cd-4aa9-8cd3-1e60ce1668e9)

Admin Dashboard - Manage clients:
![image](https://github.com/jhenals/ecommerce-frontend-angular-v1/assets/77573528/31c5fc3d-fa9b-4ae7-be41-c80e77868b00)

Admin Dashboard - Manage Orders:
![image](https://github.com/jhenals/ecommerce-frontend-angular-v1/assets/77573528/82188534-25db-489b-b90d-bb577489b48c)

Searchbar:
![image](https://github.com/jhenals/ecommerce-frontend-angular-v1/assets/77573528/b1cf3c10-4fd0-4bef-a73e-24478fe160eb)


## Prerequisites

Before running the application, ensure you have the following installed:

- Java Development Kit (JDK)
- Node.js and npm
- Angular CLI
- MySQL Server
- Keycloak Server

## Installation

1. Clone the frontend repository:

   ```bash
   git clone https://github.com/jhenals/ecommerce-frontend-angular-v1.git
   ```

2. Clone the backend repository:

  ```bash
   git clone https://github.com/jhenals/ecommerce-backend-springboot-v1.git
   ```

3. Navigate to the backend directory and run the SpringBoot application:

   ```bash
   cd <YOUR_PATH>\ecommerce-backend
   ./mvnw spring-boot:run
   ```

4. Navigate to the frontend directory and install dependencies:

   ```bash
   cd <YOUR_PATH>\ecommerce-frontend
   npm install
   ```

5. Run the Angular application:

   ```bash
   npm start
   ```

6. Navigate to Keycloak directory and run Keycloak Server:

   ```bash
   cd <YOUR_PATH>\bin
   kc.bat start-dev
   ```

7. Access the application in your web browser at `http://localhost:4200`.

## Configuration

1. Configure MySQL database connection settings and Keycloak settings in `application.properties` file located in the `backend/src/main/resources` directory.

2. Configure Keycloak settings in the Angular environment files located in the `ecommerce-frontend\environment.ts` file.

## Usage

- Visit `http://localhost:4200` in your web browser to access the application.
- Use the provided login interface to authenticate. You can use the default admin credentials for testing or create a new account.
- Navigate through the bookstore, add products to your cart, and complete the checkout process.
---


