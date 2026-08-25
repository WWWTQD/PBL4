# PBL4 Quick Reference Guide

**Common commands and quick troubleshooting**

---

## ⚡ Quick Start (5 minutes)

```bash
# Terminal 1: AI Service
cd ai-service
python -m venv venv
venv\Scripts\activate
pip install -r requirements.txt
uvicorn main:app --reload --host 0.0.0.0 --port 8000

# Terminal 2: Backend
cd backend-core
mvn clean install
mvn spring-boot:run

# Terminal 3: Frontend
cd frontend-web
npm install
npm run dev
```

**Then open:** http://localhost:3000

---

## 🔧 Common Commands

### Database
```bash
# Create database
createdb pbl4_db

# Connect to database
psql -U postgres -d pbl4_db

# List databases
psql -U postgres -l

# Delete database
dropdb pbl4_db
```

### Python/AI Service
```bash
# Create virtual environment
python -m venv venv

# Activate (Windows)
venv\Scripts\activate

# Activate (macOS/Linux)
source venv/bin/activate

# Install dependencies
pip install -r requirements.txt

# Run with Uvicorn
uvicorn main:app --reload --port 8000

# Test endpoint
curl http://localhost:8000/health
```

### Java/Backend
```bash
# Build with Maven
mvn clean install
mvn clean build

# Run Spring Boot
mvn spring-boot:run

# Build JAR
mvn package

# Run JAR
java -jar target/backend-core-1.0.0.jar

# Test endpoint
curl http://localhost:8080/api/v1/documents
```

### Node.js/Frontend
```bash
# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build

# Start production build
npm start

# Run linter
npm run lint

# Type checking
npm run type-check
```

---

## 📡 API Testing

### Health Checks
```bash
# AI Service
curl http://localhost:8000/health

# Backend
curl http://localhost:8080/api/v1/documents/health/system

# Frontend
http://localhost:3000
```

### Document Operations
```bash
# Get all documents
curl http://localhost:8080/api/v1/documents

# Get recent documents
curl http://localhost:8080/api/v1/documents/recent

# Get specific document
curl http://localhost:8080/api/v1/documents/1

# Get by status
curl http://localhost:8080/api/v1/documents/status/COMPLETED

# Upload document
curl -X POST http://localhost:8080/api/v1/documents/upload \
  -F "file=@path/to/image.jpg" \
  -F "notes=Test document"

# Update document
curl -X PUT http://localhost:8080/api/v1/documents/1 \
  -H "Content-Type: application/json" \
  -d '{"notes":"Updated","status":"COMPLETED"}'

# Delete document
curl -X DELETE http://localhost:8080/api/v1/documents/1
```

### AI Service Processing
```bash
# Process single image
curl -X POST http://localhost:8000/api/v1/process-image \
  -F "file=@path/to/image.jpg"

# Process batch
curl -X POST http://localhost:8000/api/v1/process-batch \
  -F "files=@image1.jpg" \
  -F "files=@image2.jpg"
```

---

## 🔗 Service URLs

| Service | URL | Purpose |
|---------|-----|---------|
| Frontend | http://localhost:3000 | Main dashboard |
| Backend API | http://localhost:8080 | REST API |
| Backend Docs | http://localhost:8080/swagger-ui.html | API documentation |
| AI Service | http://localhost:8000 | Image processing |
| AI Docs | http://localhost:8000/docs | Swagger UI |
| AI Redoc | http://localhost:8000/redoc | ReDoc UI |
| Database | localhost:5432 | PostgreSQL |

---

## 🆘 Troubleshooting

### "Port already in use"
```bash
# Find process using port (Windows)
netstat -ano | findstr :8080

# Kill process
taskkill /PID <PID_NUMBER> /F

# Or use different ports in configuration
```

### "Connection refused - cannot reach database"
```bash
# Check PostgreSQL is running
psql -U postgres -l

# If not running, start PostgreSQL service
# Windows: Services > PostgreSQL > Start
# macOS: brew services start postgresql
# Linux: sudo systemctl start postgresql
```

### "AI Service not responding"
```bash
# Check if service is running
curl http://localhost:8000/health

# Check Python environment
python --version
pip list

# Reinstall dependencies
pip install --upgrade -r requirements.txt
```

### "Frontend cannot connect to backend"
```bash
# Check CORS configuration
# File: backend-core/src/main/java/com/pbl4/config/CorsConfig.java

# Verify environment variables
# File: frontend-web/.env.local
NEXT_PUBLIC_API_URL=http://localhost:8080

# Clear browser cache
# Ctrl+Shift+Del or Cmd+Shift+Delete
```

### "npm install fails"
```bash
# Clear npm cache
npm cache clean --force

# Update npm
npm install -g npm@latest

# Delete node_modules and package-lock.json
rm -rf node_modules package-lock.json

# Reinstall
npm install
```

### "Build errors in Java"
```bash
# Clean previous builds
mvn clean

# Update Maven
mvn -version

# Rebuild
mvn install
```

---

## 📝 Configuration Quick Reference

### AI Service Environment
**File:** `ai-service/.env`
```env
ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080
PYTHON_ENV=development
```

### Backend Environment
**File:** `backend-core/src/main/resources/application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pbl4_db
spring.datasource.username=postgres
spring.datasource.password=postgres
ai.service.url=http://localhost:8000
```

### Frontend Environment
**File:** `frontend-web/.env.local`
```env
NEXT_PUBLIC_API_URL=http://localhost:8080
NEXT_PUBLIC_AI_SERVICE_URL=http://localhost:8000
```

---

## 🐳 Docker Shortcuts

```bash
# Build all images
docker-compose build

# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f backend

# Restart a service
docker-compose restart backend

# Remove volumes (database cleaning)
docker-compose down -v

# Rebuild without cache
docker-compose build --no-cache
```

---

## 📊 File Locations Reference

| File | Location | Purpose |
|------|----------|---------|
| Main config | `README.md` | Architecture overview |
| Setup guide | `SETUP_GUIDE.md` | Step-by-step instructions |
| Completion info | `SCAFFOLDING_COMPLETE.md` | What was created |
| Backend config | `backend-core/src/main/resources/application.properties` | Database & services |
| Frontend config | `frontend-web/.env.local` | API URLs |
| AI config | `ai-service/.env` | CORS & environment |
| Backend API docs | `backend-core/README.md` | Controller documentation |
| AI docs | `ai-service/README.md` | FastAPI documentation |
| Frontend docs | `frontend-web/README.md` | React documentation |

---

## 🎯 Development Workflow

### Adding a Backend Feature
1. Create entity in `backend-core/src/main/java/com/pbl4/model/`
2. Create repository in `repository/`
3. Add service methods in `service/`
4. Create REST endpoints in `controller/`
5. Test with curl or Postman

### Adding a Frontend Component
1. Create component in `frontend-web/src/components/`
2. Use API client from `services/api.ts`
3. Import in page or parent component
4. Test in browser at http://localhost:3000

### Adding AI Processing
1. Add logic to `ai-service/main.py`
2. Create new endpoint if needed
3. Update backend `AiIntegrationService.java`
4. Call from document controller

---

## 📚 Important Files

**Backend Core Files:**
- `PBL4Application.java` - Entry point
- `DocumentController.java` - API endpoints
- `DocumentService.java` - Business logic
- `AiIntegrationService.java` - AI communication
- `Document.java` - Database model
- `application.properties` - Configuration

**AI Service Files:**
- `main.py` - FastAPI application
- `ImageProcessor` - Image processing logic
- `requirements.txt` - Dependencies

**Frontend Files:**
- `page.tsx` - Main dashboard
- `api.ts` - Backend communication
- `Sidebar.tsx` - Navigation
- `Dashboard.tsx` - Data display
- `UploadButton.tsx` - File upload

---

## 🚀 Performance Tips

### Backend
- Enable query logging in `application.properties`:
  ```properties
  spring.jpa.show-sql=true
  spring.jpa.properties.hibernate.format_sql=true
  ```
- Use connection pooling (automatic with Spring Boot)
- Optimize database queries in repositories

### AI Service
- Use async processing for large files
- Implement caching for repeated operations
- Monitor memory usage with large images

### Frontend
- Use Next.js built-in image optimization
- Implement pagination for document lists
- Cache API responses with SWR or React Query

---

## 🔐 Security Checklist

- [ ] Change default PostgreSQL password
- [ ] Configure CORS only for known origins
- [ ] Validate file uploads (size, type)
- [ ] Implement authentication in production
- [ ] Use HTTPS in production
- [ ] Don't commit `.env` files
- [ ] Sanitize user input
- [ ] Use environment variables for secrets

---

## 📞 Getting Help

**Visit these files for more information:**
- `README.md` - Architecture and overview
- `SETUP_GUIDE.md` - Detailed setup steps
- `backend-core/README.md` - Backend documentation
- `ai-service/README.md` - AI service documentation
- `frontend-web/README.md` - Frontend documentation
- `SCAFFOLDING_COMPLETE.md` - What was created

---

**Last Updated:** 2026-08-25  
**Status:** ✅ Ready to use

