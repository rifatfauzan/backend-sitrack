# SiTrack - Tracking Information System for PT. Glorious Interbuana

![SiTrack Banner](backend-sitrack/src/main/resources/static/SiTrack%20-%20README.png)

SiTrack is a comprehensive logistics management system designed specifically for PT. Glorious Interbuana. This backend API provides RESTful services for managing transportation operations, from vehicle management and customer bookings to reporting and analytics.

## Features

### Data Management
- **Vehicle Management**: Registration, editing, and monitoring of vehicles
- **Driver Management**: Driver data management and assignment
- **Customer Management**: Customer database and contact information
- **Chassis Management**: Chassis and container load tracking
- **Asset Management**: Company asset inventory and tracking

### Operations
- **Customer Booking**: Customer booking and scheduling system
- **Vehicle Out/In**: Vehicle entry and exit tracking
- **SPJ (Surat Perintah Jalan)**: Waybill and documentation management
- **Asset Request**: Asset request and allocation system

### Reporting & Analytics
- **Dashboard Analytics**: Operational data visualization with charts and graphs
- **Truck Reports**: Vehicle condition and maintenance reports
- **Commission**: Commission calculation and tracking
- **Notification System**: Real-time notification system
- **Export Functionality**: PDF and Excel export capabilities

### User Management
- **Multi-role Access**: Admin, Manager, Supervisor, Operational, Mechanic
- **Authentication**: Secure JWT-based login system
- **Authorization**: Role-based access control

## Tech Stack

- Spring Boot 3.4.2
- Java 17
- PostgreSQL
- Spring Security + JWT
- Spring Data JPA

## Prerequisites

Ensure your system has:
- **Java** (version 17 or newer)
- **Gradle** (or use Gradle Wrapper)
- **PostgreSQL** (version 16 or newer)
- **Docker** (optional, for database setup)

## Installation & Setup

### 1. Clone Repository
```bash
git clone <repository-url>
cd backend-sitrack/backend-sitrack
```

### 2. Database Setup

#### Option A: Using Docker Compose
```bash
docker-compose up -d
```

#### Option B: Manual PostgreSQL Setup
Create a PostgreSQL database named `backend-sitrack-dev`

### 3. Environment Configuration
Create a `.env` file or set environment variables:
```env
DATABASE_URL_DEV=jdbc:postgresql://localhost:15001/backend-sitrack-dev
DEV_USERNAME=postgres
DEV_PASSWORD=secret99
JWT_SECRET_KEY=your-secret-key-here
SPRING_PROFILES_ACTIVE=dev
```

### 4. Build Project
```bash
./gradlew build
```

### 5. Run Application
```bash
./gradlew bootRun
```
The API will run at `http://localhost:8080`

### 6. Access API Documentation
Once running, access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## Development Guide

### Project Structure
```
backend-sitrack/
├── src/main/java/be_sitruck/backend_sitruck/
│   ├── config/              # Configuration classes
│   ├── model/               # Entity models
│   ├── repository/           # Data access layer
│   ├── restcontroller/      # REST API controllers
│   ├── restdto/             # Data transfer objects
│   ├── restservice/         # Business logic layer
│   ├── security/             # Security configuration
│   └── util/                # Utility classes
├── src/main/resources/
│   ├── application.yml       # Main configuration
│   ├── application-dev.yml   # Development profile
│   └── application-prod.yml  # Production profile
├── build.gradle
└── docker-compose.yml
```

### Available Scripts
```bash
./gradlew build          # Build the project
./gradlew bootRun        # Run the application
./gradlew test           # Run tests
./gradlew clean          # Clean build artifacts
```

### API Endpoints
The API provides RESTful endpoints for:
- `/api/auth` - Authentication endpoints
- `/api/users` - User management
- `/api/trucks` - Vehicle management
- `/api/orders` - Order management
- `/api/customers` - Customer management
- `/api/spj` - Waybill management
- `/api/assets` - Asset management
- `/api/reports` - Reporting endpoints
- And more...

Full API documentation available via Swagger UI.

## Deployment

### Railway Deployment
The application is configured for deployment on Railway:
- Port: Configurable via environment
- Database: PostgreSQL (managed service)
- Environment: Production profile

### Environment Variables
```env
DATABASE_URL=<production-database-url>
JWT_SECRET_KEY=<production-secret-key>
SPRING_PROFILES_ACTIVE=prod
```

## Security Features

- JWT-based authentication
- Role-based access control (RBAC)
- Spring Security integration
- Secure API communication
- Input validation and sanitization
- CORS configuration
