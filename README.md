# HookFlow – Webhook Delivery & Retry Management Platform

HookFlow is a Java-based webhook delivery platform built using Spring Boot. It allows applications to create webhook events, deliver them to external endpoints, track delivery statuses, and handle failed deliveries through automatic retries.

The project demonstrates core backend engineering concepts such as REST API development, asynchronous processing, persistence, failure handling, and scheduled retry mechanisms.

## Key Features

- **Webhook Event Creation:** Create and persist webhook events through REST APIs.
- **Event Tracking:** Retrieve individual events and monitor their delivery status.
- **Webhook Delivery:** Send HTTP POST requests to registered destination URLs.
- **Automatic Retry Mechanism:** Retry failed webhook deliveries using a scheduled retry process.
- **Delivery Status Management:** Track events through PENDING, SUCCESS, and FAILED states.
- **Failure Handling:** Record failed attempts and enforce a maximum delivery attempt limit.
- **Webhook Dashboard:** Monitor total events, successful deliveries, and failed deliveries through a web interface.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Backend programming |
| Spring Boot | Application framework |
| Spring Web | REST API development |
| Spring Data JPA | Database operations |
| Hibernate | ORM |
| H2 Database | Data persistence |
| Thymeleaf | Dashboard rendering |
| Maven | Dependency management and build |

## Architecture

The application follows a layered architecture:

```text
Client / Postman
       |
       v
Webhook Controller
       |
       v
Webhook Service
       |
       +------> Webhook Repository
       |                |
       |                v
       |             H2 Database
       |
       v
Webhook Delivery
       |
       v
External Endpoint
       |
       v
Delivery Status Update
       |
       v
Retry Scheduler
```

## Webhook Delivery Workflow

1. A client submits a webhook event through the REST API.
2. The application validates and stores the event.
3. The event is initially assigned a PENDING status.
4. The delivery service sends an HTTP POST request to the destination URL.
5. Based on the response, the event status is updated to SUCCESS or FAILED.
6. Failed events are eligible for retry through the scheduled retry mechanism.
7. Once the maximum number of attempts is reached, the event remains FAILED.

## API Endpoints

Base URL: `http://localhost:8080`

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/webhooks` | Create a webhook event |
| GET | `/api/webhooks` | Retrieve all webhook events |
| GET | `/api/webhooks/{id}` | Retrieve a specific webhook event |
| POST | `/api/webhooks/{id}/deliver` | Trigger webhook delivery |

### 1. Create a Webhook

**POST** `/api/webhooks`

Request:

```json
{
  "eventType": "order.created",
  "destinationUrl": "http://localhost:8080/api/test-receiver",
  "payload": "{\"orderId\":101,\"amount\":2500}"
}
```

Expected response: `201 Created`

The newly created event is stored with a PENDING status.

### 2. Retrieve a Webhook

**GET** `/api/webhooks/1`

Returns the stored event, including its ID, event type, destination URL, payload, delivery status, and attempt count.

### 3. Deliver a Webhook

**POST** `/api/webhooks/1/deliver`

Triggers delivery to the configured destination endpoint.

A successful response updates the event status to SUCCESS. Failed delivery attempts are recorded and handled by the retry mechanism.

## Running Locally

### Prerequisites

- Java 21 or compatible JDK
- Git
- Maven (optional, as the project includes Maven Wrapper)

### Installation

Clone the repository:

```bash
git clone https://github.com/SanJil01/hookflow.git
```

Navigate to the project:

```bash
cd hookflow
```

Run the application on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application starts at:

`http://localhost:8080`

Open the dashboard in your browser:

`http://localhost:8080/`

## Testing

The REST APIs can be tested using Postman.

Important scenarios include:

- Creating webhook events.
- Retrieving stored events.
- Successful webhook delivery.
- Failed webhook delivery.
- Retry attempts for failed deliveries.
- Maximum delivery attempt handling.

## Learning Outcomes

Through this project, I explored:

- Building RESTful backend services using Spring Boot.
- Designing layered application architecture.
- Database persistence using Spring Data JPA.
- HTTP communication between services.
- Handling delivery failures and retry logic.
- Scheduling background tasks.
- Testing REST API workflows using Postman.

## Future Improvements

- Exponential backoff for retry scheduling.
- Idempotency support for webhook processing.
- HMAC-based webhook signature verification.
- Dead-letter queue for permanently failed events.
- Authentication and API key management.
- Docker-based deployment and integration testing.

---

**Built as a backend engineering project to explore reliable event delivery and distributed system concepts.**