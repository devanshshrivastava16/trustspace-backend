# TrustSpace API Documentation

Base URL: `http://localhost:8080/api`

## Table of Contents
- [Authentication](#authentication)
- [Users](#users)
- [Properties](#properties)
- [Bookings](#bookings)
- [Reviews](#reviews)
- [Wishlist](#wishlist)
- [Chat](#chat)
- [Notifications](#notifications)
- [Cities](#cities)
- [Amenities](#amenities)

---

## Authentication

### Register
**POST** `/auth/register`

Register a new user account.

**Request Body:**
```json
{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "Password123",
  "phone": "9876543210",
  "cityId": 1
}
```

**Response:** `201 Created`
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "userId": 1,
  "fullName": "John Doe",
  "email": "john@example.com",
  "role": "USER",
  "isVerified": false
}
```

---

### Login
**POST** `/auth/login`

Authenticate user and get JWT tokens.

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "Password123"
}
```

**Response:** `200 OK`
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "userId": 1,
  "fullName": "John Doe",
  "email": "john@example.com",
  "role": "USER",
  "isVerified": true
}
```

---

### Refresh Token
**POST** `/auth/refresh-token`

Get new access token using refresh token.

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response:** `200 OK`
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "userId": 1,
  "fullName": "John Doe",
  "email": "john@example.com",
  "role": "USER",
  "isVerified": true
}
```

---

### Verify Email
**GET** `/auth/verify-email?token={token}`

Verify user email address.

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Email verified successfully"
}
```

---

### Forgot Password
**POST** `/auth/forgot-password`

Request password reset email.

**Request Body:**
```json
{
  "email": "john@example.com"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Password reset email sent"
}
```

---

### Reset Password
**POST** `/auth/reset-password`

Reset password using token from email.

**Request Body:**
```json
{
  "token": "reset-token-from-email",
  "newPassword": "NewPassword123"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Password reset successfully"
}
```

---

## Users

All user endpoints require authentication (Bearer token).

### Get Current User
**GET** `/users/me`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "fullName": "John Doe",
  "email": "john@example.com",
  "phone": "9876543210",
  "profileImage": "https://res.cloudinary.com/.../profile.jpg",
  "bio": "Property enthusiast",
  "city": {
    "id": 1,
    "name": "Gwalior",
    "state": "Madhya Pradesh",
    "country": "India",
    "imageUrl": "https://...",
    "isTrending": true,
    "propertyCount": 25
  },
  "role": "OWNER",
  "isVerified": true,
  "createdAt": "2024-01-15T10:30:00"
}
```

---

### Update Profile
**PUT** `/users/profile`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "fullName": "John Doe Updated",
  "phone": "9876543210",
  "bio": "Passionate about real estate",
  "cityId": 2
}
```

**Response:** `200 OK` - User object

---

### Upload Profile Image
**POST** `/users/profile/image`

**Headers:**
```
Authorization: Bearer {accessToken}
Content-Type: multipart/form-data
```

**Form Data:**
```
file: [image file]
```

**Response:** `200 OK` - User object with updated profileImage

---

### Change Password
**PUT** `/users/change-password`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "currentPassword": "OldPassword123",
  "newPassword": "NewPassword123"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Password changed successfully"
}
```

---

## Properties

### Create Property
**POST** `/properties`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "title": "Luxury Banquet Hall",
  "description": "Spacious banquet hall perfect for weddings and events",
  "propertyType": "BANQUET",
  "cityId": 1,
  "address": "123 MG Road, Gwalior",
  "latitude": 26.2183,
  "longitude": 78.1828,
  "hourlyPrice": 5000.00,
  "dailyPrice": 80000.00,
  "bedrooms": 0,
  "bathrooms": 4,
  "parkingAvailable": true,
  "furnished": true,
  "guestCapacity": 500,
  "size": 10000.0,
  "amenityIds": [1, 2, 3, 5]
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "title": "Luxury Banquet Hall",
  "description": "Spacious banquet hall...",
  "propertyType": "BANQUET",
  "city": { ... },
  "address": "123 MG Road, Gwalior",
  "latitude": 26.2183,
  "longitude": 78.1828,
  "hourlyPrice": 5000.00,
  "dailyPrice": 80000.00,
  "bedrooms": 0,
  "bathrooms": 4,
  "parkingAvailable": true,
  "furnished": true,
  "guestCapacity": 500,
  "size": 10000.0,
  "status": "ACTIVE",
  "owner": { ... },
  "images": [],
  "amenities": [
    {
      "id": 1,
      "name": "WiFi",
      "icon": "wifi",
      "description": "High-speed internet"
    }
  ],
  "averageRating": 0.0,
  "totalReviews": 0,
  "isWishlisted": false,
  "createdAt": "2024-01-20T14:30:00",
  "updatedAt": "2024-01-20T14:30:00"
}
```

---

### Get Property by ID
**GET** `/properties/public/{propertyId}`

No authentication required.

**Response:** `200 OK` - Property object

---

### Get Properties by City
**GET** `/properties/public/city/{cityId}`

**Query Parameters:**
- `propertyType` (optional): HALL, HOUSE, FARMHOUSE, ROOFTOP, BANQUET, STUDIO, MEETING_ROOM, OPEN_AREA, CAFE_SPACE, PARTY_LAWN
- `minPrice` (optional): Minimum hourly price
- `maxPrice` (optional): Maximum hourly price
- `minCapacity` (optional): Minimum guest capacity
- `parkingAvailable` (optional): true/false
- `furnished` (optional): true/false
- `sortBy` (optional): newest, price_low, price_high, rating (default: newest)
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 12)

**Example:**
```
GET /properties/public/city/1?propertyType=BANQUET&minPrice=3000&maxPrice=10000&sortBy=price_low&page=0&size=12
```

**Response:** `200 OK`
```json
{
  "content": [
    { /* Property object */ }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 12
  },
  "totalElements": 25,
  "totalPages": 3,
  "last": false
}
```

---

### Upload Property Images
**POST** `/properties/{propertyId}/images`

**Headers:**
```
Authorization: Bearer {accessToken}
Content-Type: multipart/form-data
```

**Form Data:**
```
files: [image file 1, image file 2, ...]
```

**Response:** `200 OK` - Property object with updated images

---

### Get My Properties
**GET** `/properties/my-properties`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK` - Array of Property objects

---

## Bookings

### Create Booking
**POST** `/bookings`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "propertyId": 1,
  "bookingDate": "2024-02-15",
  "startTime": "18:00:00",
  "endTime": "23:00:00",
  "numberOfGuests": 200,
  "specialRequests": "Need extra chairs and tables"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "user": { ... },
  "property": { ... },
  "bookingDate": "2024-02-15",
  "startTime": "18:00:00",
  "endTime": "23:00:00",
  "totalPrice": 25000.00,
  "status": "PENDING",
  "paymentStatus": "PENDING",
  "numberOfGuests": 200,
  "specialRequests": "Need extra chairs and tables",
  "createdAt": "2024-01-20T15:00:00"
}
```

---

### Get My Bookings
**GET** `/bookings/my-bookings`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK` - Array of Booking objects

---

### Approve Booking (Owner/Admin)
**PUT** `/bookings/{bookingId}/approve`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK` - Updated Booking object with status "APPROVED"

---

### Reject Booking (Owner/Admin)
**PUT** `/bookings/{bookingId}/reject`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK` - Updated Booking object with status "REJECTED"

---

### Cancel Booking
**PUT** `/bookings/{bookingId}/cancel`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Query Parameters:**
- `reason` (optional): Cancellation reason

**Response:** `200 OK` - Updated Booking object with status "CANCELLED"

---

## Reviews

### Create Review
**POST** `/reviews`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "propertyId": 1,
  "bookingId": 5,
  "rating": 5,
  "comment": "Amazing venue! Perfect for our wedding.",
  "images": [
    "https://res.cloudinary.com/.../review1.jpg",
    "https://res.cloudinary.com/.../review2.jpg"
  ]
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "user": { ... },
  "propertyId": 1,
  "rating": 5,
  "comment": "Amazing venue! Perfect for our wedding.",
  "images": ["https://..."],
  "createdAt": "2024-01-21T10:00:00"
}
```

---

### Get Property Reviews
**GET** `/reviews/property/{propertyId}`

**Response:** `200 OK` - Array of Review objects

---

## Wishlist

### Add to Wishlist
**POST** `/wishlist/{propertyId}`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Added to wishlist"
}
```

---

### Remove from Wishlist
**DELETE** `/wishlist/{propertyId}`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Removed from wishlist"
}
```

---

### Get My Wishlist
**GET** `/wishlist`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK` - Array of Property objects

---

## Chat

### Send Message
**POST** `/chat/messages`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**
```json
{
  "receiverId": 2,
  "content": "Hi, I'm interested in booking your property",
  "propertyId": 1
}
```

**Response:** `201 Created` - Message object

---

### Get My Chat Rooms
**GET** `/chat/rooms`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK` - Array of ChatRoom objects

---

## Notifications

### Get My Notifications
**GET** `/notifications`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK` - Array of Notification objects

---

### Mark Notification as Read
**PUT** `/notifications/{notificationId}/read`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK` - Updated Notification object

---

### Get Unread Count
**GET** `/notifications/unread-count`

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`
```json
5
```

---

## Cities

### Get All Cities
**GET** `/cities`

**Response:** `200 OK` - Array of City objects

---

### Get Trending Cities
**GET** `/cities/trending`

**Response:** `200 OK` - Array of City objects marked as trending

---

## Amenities

### Get All Amenities
**GET** `/amenities`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "WiFi",
    "icon": "wifi",
    "description": "High-speed internet connection"
  },
  {
    "id": 2,
    "name": "AC",
    "icon": "ac",
    "description": "Air conditioning"
  }
]
```

---

## Error Responses

All endpoints may return error responses in the following format:

**400 Bad Request:**
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "email": "Invalid email format",
    "password": "Password must be at least 8 characters"
  },
  "timestamp": "2024-01-20T10:30:00"
}
```

**401 Unauthorized:**
```json
{
  "status": 401,
  "message": "Invalid email or password",
  "timestamp": "2024-01-20T10:30:00"
}
```

**403 Forbidden:**
```json
{
  "status": 403,
  "message": "Access denied",
  "timestamp": "2024-01-20T10:30:00"
}
```

**404 Not Found:**
```json
{
  "status": 404,
  "message": "Property not found with id: 999",
  "timestamp": "2024-01-20T10:30:00"
}
```

**500 Internal Server Error:**
```json
{
  "status": 500,
  "message": "An error occurred: ...",
  "timestamp": "2024-01-20T10:30:00"
}
```

---

## Property Types

Available property types:
- `HALL`
- `HOUSE`
- `FARMHOUSE`
- `ROOFTOP`
- `BANQUET`
- `STUDIO`
- `MEETING_ROOM`
- `OPEN_AREA`
- `CAFE_SPACE`
- `PARTY_LAWN`

## Booking Status

- `PENDING` - Awaiting owner approval
- `APPROVED` - Confirmed by owner
- `REJECTED` - Declined by owner
- `COMPLETED` - Event finished
- `CANCELLED` - Cancelled by user

## User Roles

- `USER` - Regular user (can book properties)
- `OWNER` - Property owner (can list and manage properties)
- `ADMIN` - Administrator (full access)
