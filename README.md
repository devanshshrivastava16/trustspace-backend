# TrustSpace Backend

Production-ready Spring Boot API for a city-based property rental marketplace.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)
![Java](https://img.shields.io/badge/Java-17-orange)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-blue)
![License](https://img.shields.io/badge/License-MIT-purple)

---

## Tech stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.2.0 |
| Language | Java 17 |
| Database | PostgreSQL 12+ |
| Auth | Spring Security + JWT |
| ORM | Hibernate (JPA) |
| Real-time | WebSocket / STOMP |
| Storage | Cloudinary |
| Build | Maven + MapStruct + Lombok |

---

## Prerequisites

- JDK 17+
- Maven 3.6+
- PostgreSQL 12+
- Cloudinary account
- Gmail account (for email notifications)

---

## Quick start

### 1. Clone the repository

```bash
git clone <repository-url>
cd trustspace-backend
```

### 2. Create the database

```sql
CREATE DATABASE trustspace_db;
```

### 3. Configure environment

```bash
cp .env.example .env
```

Update `.env` with your values:

| Variable | Description |
|---|---|
| `DB_USERNAME` | PostgreSQL username |
| `DB_PASSWORD` | PostgreSQL password |
| `JWT_SECRET` | 256-bit secret key |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name |
| `CLOUDINARY_API_KEY` | Cloudinary API key |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret |
| `MAIL_USERNAME` | Gmail address for notifications |
| `MAIL_PASSWORD` | Gmail app password |
| `FRONTEND_URL` | CORS origin (e.g. `http://localhost:5173`) |

### 4. Build and run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

App starts at `http://localhost:8080`.

---

## Project structure

```
trustspace-backend/
├── src/
│   ├── main/
│   │   ├── java/com/trustspace/
│   │   │   ├── config/           # Configuration classes
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── dto/
│   │   │   │   ├── request/      # Request DTOs
│   │   │   │   └── response/     # Response DTOs
│   │   │   ├── entity/           # JPA entities
│   │   │   ├── enums/            # Enum types
│   │   │   ├── exception/        # Custom exceptions
│   │   │   ├── mapper/           # MapStruct mappers
│   │   │   ├── repository/       # JPA repositories
│   │   │   ├── security/         # Security configuration
│   │   │   ├── service/          # Business logic
│   │   │   ├── util/             # Utility classes
│   │   │   └── TrustSpaceApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
├── .env.example
├── pom.xml
└── README.md
```

---

## API reference

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/register` | Register new user |
| `POST` | `/api/auth/login` | User login |
| `POST` | `/api/auth/refresh-token` | Refresh access token |
| `GET` | `/api/auth/verify-email` | Verify email address |
| `POST` | `/api/auth/forgot-password` | Request password reset |
| `POST` | `/api/auth/reset-password` | Reset password |

### Users

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/users/me` | Get current user |
| `GET` | `/api/users/{userId}` | Get user by ID |
| `PUT` | `/api/users/profile` | Update profile |
| `POST` | `/api/users/profile/image` | Upload profile image |
| `PUT` | `/api/users/change-password` | Change password |
| `DELETE` | `/api/users/account` | Delete account |

### Properties

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/properties` | Create property |
| `GET` | `/api/properties/public/{propertyId}` | Get property by ID |
| `GET` | `/api/properties/public/city/{cityId}` | Get properties by city |
| `GET` | `/api/properties/public/search` | Search properties |
| `GET` | `/api/properties/my-properties` | Owner's property list |
| `PUT` | `/api/properties/{propertyId}` | Update property |
| `POST` | `/api/properties/{propertyId}/images` | Upload property images |
| `DELETE` | `/api/properties/{propertyId}` | Delete property |

### Bookings

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/bookings` | Create booking |
| `GET` | `/api/bookings/my-bookings` | Guest's bookings |
| `GET` | `/api/bookings/owner` | Owner's received bookings |
| `GET` | `/api/bookings/{bookingId}` | Booking details |
| `PUT` | `/api/bookings/{bookingId}/approve` | Approve booking |
| `PUT` | `/api/bookings/{bookingId}/reject` | Reject booking |
| `PUT` | `/api/bookings/{bookingId}/cancel` | Cancel booking |

### Reviews

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/reviews` | Create review |
| `GET` | `/api/reviews/property/{propertyId}` | Property reviews |
| `GET` | `/api/reviews/my-reviews` | My reviews |
| `PUT` | `/api/reviews/{reviewId}` | Update review |
| `DELETE` | `/api/reviews/{reviewId}` | Delete review |

### Wishlist

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/wishlist/{propertyId}` | Add to wishlist |
| `DELETE` | `/api/wishlist/{propertyId}` | Remove from wishlist |
| `GET` | `/api/wishlist` | My wishlist |
| `GET` | `/api/wishlist/{propertyId}/check` | Check if saved |

### Chat

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/chat/messages` | Send message |
| `GET` | `/api/chat/rooms` | List chat rooms |
| `GET` | `/api/chat/rooms/{chatRoomId}/messages` | Room messages |
| `PUT` | `/api/chat/messages/{messageId}/read` | Mark as read |
| `GET` | `/api/chat/unread-count` | Unread message count |

### Notifications

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/notifications` | All notifications |
| `PUT` | `/api/notifications/{notificationId}/read` | Mark as read |
| `PUT` | `/api/notifications/mark-all-read` | Mark all read |
| `GET` | `/api/notifications/unread-count` | Unread count |
| `DELETE` | `/api/notifications/{notificationId}` | Delete notification |

### Cities

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/cities` | All cities |
| `GET` | `/api/cities/trending` | Trending cities |
| `GET` | `/api/cities/{cityId}` | City by ID |
| `GET` | `/api/cities/name/{name}` | City by name |

### Amenities

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/amenities` | All amenities |

---

## WebSocket

Real-time chat over STOMP/WebSocket:

| | |
|---|---|
| Endpoint | `ws://localhost:8080/ws` |
| Broadcast topics | `/topic/`, `/queue/` |
| Per-user destinations | `/user/` |

---

## Security

- JWT Bearer token authentication
- Role-based authorization: `USER`, `OWNER`, `ADMIN`
- BCrypt password hashing
- Email verification on registration
- Password reset via email
- CORS configuration

---

## Database schema

Main entities: `User`, `City`, `Property`, `PropertyImage`, `Amenity`, `PropertyAmenity`, `Booking`, `Review`, `Wishlist`, `ChatRoom`, `Message`, `Notification`, `AvailabilitySlot`.

---

## Email notifications

Sent for: email verification, password reset, booking status updates, new messages, and property updates.

---

## Docker

### Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### docker-compose.yml

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: trustspace_db
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/trustspace_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
    depends_on:
      - postgres

volumes:
  postgres_data:
```

### Run

```bash
./mvnw clean package -DskipTests
docker-compose up --build
```

---

## Testing

```bash
./mvnw test
```

---

## Health check

```bash
curl http://localhost:8080/actuator/health
```

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes
4. Push to the branch
5. Open a pull request

---

## License

MIT License — see [LICENSE](LICENSE) for details.

---

## Support

For issues, open a ticket on [GitHub Issues](<repository-url>/issues).  
For support, email: support@trustspace.com
