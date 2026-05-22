# TrustSpace Backend

A production-ready Spring Boot backend for TrustSpace - a city-based property rental marketplace platform.

## 🚀 Tech Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT
- **ORM**: Hibernate (JPA)
- **API**: RESTful
- **Real-time**: WebSocket
- **Cloud Storage**: Cloudinary
- **Build Tool**: Maven
- **Mapping**: MapStruct
- **Documentation**: Lombok

## 📋 Prerequisites

- JDK 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Cloudinary account
- Gmail account (for email notifications)

## 🛠️ Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd trustspace-backend
```

### 2. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE trustspace_db;
```

### 3. Environment Configuration

Copy `.env.example` to `.env` and configure:

```bash
cp .env.example .env
```

Update the following in `.env`:

```properties
DB_USERNAME=your_postgres_username
DB_PASSWORD=your_postgres_password
JWT_SECRET=your_256_bit_secret_key
CLOUDINARY_CLOUD_NAME=your_cloudinary_cloud_name
CLOUDINARY_API_KEY=your_cloudinary_api_key
CLOUDINARY_API_SECRET=your_cloudinary_api_secret
MAIL_USERNAME=your_gmail@gmail.com
MAIL_PASSWORD=your_gmail_app_password
FRONTEND_URL=http://localhost:5173
```

### 4. Build the Project

```bash
./mvnw clean install
```

### 5. Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## 📁 Project Structure

```
trustspace-backend/
├── src/
│   ├── main/
│   │   ├── java/com/trustspace/
│   │   │   ├── config/           # Configuration classes
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── dto/              # Data Transfer Objects
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
│   │       └── application.yml   # Application configuration
│   └── test/                     # Test files
├── .env.example                  # Environment variables template
├── .gitignore
├── pom.xml                       # Maven configuration
└── README.md
```

## 🔑 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh-token` - Refresh access token
- `GET /api/auth/verify-email` - Verify email
- `POST /api/auth/forgot-password` - Request password reset
- `POST /api/auth/reset-password` - Reset password

### Users
- `GET /api/users/me` - Get current user
- `GET /api/users/{userId}` - Get user by ID
- `PUT /api/users/profile` - Update profile
- `POST /api/users/profile/image` - Upload profile image
- `PUT /api/users/change-password` - Change password
- `DELETE /api/users/account` - Delete account

### Properties
- `POST /api/properties` - Create property
- `GET /api/properties/public/{propertyId}` - Get property by ID
- `GET /api/properties/public/city/{cityId}` - Get properties by city
- `PUT /api/properties/{propertyId}` - Update property
- `DELETE /api/properties/{propertyId}` - Delete property
- `POST /api/properties/{propertyId}/images` - Upload images
- `GET /api/properties/my-properties` - Get owner's properties
- `GET /api/properties/public/search` - Search properties

### Bookings
- `POST /api/bookings` - Create booking
- `GET /api/bookings/my-bookings` - Get user's bookings
- `GET /api/bookings/owner` - Get owner's bookings
- `GET /api/bookings/{bookingId}` - Get booking details
- `PUT /api/bookings/{bookingId}/approve` - Approve booking
- `PUT /api/bookings/{bookingId}/reject` - Reject booking
- `PUT /api/bookings/{bookingId}/cancel` - Cancel booking

### Reviews
- `POST /api/reviews` - Create review
- `GET /api/reviews/property/{propertyId}` - Get property reviews
- `GET /api/reviews/my-reviews` - Get user's reviews
- `PUT /api/reviews/{reviewId}` - Update review
- `DELETE /api/reviews/{reviewId}` - Delete review

### Wishlist
- `POST /api/wishlist/{propertyId}` - Add to wishlist
- `DELETE /api/wishlist/{propertyId}` - Remove from wishlist
- `GET /api/wishlist` - Get user's wishlist
- `GET /api/wishlist/{propertyId}/check` - Check if in wishlist

### Chat
- `POST /api/chat/messages` - Send message
- `GET /api/chat/rooms` - Get chat rooms
- `GET /api/chat/rooms/{chatRoomId}/messages` - Get messages
- `PUT /api/chat/messages/{messageId}/read` - Mark as read
- `GET /api/chat/unread-count` - Get unread count

### Notifications
- `GET /api/notifications` - Get notifications
- `PUT /api/notifications/{notificationId}/read` - Mark as read
- `PUT /api/notifications/mark-all-read` - Mark all as read
- `GET /api/notifications/unread-count` - Get unread count
- `DELETE /api/notifications/{notificationId}` - Delete notification

### Cities
- `GET /api/cities` - Get all cities
- `GET /api/cities/trending` - Get trending cities
- `GET /api/cities/{cityId}` - Get city by ID
- `GET /api/cities/name/{name}` - Get city by name

### Amenities
- `GET /api/amenities` - Get all amenities

## 🔐 Security

- JWT-based authentication
- Role-based authorization (USER, OWNER, ADMIN)
- Password encryption with BCrypt
- Email verification
- Password reset functionality
- CORS configuration
- Rate limiting (to be implemented)

## 💾 Database Schema

The application uses PostgreSQL with the following main entities:

- **User**: User accounts and authentication
- **City**: Cities where properties are available
- **Property**: Property listings
- **PropertyImage**: Property images
- **Amenity**: Property amenities
- **PropertyAmenity**: Property-amenity relationship
- **Booking**: Property bookings
- **Review**: Property reviews
- **Wishlist**: User wishlist
- **ChatRoom**: Chat rooms between users
- **Message**: Chat messages
- **Notification**: User notifications
- **AvailabilitySlot**: Property availability

## 🌐 WebSocket

Real-time chat functionality using STOMP over WebSocket:

- Endpoint: `ws://localhost:8080/ws`
- Topics: `/topic/`, `/queue/`
- User destinations: `/user/`

## 📧 Email Notifications

Email notifications are sent for:
- Email verification
- Password reset
- Booking updates
- New messages
- Property updates

## 🐳 Docker Deployment

### Create Dockerfile

```dockerfile
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Create docker-compose.yml

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

### Run with Docker

```bash
# Build the application
./mvnw clean package -DskipTests

# Build and run with Docker Compose
docker-compose up --build
```

## 🧪 Testing

```bash
./mvnw test
```

## 📝 API Documentation

API documentation will be available at:
- Swagger UI: `http://localhost:8080/swagger-ui.html` (To be implemented)

## 🚦 Health Check

```bash
curl http://localhost:8080/actuator/health
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Authors

- TrustSpace Team

## 🐛 Issues

Report issues at: [GitHub Issues](your-repo-url/issues)

## 📞 Support

For support, email: support@trustspace.com
#   t r u s t s p a c e - b a c k e n d  
 