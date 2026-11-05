# Student Management System (Console-based Java & JDBC Project)

A complete console-based Student Management System built using **Core Java** and **JDBC** with **MySQL**.

---

## Project Structure

```
StudentManagementSystem/
├── sql/
│   └── schema.sql                 # MySQL database and table creation script
├── src/
│   └── com/
│       └── sms/
│           ├── model/
│           │   └── Student.java       # Entity class representing Student data
│           ├── util/
│           │   └── DBConnection.java  # JDBC connection factory
│           ├── dao/
│           │   └── StudentDAO.java    # Database operations (CRUD & Search)
│           └── main/
│               └── Main.java          # Menu-driven console UI and validation
└── README.md
```

---

## Features
- **Add a new student**: (ID, Name, Age, Course, Email, Phone) with uniqueness and format validation.
- **Update student details**: Modify records by ID, keeping existing values if preferred.
- **Delete a student**: Remove records by ID with user confirmation.
- **Search students**: Look up by unique ID or by substring match in student name.
- **View all students**: Formatted ASCII table display of all registered students.
- **Security & Integrity**: Uses `PreparedStatement` to prevent SQL Injection and validates inputs against regular expressions.

---

## Prerequisites
- **Java Development Kit (JDK)**: Version 8 or higher (Java 11, 17, or 21 recommended)
- **MySQL Server**: Version 8.0 or higher (or MariaDB)
- **MySQL Connector/J**: JDBC driver jar (e.g. `mysql-connector-j-8.3.0.jar`)

---

## Step-by-Step Setup Guide

### 1. Database Configuration
1. Open MySQL Command Line Client or MySQL Workbench.
2. Execute the setup script located in [`sql/schema.sql`](file:///c:/Users/RAGAVENDRA%20%20A%20S/OneDrive/Raghavendra%20A%20S/StudentManagementSystem/sql/schema.sql):
   ```sql
   SOURCE sql/schema.sql;
   ```
   Or manually run:
   ```sql
   CREATE DATABASE IF NOT EXISTS student_management_db;
   USE student_management_db;

   CREATE TABLE IF NOT EXISTS students (
       id INT PRIMARY KEY,
       name VARCHAR(100) NOT NULL,
       age INT NOT NULL,
       course VARCHAR(100) NOT NULL,
       email VARCHAR(100) NOT NULL UNIQUE,
       phone VARCHAR(15) NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

### 2. Configure JDBC Credentials
Open [`src/com/sms/util/DBConnection.java`](file:///c:/Users/RAGAVENDRA%20%20A%20S/OneDrive/Raghavendra%20A%20S/StudentManagementSystem/src/com/sms/util/DBConnection.java) and set your MySQL password and port if different:
```java
private static final String URL = "jdbc:mysql://localhost:3306/student_management_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_mysql_password"; // <-- Replace with your MySQL password
```

### 3. Add MySQL JDBC Driver
1. Download `mysql-connector-j-8.x.xx.jar` from the [MySQL Official Website](https://dev.mysql.com/downloads/connector/j/) or [Maven Central](https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/).
2. Create a `lib/` directory inside `StudentManagementSystem/` if it doesn't already exist.
3. Place `mysql-connector-j-8.x.xx.jar` inside the `lib/` directory.

---

## How to Compile & Run (Command Line)

From the `StudentManagementSystem/` root folder:

### Windows (PowerShell / Command Prompt):
```powershell
# 1. Create output folder for compiled class files
mkdir bin

# 2. Compile all source files with the JDBC driver in the classpath
javac -cp "lib/*" -d bin src/com/sms/model/Student.java src/com/sms/util/DBConnection.java src/com/sms/dao/StudentDAO.java src/com/sms/main/Main.java

# 3. Run the application
java -cp "bin;lib/*" com.sms.main.Main
```

### Linux / macOS:
```bash
# 1. Create output folder
mkdir -p bin

# 2. Compile
javac -cp "lib/*" -d bin src/com/sms/model/Student.java src/com/sms/util/DBConnection.java src/com/sms/dao/StudentDAO.java src/com/sms/main/Main.java

# 3. Run
java -cp "bin:lib/*" com.sms.main.Main
```

---

## Running in IDEs

### Eclipse:
1. Go to **File -> Open Projects from File System...** and select `StudentManagementSystem`.
2. Right-click project -> **Build Path -> Configure Build Path...** -> **Libraries** tab -> **Add External JARs...** and select `mysql-connector-j-8.x.xx.jar`.
3. Right-click [`Main.java`](file:///c:/Users/RAGAVENDRA%20%20A%20S/OneDrive/Raghavendra%20A%20S/StudentManagementSystem/src/com/sms/main/Main.java) -> **Run As -> Java Application**.

### IntelliJ IDEA:
1. Open the folder `StudentManagementSystem`.
2. Go to **File -> Project Structure -> Libraries**, click `+` -> **Java**, and select `lib/mysql-connector-j-8.x.xx.jar`.
3. Open [`Main.java`](file:///c:/Users/RAGAVENDRA%20%20A%20S/OneDrive/Raghavendra%20A%20S/StudentManagementSystem/src/com/sms/main/Main.java) and click the green **Run** arrow.

### Visual Studio Code:
1. Open the `StudentManagementSystem` folder.
2. In the **JAVA PROJECTS** explorer pane, locate **Referenced Libraries**, click `+`, and choose the MySQL `.jar` file in `lib/`.
3. Open [`Main.java`](file:///c:/Users/RAGAVENDRA%20%20A%20S/OneDrive/Raghavendra%20A%20S/StudentManagementSystem/src/com/sms/main/Main.java) and press `F5` or click **Run**.

---

## Validation Rules
- **Student ID**: Must be a positive integer and unique (duplicates are rejected).
- **Student Name**: Non-empty text string.
- **Age**: Integer value between `5` and `120`.
- **Course**: Non-empty text string.
- **Email**: Must conform to standard email regex format (`name@domain.ext`).
- **Phone Number**: Must contain between 10 to 15 digits (optional leading `+`).

---

## Troubleshooting Common Issues

| Issue | Cause | Solution |
| :--- | :--- | :--- |
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | MySQL Connector JAR is missing from classpath | Ensure `-cp "bin;lib/*"` is passed and the `.jar` is inside `lib/`. |
| `CommunicationsLinkException: Communications link failure` | MySQL Server is not running | Start the MySQL service via Windows Services (`services.msc`) or `net start mysql`. |
| `Access denied for user 'root'@'localhost'` | Incorrect MySQL password | Update the `PASSWORD` constant in [`DBConnection.java`](file:///c:/Users/RAGAVENDRA%20%20A%20S/OneDrive/Raghavendra%20A%20S/StudentManagementSystem/src/com/sms/util/DBConnection.java). |
| `Unknown database 'student_management_db'` | Database was not created | Run `CREATE DATABASE student_management_db;` in MySQL before starting the app. |


