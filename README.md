# CareerStart

### 📋 Project Overview
CareerStart is a simple full-stack job portal application with a REST API backend built with Java/Spring Boot,
a React frontend, and PostgreSQL for database persistence. The app aims to connect employers and job seekers
through a role-based access system, enabling each user type to interact with the platform according to their permissions.

### 🚀 Features
- Jwt authentication & role-based access: Admin, Employer and Job Seeker roles with role-specific permissions
- Job listings: pagination, filtering, and posting
- Applications: Job seekers can apply to or withdraw from job listings
- CV management: Job seekers can create a CV profile or upload a CV file
- Profile management: Users can update personal info, upload profile pictures or deactivate their accounts
- Simple Admin dashboard: Admin can manage users and platform data 


### 🛠️ Technologies

#### 💻 Backend
- Java 21
- Spring Boot 3.5
- Spring Data JPA
- Spring Security
- JWT
- Maven

#### 🎨 Frontend
- React 19.2
- Vite
- Typescript
- React Router
- React Hook Form
- Zod
- TailwindCSS
- shadcn/ui

#### 🗄️ Database
- PostgreSQL 18
- Flyway

#### 🔧 Tools
- Docker
- Git / GitHub


### ⚙️ SetUp

#### 📌 Prerequisites
Ensure you have the following installed on your system
- Java 21
- Maven
- Docker Desktop
- Node.js and nom
- Git

#### 📝 Steps
1. Open a command prompt and navigate to the directory where you want to store the project
2. Clone the backend repository:
```
git clone https://github.com/dafnipapado/careerstart-backend
```
3. Clone the frontend repository in the same directory:
```
git clone https://github.com/dafnipapado/careerstart-frontend
```
4. Create a .env file for each repository using the corresponding .env.example file as a reference
5. In the backend repository, generate a secret value for JWT_SECRET_KEY, in git bash, using:
```
openssl rand -base64 64
```
Add the generated value to the backend .env file
6. Open the backend repository in your preferred IDE and create the Maven package:
```
mvn clean package -DskipTests
```
> Note: The project includes a Spring Boot context-load test. Running the tests requires PostgreSQL to be running
> and the necessary environment variables to be available to Spring Boot. The variables in .env are loaded automatically
> by Docker Compose, but are not automatically loaded when running Maven directly.
> If you want to run the tests, configure the required values in application.properties,
> or make them available as environment variables before running Maven:
> ```
> mvn clean package
> ```
> Otherwise, the tests can be skipped using the command above.
7. Start Docker Desktop and run the following command from the backend repository:
```
docker-compose up --build
```
This starts the backend application together with the PostgresSQL database. The backend will be available on port 8080.
8. Open the frontend in your preferred IDE or terminal and install its dependencies:
```
npm install
```
9. Start the frontend development server:
```
npm run dev
```
The frontend will be available at: http://localhost:5173 . Open this address in your web browser to access the application.