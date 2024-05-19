# Y_Lab Training Diary

A workout diary app that lets you record your workouts, review them, and analyze your workout progress

# Note

To auth as login use admin:admin. If you want to change it go
to [002-insert-data.xml](ylab-training-app/src/main/resources/database/002-insert-data.yml) and change next rows:

```yaml
          insert:
            tableName: person
            columns:
              - column:
                  name: username
                  value: 'default_admin'
              - column:
                  name: password
                  value: 'default_admin'
              - column:
                  name: role_id
                  value: 2
```

Please note that role_id must match admin role_id (2 by default)

# Requirements

- JDK 17
- Maven
- Docker

# Build and run

1. Clone the repository by running the following command:
    ```bash
    git clone https://github.com/drugalevmaxim/y_lab_homework.git
    ```
2. Initialize docker database by running the following command
   ```bash
   docker compose up -d
   ```
3. Build the project by running the following command:
    ```bash
   mvn clean install
   ```
4. Start project by running the following command:
    ```bash
    mvn spring-boot:run -pl ylab-training-app
   ```

# API Documentation

Documentation can be accessed via Swagger UI at ```.../swagger-ui/index.html```

# Completed tasks

1. [Task 1](../../pull/1/)
2. [Task 2](../../pull/2/)
3. [Task 3](../../pull/3/)
4. [Task 4](../../pull/4/)
5. [Task 5](../../pull/5/)
