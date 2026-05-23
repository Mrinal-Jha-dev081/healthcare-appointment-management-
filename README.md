# Healthcare Appointment Platform

A scalable healthcare appointment management platform built with **Spring Boot**, **Python**, **RabbitMQ**, and **React**.

## Architecture

```
┌──────────────────┐     ┌─────────────────┐     ┌──────────────────────┐
│  React Frontend  │────▶│  Spring Boot    │────▶│     RabbitMQ         │
│  (Vite + TS)     │     │  (REST API)     │     │  (Message Broker)    │
└──────────────────┘     └─────────────────┘     └──────────┬───────────┘
                                  ▲                          │
                                  │                          ▼
                                  │              ┌──────────────────────┐
                                  └──────────────│  Python Notification │
                                   status update │  Service (Consumer)  │
                                                 └──────────────────────┘
```

### Workflow
1. User books appointment via React frontend
2. Spring Boot validates, persists to H2 database, publishes event to RabbitMQ
3. Python service consumes event, simulates email notification
4. Python service calls back Spring Boot to update appointment status
5. Frontend polls and displays real-time status updates

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.2, Java 17 |
| Worker Service | Python 3.11 |
| Database | H2 (file-based) |
| Messaging | RabbitMQ |
| Frontend | React 18 + Vite + TypeScript + Tailwind CSS |
| Auth | JWT (jjwt) |
| API Docs | SpringDoc OpenAPI (Swagger) |
| Containerization | Docker + Docker Compose |

## Features

- **User Authentication** - JWT-based register/login
- **Appointment Booking** - Browse doctors, select date/slot, book
- **Appointment Cancellation** - Cancel with slot auto-release
- **Duplicate Prevention** - Unique constraint + optimistic locking
- **Concurrent Request Handling** - @Version-based optimistic locking
- **Event-driven Notifications** - RabbitMQ for async processing
- **Status Tracking** - Full appointment lifecycle with audit logs
- **Visual Status Flow** - Real-time UI status progression

## Quick Start

### Prerequisites
- Docker & Docker Compose installed

### Run with Docker Compose

```bash
# Clone the repository
git clone <repo-url>
cd healthcare-appointment-platform

# Start all services
docker-compose up --build
```

### Access Points

| Service | URL |
|---------|-----|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| RabbitMQ Management | http://localhost:15672 (guest/guest) |
| H2 Console | http://localhost:8080/h2-console |

### Local Development (without Docker)

**Backend:**
```bash
cd backend
./mvnw spring-boot:run
```

**Frontend:**
```bash
cd frontend
npm install
npm run dev
```

**Python Service:**
```bash
cd notification-service
pip install -r requirements.txt
python main.py
```

> Note: You need RabbitMQ running locally on port 5672 for local development.

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new patient |
| POST | `/api/auth/login` | Login and get JWT |

### Appointments (requires JWT)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/appointments` | Book appointment |
| GET | `/api/appointments` | Get user's appointments |
| DELETE | `/api/appointments/{id}` | Cancel appointment |

### Slots (requires JWT)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/slots?doctorId=&date=` | Get available slots |
| GET | `/api/slots/doctors` | Get all doctors |

### Internal (service-to-service)
| Method | Endpoint | Description |
|--------|----------|-------------|
| PUT | `/api/internal/appointments/{id}/status` | Update appointment status |

## Database Schema

### Core Tables
- **users** - Patient accounts (email, password, name, role)
- **doctors** - Doctor registry (name, specialization)
- **slots** - Available time slots (doctor, date, time, availability)
- **appointments** - Bookings (patient, doctor, slot, status, version)
- **appointment_logs** - Status change audit trail

### Appointment Status Flow
```
CONFIRMED → NOTIFICATION_SENDING → NOTIFICATION_SENT
    ↓
CANCELLED
```

## Event Flow (RabbitMQ)

**Exchange:** `appointment.events` (Topic)

**Queues:**
- `appointment.created.queue` → Python processes booking confirmations
- `appointment.cancelled.queue` → Python processes cancellation notices

**Event Payload:**
```json
{
  "eventType": "APPOINTMENT_CREATED",
  "appointmentId": 1,
  "patientName": "John Doe",
  "patientEmail": "john@example.com",
  "doctorName": "Dr. Sarah Johnson",
  "date": "2024-01-15",
  "startTime": "09:00",
  "endTime": "09:30",
  "timestamp": "2024-01-15T08:30:00"
}
```

## Project Structure

```
healthcare-appointment-platform/
├── docker-compose.yml
├── .env
├── README.md
├── backend/                          # Spring Boot
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/com/healthcare/
│       ├── config/                   # Security, RabbitMQ, Swagger
│       ├── controller/               # REST endpoints
│       ├── dto/                      # Request/Response objects
│       ├── exception/                # Global error handling
│       ├── model/                    # JPA entities
│       ├── repository/               # Data access
│       ├── security/                 # JWT filter & util
│       └── service/                  # Business logic
├── notification-service/             # Python
│   ├── Dockerfile
│   ├── requirements.txt
│   ├── main.py                       # RabbitMQ consumer
│   ├── notification_handler.py       # Email simulation
│   └── callback_client.py           # Status update client
└── frontend/                         # React + Vite
    ├── Dockerfile
    ├── nginx.conf
    ├── package.json
    └── src/
        ├── api/                      # Axios HTTP client
        ├── components/               # Reusable UI components
        ├── context/                  # Auth context
        └── pages/                    # Page components
```

## Seed Data

The application comes pre-loaded with:
- **4 Doctors** - Cardiology, Dermatology, Orthopedics, General Medicine
- **Time Slots** - 30-minute slots across today and tomorrow for all doctors

## Security

- JWT-based stateless authentication
- BCrypt password hashing
- CORS configured for frontend origins
- Service-to-service auth via X-Internal-Key header
- Input validation on all endpoints
- SQL injection prevention via JPA parameterized queries
