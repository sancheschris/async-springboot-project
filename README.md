Async Spring Boot Application
This Spring Boot application demonstrates how to implement asynchronous operations using CompletableFuture and @Async annotation to handle multiple concurrent requests efficiently. It includes endpoints for uploading employee data from CSV files asynchronously and retrieving all employees either synchronously or asynchronously.

Features
Asynchronous Data Upload: Allows uploading CSV files containing employee data concurrently using @Async and CompletableFuture.

Asynchronous Data Retrieval: Retrieves all employee data using asynchronous processing for improved responsiveness.

Synchronous Data Retrieval: Provides a synchronous endpoint for comparison, demonstrating the benefits of asynchronous operations.

Getting Started
Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

Prerequisites
Ensure you have the following installed:

Java Development Kit (JDK) 8 or higher
Maven (for building and managing dependencies)
IDE (e.g., IntelliJ IDEA, Eclipse) or a text editor
Installing
Clone the repository:

bash
Copy code
git clone https://github.com/your/repository.git
cd async-springboot
Build the project using Maven:

bash
Copy code
mvn clean install
Running the Application
You can run the application either from your IDE or using Maven.

Run from IDE: Import the project into your preferred IDE and run AsyncSpringbootApplication.java as a Java application.

Run using Maven: Navigate to the project directory and run the following command:

bash
Copy code
mvn spring-boot:run
The application will start on http://localhost:8080.

Usage
Uploading Employee Data
Upload CSV Files:

Use the /employees endpoint with multipart/form-data to upload CSV files containing employee data.
Example using cURL:

bash
Copy code
curl -X POST -F "file=@employees.csv" http://localhost:8080/employees
Retrieving Employee Data
Get Employees Asynchronously:

Use the /getEmployeesByThread endpoint to retrieve all employees asynchronously.
Example using cURL:

bash
Copy code
curl http://localhost:8080/getEmployeesByThread
Get Employees Synchronously:

Use the /getEmployeesSync endpoint to retrieve all employees synchronously.
Example using cURL:

bash
Copy code
curl http://localhost:8080/getEmployeesSync
Performance
Logs in the console show the execution time for saving and retrieving employees both synchronously and asynchronously. Asynchronous operations demonstrate significantly faster processing times due to concurrent execution.
Configuration
The project includes configuration for asynchronous execution in AsyncConfig.java, where a thread pool (taskExecutor) is configured with core and max pool size.
Acknowledgments
This project was inspired by the need to demonstrate the benefits of asynchronous processing in Spring Boot applications.
