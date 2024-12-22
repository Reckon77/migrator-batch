# **FinanceBridge – A Bridge for Seamless Financial Data Migration**

## **About the App**
**FinanceBridge** is a robust financial data migration tool designed to simplify and automate the transfer of financial data from MySQL to MongoDB. It ensures seamless migration with support for incremental data migration, multithreading, fault tolerance, and retry mechanisms.

This application is built with modern technologies to provide reliability, flexibility, and scalability for data migration needs.

---

## **Features**
- **MySQL to MongoDB Data Migration**: Automates the migration of data between relational and NoSQL databases.
- **Incremental Data Migration**: Only migrates new or updated records since the last run.
- **Multithreading**: Utilizes parallel processing for faster data migration.
- **REST Endpoint + Scheduler Migration**: Provides an endpoint (`/startJob`) for manual job execution and a scheduler for periodic automated runs.
- **Retry and Fault Tolerance**: Ensures resilience during failures with retry mechanisms and fault-tolerant design.
- **Listeners for Metadata Updates**: Automatically updates metadata upon job and step completion.

---

## **Folder Structure**
The project is organized into a modular and logical folder structure to enhance maintainability:

```
src/
└── main/
    ├── java/com/example/migrator/batch/
    │   ├── config/                 # Spring configuration files (job and step config)
    │   ├── controller/             # REST controllers
    │   ├── domain/                 # Entity and document classes
    │   ├── job/                    # Batch job configurations (reader, writer and processor)
    │   ├── listener/               # Listeners for job and step events
    │   ├── repository/             # Repositories for data persistence
    │   ├── scheduler/              # Scheduler for periodic job execution
    │   └── service/                # Services for business logic
    │       ├── BatchApplication    # Main application class
    │       └── GlobalExceptionHandler # Handles global exceptions
    └── resources/
        ├── application.yml         # Spring Boot application configuration
        ├── data.sql                # Sample data for MySQL
        ├── schema.sql              # MySQL schema definitions

```

## Steps to Start the Application

### Prerequisites

- Ensure Docker is installed and running on your system.
- Install Maven to build the project.

### Starting the Application

1. Build the project:
   ```bash
   mvn clean install
   ```

2. Start the services using Docker:
   ```bash
   docker-compose up -d
   ```

3. Access the application:
   - The server will be available at: `http://localhost:8080`

### Stopping the Application

1. To stop all running services:
   ```bash
   docker-compose down
   ```

2. To remove associated volumes (if necessary):
   ```bash
   docker-compose down --volumes
   ```

---

## How It Works

1. **Read the Data**:
   - Extracts user, account, and transaction data from MySQL that has not been migrated before.

2. **Process the Data**:
   - Converts and maps relational entities (rows) to MongoDB documents.

3. **Write the Data**:
   - Saves the processed data into the MongoDB database.

4. **Update Metadata**:
   - Uses writer and job completion listeners to update metadata, tracking the migration status.

5. **Scheduler Execution**:
   - Automatically triggers the migration job at specific intervals for seamless incremental updates.

6. **Manual Invocation**:
   - Users can manually trigger the migration job via the REST endpoint:
     ```
     GET /startJob
     ```

---

## Tech Stack

- **Java 17**: Core programming language for application development.
- **Spring Boot**: Framework for building scalable and robust applications.
- **MongoDB**: NoSQL database for document-oriented data storage.
- **MySQL**: Relational database for structured data.
- **Spring Batch**: Batch processing framework for data migration.
- **Spring Scheduler**: For scheduling jobs at fixed intervals.
- **Docker**: Containerization for consistent deployment.
- **Maven**: Build automation tool.
- **JPA**: For database interactions.

---
