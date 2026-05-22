# TrustSpace Backend - Complete Setup Guide

This guide will walk you through setting up the TrustSpace backend from scratch.

## 📋 System Requirements

- **Java**: JDK 17 or higher
- **Maven**: 3.6 or higher
- **PostgreSQL**: 12 or higher
- **RAM**: Minimum 4GB
- **Disk Space**: Minimum 2GB free space

## 🔧 Step-by-Step Setup

### Step 1: Install Prerequisites

#### Install Java 17
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-17-jdk

# macOS (using Homebrew)
brew install openjdk@17

# Verify installation
java -version
```

#### Install Maven
```bash
# Ubuntu/Debian
sudo apt install maven

# macOS
brew install maven

# Verify installation
mvn -version
```

#### Install PostgreSQL
```bash
# Ubuntu/Debian
sudo apt install postgresql postgresql-contrib

# macOS
brew install postgresql@15

# Start PostgreSQL service
# Ubuntu/Debian
sudo systemctl start postgresql
sudo systemctl enable postgresql

# macOS
brew services start postgresql@15
```

### Step 2: Database Configuration

#### Create Database
```bash
# Access PostgreSQL
sudo -u postgres psql

# Create database and user
CREATE DATABASE trustspace_db;
CREATE USER trustspace_user WITH PASSWORD 'your_secure_password';
GRANT ALL PRIVILEGES ON DATABASE trustspace_db TO trustspace_user;

# Exit PostgreSQL
\q
```

#### Verify Database Connection
```bash
psql -U trustspace_user -d trustspace_db -h localhost
```

### Step 3: Cloudinary Setup

1. Go to [Cloudinary](https://cloudinary.com/)
2. Sign up for a free account
3. From your dashboard, get:
   - Cloud Name
   - API Key
   - API Secret

### Step 4: Gmail Configuration for Emails

1. Enable 2-Factor Authentication on your Gmail account
2. Generate an App Password:
   - Go to Google Account → Security
   - 2-Step Verification → App Passwords
   - Generate new app password for "Mail"
   - Copy the 16-character password

### Step 5: Clone and Configure

```bash
# Clone the repository
git clone <your-repo-url>
cd trustspace-backend

# Copy environment template
cp .env.example .env

# Edit .env file
nano .env
```

#### Configure .env file:
```properties
# Database Configuration
DB_USERNAME=trustspace_user
DB_PASSWORD=your_secure_password

# JWT Configuration (Generate a secure key)
JWT_SECRET=your_256_bit_secret_key_here

# Cloudinary Configuration
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# Email Configuration
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_16_char_app_password

# Frontend URL
FRONTEND_URL=http://localhost:5173
```

### Step 6: Build the Project

```bash
# Clean and install dependencies
./mvnw clean install

# If you encounter permission issues
chmod +x mvnw
./mvnw clean install
```

### Step 7: Run the Application

```bash
# Run the application
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### Step 8: Verify Installation

#### Test Health Endpoint
```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{"status":"UP"}
```

#### Test API
```bash
# Get all cities
curl http://localhost:8080/api/cities

# Register a user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Test User",
    "email": "test@example.com",
    "password": "Password123",
    "phone": "9876543210"
  }'
```

## 🐳 Docker Setup (Alternative)

If you prefer Docker:

### Prerequisites
- Docker
- Docker Compose

### Setup Steps

```bash
# Clone repository
git clone <your-repo-url>
cd trustspace-backend

# Create .env file
cp .env.example .env
# Edit .env with your configurations

# Build and run
docker-compose up --build

# Run in detached mode
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

The application will be available at:
- Backend API: http://localhost:8080
- PostgreSQL: localhost:5432
- PgAdmin: http://localhost:5050 (admin@trustspace.com / admin)

## 📊 Database Initialization

The application will automatically create tables on first run using Hibernate DDL auto-update.

### Seed Initial Data (Optional)

Create a SQL file `seed-data.sql`:

```sql
-- Insert Cities
INSERT INTO cities (name, state, country, is_trending) VALUES
('Mumbai', 'Maharashtra', 'India', true),
('Delhi', 'Delhi', 'India', true),
('Bangalore', 'Karnataka', 'India', true),
('Gwalior', 'Madhya Pradesh', 'India', true),
('Pune', 'Maharashtra', 'India', false);

-- Insert Amenities
INSERT INTO amenities (name, icon, description) VALUES
('WiFi', 'wifi', 'High-speed internet connection'),
('AC', 'ac', 'Air conditioning'),
('Parking', 'parking', 'Vehicle parking space'),
('Sound System', 'sound', 'Professional audio system'),
('Kitchen', 'kitchen', 'Full kitchen facilities'),
('Decoration', 'decoration', 'Event decoration services'),
('Swimming Pool', 'pool', 'Swimming pool access'),
('CCTV', 'security', 'CCTV surveillance'),
('Stage', 'stage', 'Event stage/platform'),
('Furniture', 'furniture', 'Furnished space'),
('Power Backup', 'power', 'Generator backup');
```

Run the seed script:
```bash
psql -U trustspace_user -d trustspace_db -f seed-data.sql
```

## 🔒 Security Checklist

- [ ] Change default JWT_SECRET to a strong random key
- [ ] Use strong database passwords
- [ ] Never commit .env file to version control
- [ ] Enable HTTPS in production
- [ ] Configure CORS for production domains only
- [ ] Set up rate limiting
- [ ] Enable SQL injection protection (already configured)
- [ ] Use prepared statements (already configured via JPA)

## 🧪 Testing the Application

### Manual Testing

1. **Register a User**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "email": "john@example.com",
    "password": "Password123",
    "phone": "9876543210",
    "cityId": 1
  }'
```

2. **Login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "Password123"
  }'
```

Copy the accessToken from the response.

3. **Create a Property**
```bash
curl -X POST http://localhost:8080/api/properties \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -d '{
    "title": "Beautiful Banquet Hall",
    "description": "Spacious hall for events",
    "propertyType": "BANQUET",
    "cityId": 1,
    "address": "123 Main Street",
    "hourlyPrice": 5000,
    "guestCapacity": 500,
    "amenityIds": [1, 2, 3]
  }'
```

### Run Unit Tests
```bash
./mvnw test
```

## 🚀 Production Deployment

### Environment Variables for Production

```properties
# Use production database
SPRING_DATASOURCE_URL=jdbc:postgresql://production-db:5432/trustspace_db

# Strong JWT secret (256 bits)
JWT_SECRET=generate_a_very_strong_random_secret_key_here

# Production frontend URL
FRONTEND_URL=https://trustspace.com

# Disable SQL logging
SPRING_JPA_SHOW_SQL=false

# Update DDL strategy
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
```

### Build for Production
```bash
# Build JAR file
./mvnw clean package -DskipTests

# JAR will be created at
# target/trustspace-backend-1.0.0.jar

# Run JAR
java -jar target/trustspace-backend-1.0.0.jar
```

## 🐛 Troubleshooting

### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### Database Connection Failed
- Verify PostgreSQL is running: `sudo systemctl status postgresql`
- Check database credentials in .env
- Ensure database exists: `psql -U postgres -l`

### Maven Build Fails
```bash
# Clear Maven cache
./mvnw clean

# Delete .m2 repository and rebuild
rm -rf ~/.m2/repository
./mvnw clean install
```

### Cloudinary Upload Fails
- Verify Cloudinary credentials
- Check internet connectivity
- Ensure file size is within limits (10MB)

### Email Not Sending
- Verify Gmail app password is correct (16 characters, no spaces)
- Ensure 2FA is enabled on Gmail account
- Check spam folder for test emails

## 📞 Support

For issues and questions:
- Create an issue on GitHub
- Email: support@trustspace.com
- Documentation: See README.md and API_DOCUMENTATION.md

## ✅ Post-Setup Checklist

- [ ] Java 17 installed
- [ ] Maven installed
- [ ] PostgreSQL running
- [ ] Database created
- [ ] .env file configured
- [ ] Cloudinary account setup
- [ ] Gmail app password generated
- [ ] Application builds successfully
- [ ] Application runs without errors
- [ ] Can register and login users
- [ ] Can create properties
- [ ] API endpoints responding correctly

## 🎉 Success!

If all steps completed successfully, your TrustSpace backend is ready for development!

Next steps:
1. Review API_DOCUMENTATION.md for available endpoints
2. Test all endpoints using Postman or curl
3. Integrate with frontend application
4. Deploy to production server
