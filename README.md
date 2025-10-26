# PlacePrep - Interview Experience Platform

A comprehensive Spring Boot application for sharing and managing interview experiences. Built with ❤️ using Java 17, Spring Boot 3.x, and PostgreSQL.

## ✨ Features

### 👤 User Management
- Secure authentication with JWT
- Profile management with personal details
- Social links (GitHub, LinkedIn)
- Password management with strong validation

### 📝 Interview Experiences
- Share detailed interview experiences
- Add multiple rounds per interview
- Mark experiences as anonymous
- Rich text descriptions
- Company and role-based organization

### 🔍 Smart Search & Filtering
- Search by company, role, or keywords
- Filter by job type and interview status
- Sort by date, company, or relevance

### 📊 Analytics Dashboard
- Personal interview statistics
- Success rate analysis
- Common interview topics
- Company-wise experience breakdown

## 🛠 Tech Stack

### Backend
- **Java 17** - Core programming language
- **Spring Boot 3.x** - Application framework
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Data access
- **Hibernate** - ORM
- **PostgreSQL** - Primary database
- **Maven** - Dependency management
- **Lombok** - Boilerplate reduction

### API Documentation
- **Swagger/OpenAPI** - Interactive API documentation

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6.3+
- PostgreSQL 13+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/VenkateshW22/PlacePrep.git
   cd placeprep
   ```

2. **Configure Database**
   - Create a new PostgreSQL database
   - Update `application.properties` with your database credentials

3. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Access the Application**
   - API Docs: `http://localhost:8080/swagger-ui.html`
   - API Base URL: `http://localhost:8080/api`

## 📚 API Endpoints

### Authentication
- `POST /api/auth/signup` - Register a new user
- `POST /api/auth/login` - Authenticate user

### User Profile
- `GET /api/users/me` - Get current user profile
- `PUT /api/users/update/me` - Update profile
- `GET /api/users/me/experiences/summary` - Get user's experience summary

### Experiences
- `GET /api/experiences` - Get all experiences (filterable)
- `POST /api/experiences` - Create new experience
- `GET /api/experiences/{id}` - Get experience by ID
- `PUT /api/experiences/{id}` - Update experience
- `DELETE /api/experiences/{id}` - Delete experience
- `GET /api/experiences/summary` - Get experiences summary

## 🔒 Security
- JWT-based authentication
- Role-based access control
- Password encryption with BCrypt
- Input validation
- CORS configuration

## 📦 Project Structure

```
src/main/java/com/vk/placeprep/
├── config/           # Configuration classes
├── controller/       # REST controllers
├── dto/              # Data Transfer Objects
├── exception/        # Custom exceptions
├── model/            # JPA entities
├── repository/       # Data access layer
├── security/         # Security configuration
└── service/          # Business logic
```

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments
- Built with Spring Boot
- Inspired by platforms like LeetCode and InterviewBit
- Thanks to all contributors who have participated in this project

---

<div align="center">
  Made with ❤️ by Venkatesh K
</div>
