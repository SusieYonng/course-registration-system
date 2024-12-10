## Project Introduction
This project serves as the **backend for a course registration system**, initialized using **Spring Initializr** with the following dependencies:

- Spring Boot DevTools: For development and debugging.
- Spring Web: To build REST APIs.
- Spring Data JPA: For database interaction.
- MySQL Driver: To connect with the MySQL database.
- Spring Security: For user authentication.

The system includes a login and logout module that employs JWT-based authentication and a blacklist mechanism for enhanced security.

Currently, the system supports two roles:
1. **STUDENT**: Can search for, add, and drop courses, as well as view their selected courses and schedules 
2. **ADMIN**: Can manage courses and students, including adding/editing/deleting courses and adding/removing students from courses.
 
## Project Setting
This project uses Java 17 with the Spring Boot framework and is managed by Maven (3.3.5).
In VS Code, you can use the shortcut **Ctrl/Command + Shift + P**, then select Configure Java Runtime to view Project Settings.

## Database Configuration
This project uses MySQL for database operations. Relevant configurations can be found in the `src/main/resources/application.properties` file.

Ensure the application uses the following unified credentials:
```shell
spring.datasource.url=jdbc:mysql://localhost:3306/course_registration
spring.datasource.username=springuser
spring.datasource.password=123456
```
### MySQL Database Operations
To run your code locally, make sure MySQL is installed on your computer.

Before running the application, ensure that a database named course_registration exists.
If the course_registration database does not exist, you can create one and then switch to the newly created database using the following MySQL commands:
```sql
CREATE DATABASE course_registration;
USE course_registration
```
If your local database does not have a springuser user, it is recommended to create one. Otherwise, you will need to update the spring.datasource.username and spring.datasource.password values in the application.properties file. Use the following MySQL commands to create the user:
```sql
CREATE USER 'springuser'@'localhost' IDENTIFIED BY '123456';
GRANT ALL PRIVILEGES ON course_registration.* TO 'springuser'@'localhost';
```

## Project Compilation and Testing
**POM File**
The POM (Project Object Model) file is the core configuration file for Maven, typically named **pom.xml**.
It describes the project's basic information, dependencies, build process, and plugin configurations.

### Common Maven Commands
mvn clean – Cleans the project (deletes the target directory).
mvn compile – Compiles the project.
mvn test – Runs the tests.
mvn package – Packages the project (generates a JAR or WAR file).
mvn install – Installs the project into the local repository.
mvn dependency:tree – Displays the dependency tree of the project.

1. Use the command `mvn clean install` to remove old compiled files and reinstall dependencies.
2. When you want to skip Test:
- Use `mvn clean install -DskipTests` if the test code does not need to run but should still be compiled.
- Use `mvn clean install -Dmaven.test.skip=true` to completely skip tests, meaning the test code will neither run nor compile.

## Run the Application
The application entry is located at: `src/main/java/com/group3/course_registration_system/CourseRegistrationSystemApplication.java`

When the application is run for the first time, it will initialize the MySQL database automatically, creating tables corresponding to the entities defined within the project.

## Run Data Generation
To generate sample data and insert it into the database, execute the **DataGenerationController.java** file located in the `src/main/java/com/group3/course_registration_system/util/` directory.

During execution, the program will generate two files: `user_data.csv` and `course_data.csv`. Once the process completes successfully (you can verify the data insertion in MySQL), use the **username** and **raw_password** from user_data.csv to log into the system.

## Swagger-UI
When the service is running locally, you can explore and test the APIs via the Swagger-UI interface at: `http://localhost:8080/swagger-ui/index.html`

### Specific Steps:
1. Open the Swagger UI interface.
2. First, find the `api/auth/login` endpoint under the Authentication Controller. Use known user credentials to log in, and upon successful login, obtain the **token** returned by the endpoint.
3. In the top-right corner, locate the button labeled **"Authorize"** (usually with an open lock icon 🔓 next to it).
4. Click the Authorize button, and a dialog will pop up prompting you to enter the authentication information.
5. Paste the valid token obtained from the login into the provided field and click the Authorize button to submit. Then, close the dialog.

### Status Explanation:
1. **Lock icon in the closed state** 🔒: This indicates that authentication has been successfully applied.
At this point, Swagger will automatically add the Authorization header to all requests that require authentication, and you no longer need to manually input it.
2. **Lock icon in the open state** 🔓: This indicates that authentication has not yet been applied.
In this state, you will need to manually input the Authorization header for each request, or first click the Authorize button to perform global authentication.

**Note**: Of course, you can try requesting other endpoints without entering a valid token, but the response is typically 401, indicating unauthorized access.


### Endpoint Permissions:
- Except for the login and logout endpoints, all other endpoints under the Course Management Controller and Student Controller require administrator permissions (by logging in with an admin role).
    - However, the two course-related endpoints (GET /api/courses and GET /api/courses/{id}) can also be accessed by students.
- The Student-Course Controller can only be accessed by users with the student role.