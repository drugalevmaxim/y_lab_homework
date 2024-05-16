# Y_Lab Training Diary

A workout diary app that lets you record your workouts, review them, and analyze your workout progress

# Note 

To auth as login use admin:admin. If you want to change it go to [002-insert-data.xml](src/main/resources/database/002-insert-data.xml) and change next rows:
```xml
<insert tableName="users">
    <column name="username" value="YOUR_ADMIN_USERNAME"/>
    <column name="password" value="YOUR_ADMIN_PASSWORD"/>
</insert>
```

# Requirements

- JDK 17
- Maven
- Docker
- Servlet Containers

# Build and run

1. Clone the repository by running the following command:
    ```bash
    git clone https://github.com/drugalevmaxim/y_lab_homework.git
    ```
2. Build the project by running the following command:
    ```bash
   mvn clean package
   ```
3. Initialize docker database by running the following command
   ```bash
   docker compose up -d
   ```
4. Copy package to your servlet container

# API Documentation

Documentation can be accessed via Swagger UI at ```.../swagger-ui/index.html```

# Completed tasks

1. [Task 1](../../pull/1/)
2. [Task 2](../../pull/2/)
3. [Task 3](../../pull/3/)
4. [Task 4](../../pull/4/)
