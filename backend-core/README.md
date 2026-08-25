# PBL4 Backend Core - Spring Boot

**Spring Boot Backend Service for Document Management & Processing**

The backend handles user management, document metadata storage, CRUD operations, and orchestration of AI service calls for document processing.

## 🚀 Quick Start

### Prerequisites
- **Java 17+**
- **Maven 3.6+** or **Gradle 7+**
- **PostgreSQL 14+**

### Installation & Setup

1. **Clone and navigate to backend:**
   ```bash
   cd backend-core
   ```

2. **Create PostgreSQL Database:**
   ```bash
   createdb pbl4_db
   # or
   psql -U postgres
   CREATE DATABASE pbl4_db;
   ```

3. **Configure Application:**
   - Edit `src/main/resources/application.properties`
   - Update `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`

4. **Build the Project:**
   ```bash
   # Using Maven
   mvn clean install
   
   # Using Gradle
   gradle build
   ```

5. **Run the Application:**
   ```bash
   # Using Maven
   mvn spring-boot:run
   
   # Using Gradle
   gradle bootRun
   
   # Or run JAR directly
   java -jar target/backend-core-1.0.0.jar
   ```

The API will be available at: **http://localhost:8080**

---

## 📁 Project Structure

```
backend-core/
├── src/
│   ├── main/
│   │   ├── java/com/pbl4/
│   │   │   ├── controller/
│   │   │   │   └── DocumentController.java     # REST API endpoints
│   │   │   ├── service/
│   │   │   │   ├── DocumentService.java        # Business logic
│   │   │   │   └── AiIntegrationService.java   # AI service integration
│   │   │   ├── model/
│   │   │   │   ├── Document.java               # Document JPA entity
│   │   │   │   └── DocumentStatus.java         # Status enumeration
│   │   │   ├── repository/
│   │   │   │   └── DocumentRepository.java     # Data access layer
│   │   │   ├── config/
│   │   │   │   ├── CorsConfig.java             # CORS configuration
│   │   │   │   └── AppConfig.java              # Application configuration
│   │   │   └── PBL4Application.java            # Main application class
│   │   └── resources/
│   │       └── application.properties          # Application configuration
│   └── test/
│       └── java/com/pbl4/                      # Test classes
├── pom.xml                                      # Maven configuration
└── README.md                                    # This file
```

---

## 🔌 API Endpoints

### Document Management

#### Get All Documents
- **GET** `/api/v1/documents`
- Returns: List of all documents
- Response: `{ "success": true, "data": [...], "count": N }`

#### Get Recent Documents
- **GET** `/api/v1/documents/recent`
- Returns: Last 10 scanned documents

#### Get Document by ID
- **GET** `/api/v1/documents/{id}`
- Parameter: `id` (Long) - Document ID
- Returns: Single document details

#### Get Documents by Status
- **GET** `/api/v1/documents/status/{status}`
- Parameter: `status` (String) - PENDING, PROCESSING, COMPLETED, FAILED
- Returns: List of documents with specified status

#### Upload & Process Document
- **POST** `/api/v1/documents/upload`
- Request Body: Form data with `file` (image) and optional `notes`
- Returns: Created document with processing results
- **Integration Point:** Calls `http://localhost:8000/api/v1/process-image`

#### Update Document
- **PUT** `/api/v1/documents/{id}`
- Request Body: JSON with fields to update (notes, status)
- Returns: Updated document

#### Delete Document
- **DELETE** `/api/v1/documents/{id}`
- Parameter: `id` (Long) - Document ID
- Returns: Success message

### Health Check

#### System Health
- **GET** `/api/v1/documents/health/system`
- Returns: Backend and AI service health status

---

## 🗄️ Database Schema

### Documents Table
```sql
CREATE TABLE documents (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    file_name VARCHAR(255) NOT NULL,
    scan_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    extracted_text TEXT,
    file_url VARCHAR(255),
    processed_image LONGTEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    notes TEXT
);
```

### Document Status Values
- `PENDING` - Awaiting processing
- `PROCESSING` - Currently being processed by AI service
- `COMPLETED` - Successfully processed
- `FAILED` - Processing failed
- `ARCHIVED` - Archived by user

---

## 🔐 CORS Configuration

The backend is configured to accept requests from:
- `http://localhost:3000` (Next.js Frontend)
- `http://localhost:8000` (AI Service)

### Configuration Location
`src/main/java/com/pbl4/config/CorsConfig.java`

To modify CORS settings:
```java
registry.addMapping("/**")
        .allowedOrigins("http://your-domain:3000")
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        .allowedHeaders("*")
        .maxAge(3600);
```

---

## 🔗 Integration Points

### AI Service Integration
The backend communicates with the Python FastAPI AI service through:

**Service:** `AiIntegrationService` (src/main/java/com/pbl4/service/AiIntegrationService.java)

**Endpoint:** `POST http://localhost:8000/api/v1/process-image`

**Usage:**
```java
// In DocumentService or DocumentController
Map<String, Object> aiResponse = aiIntegrationService.processImage(multipartFile);
String processedImage = aiResponse.get("processedImage");    // base64
String extractedText = aiResponse.get("extractedText");      // OCR text
```

### Database Integration
Uses **Spring Data JPA** with **Hibernate** for ORM:
- Repository: `DocumentRepository.java`
- Entity: `Document.java`
- Automatic CRUD operations and custom queries

---

## 📝 Configuration Files

### application.properties
Located at: `src/main/resources/application.properties`

Key configurations:
```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/pbl4_db
spring.datasource.username=postgres
spring.datasource.password=postgres

# AI Service
ai.service.url=http://localhost:8000

# File Upload
spring.servlet.multipart.max-file-size=10MB
```

---

## 🧪 Testing

### Manual API Testing with curl

```bash
# Get all documents
curl -X GET http://localhost:8080/api/v1/documents

# Upload and process document
curl -X POST http://localhost:8080/api/v1/documents/upload \
  -F "file=@/path/to/document.jpg" \
  -F "notes=Sample document"

# Get document by ID
curl -X GET http://localhost:8080/api/v1/documents/1

# Update document
curl -X PUT http://localhost:8080/api/v1/documents/1 \
  -H "Content-Type: application/json" \
  -d '{"notes":"Updated notes","status":"COMPLETED"}'

# Delete document
curl -X DELETE http://localhost:8080/api/v1/documents/1

# Check system health
curl -X GET http://localhost:8080/api/v1/documents/health/system
```

### Using a REST Client
- **Postman:** Import the provided collection
- **IntelliJ IDEA:** Use built-in REST client
- **VS Code:** Use REST Client extension

---

## 📊 API Response Examples

### Success Response (200)
```json
{
  "success": true,
  "message": "Document uploaded and processed successfully",
  "data": {
    "id": 1,
    "fileName": "document.jpg",
    "scanDate": "2026-08-25T10:30:00",
    "status": "COMPLETED",
    "extractedText": "Document content...",
    "processedImage": "iVBORw0KGgo...",
    "createdAt": "2026-08-25T10:30:00",
    "updatedAt": "2026-08-25T10:30:45"
  }
}
```

### Error Response (400/500)
```json
{
  "success": false,
  "error": "Invalid image format"
}
```

---

## 🚀 Production Deployment

### Prerequisites
- Java 17+ installed
- PostgreSQL server running
- Environment variables configured

### Deployment Steps

1. **Build production JAR:**
   ```bash
   mvn clean package -DskipTests
   ```

2. **Set environment variables:**
   ```bash
   export SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/pbl4_db
   export SPRING_DATASOURCE_USERNAME=prod_user
   export SPRING_DATASOURCE_PASSWORD=prod_password
   export AI_SERVICE_URL=http://ai-service:8000
   ```

3. **Run JAR:**
   ```bash
   java -jar target/backend-core-1.0.0.jar
   ```

### Docker Support

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/backend-core-1.0.0.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

Build: `docker build -t pbl4-backend .`

---

## 🛠️ Technologies & Dependencies

| Technology | Version | Purpose |
|-----------|---------|---------|
| Spring Boot | 3.2.0 | Web framework |
| Spring Data JPA | 3.2.0 | ORM/Database |
| PostgreSQL Driver | 42.7.1 | Database |
| Lombok | Latest | Reduce boilerplate |
| Jackson | 2.15.2 | JSON processing |
| JUnit 5 | Latest | Testing |

---

## 📄 Design Patterns

- **MVC Pattern:** Separation of Controller, Service, Repository layers
- **Dependency Injection:** Spring DI for loose coupling
- **Repository Pattern:** Data access abstraction
- **Service Layer Pattern:** Business logic encapsulation
- **Builder Pattern:** Document entity creation

---

## 💡 Best Practices

- ✅ Clean code with clear naming
- ✅ Comprehensive error handling
- ✅ Request/response logging
- ✅ CORS security configuration
- ✅ Database transaction management
- ✅ RESTful API design
- ✅ Integration test support

---

## 🤝 Contributing

1. Create feature branch: `git checkout -b feature/document-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push to branch: `git push origin feature/document-feature`
4. Create Pull Request

---

## 📄 License

Part of the PBL4 initiative.

---

## 💬 Support

For issues or questions:
1. Check the main PBL4 README.md
2. Review API endpoint documentation
3. Check application.properties configuration

