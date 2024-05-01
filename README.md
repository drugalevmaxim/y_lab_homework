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

Endpoints, methods and request examples:
- POST /login
```json
{
  "username":"admin",
  "password":"admin"
}
```
- POST /register 
```json
{
  "username":"new_user", 
  "password":"password"
}
```
- GET /logout
- GET /trainingTypes
- POST /trainingTypes
```json
{
  "name": "New Training Type"
}
```
- GET /trainings
- get /trainings?startDate={date}&endDate={date}
- GET /trainings/{id}
- DELETE /trainings/{id}
- POST /trainings
```json
{
  "date": "2024-05-01", 
   "trainingType": {
      "id": 1
   }, 
   "trainingData": [
      {"name": "Optional 1", "value": 200}, 
      {"name": "Optional 2", "value": 200}
   ], 
   "duration": 10, 
   "burnedCalories": 10
}
```
- PUT /trainings/{id}
```json
{
  "date": "2024-04-30", 
   "trainingType": {
      "id": 2
   }, 
   "trainingData": [
      {"name": "Optional 1", "value": 220},
      {"name": "Optional 2", "value": 230},
      {"name": "Optional 3", "value": 240}
   ], 
   "duration": 20, 
   "burnedCalories": 30
}
```
- GET /audit

# Completed tasks

1. [Task 1](../../pull/1/)
2. [Task 2](../../pull/2/)