# PBL4 Complete Setup Guide

**Step-by-step instructions to run the entire PBL4 system locally**

---

## 📋 Prerequisites Checklist

Before starting, ensure you have:

- ✅ **Java 17+** - Download from [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or use [OpenJDK](https://openjdk.org/)
- ✅ **Python 3.10+** - Download from [python.org](https://www.python.org/downloads/)
- ✅ **Node.js 18+ & npm** - Download from [nodejs.org](https://nodejs.org/)
- ✅ **PostgreSQL 14+** - Download from [postgresql.org](https://www.postgresql.org/download/)
- ✅ **Git** - For version control
- ✅ **IDE** - IntelliJ IDEA, VS Code, or your preferred editor
- ✅ **Postman** (optional) - For API testing

---

## 🚀 Step-by-Step Setup Instructions

### Step 1: Database Setup (PostgreSQL)

#### Windows
```powershell
# Open PostgreSQL command line
psql -U postgres

# Create the database
CREATE DATABASE pbl4_db;

# Verify creation
\l

# Exit
\q
```

#### macOS
```bash
createdb pbl4_db
```

#### Linux
```bash
sudo -u postgres createdb pbl4_db
```

---

### Step 2: Start AI Service (Python/FastAPI)

Open **Terminal/PowerShell** and run:

```powershell
# Navigate to ai-service
cd frontend-web\..\ai-service
# or
cd ai-service

# Create virtual environment
python -m venv venv

# Activate virtual environment
# Windows:
venv\Scripts\activate
# macOS/Linux:
# source venv/bin/activate

# Install dependencies
pip install -r requirements.txt

# Run the service
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

**Expected Output:**
```
✓ Uvicorn running on http://0.0.0.0:8000
✓ Application startup complete
```

**Verify:** Open http://localhost:8000/docs (Swagger UI)

---

### Step 3: Start Backend (Java/Spring Boot)

Open **new Terminal/PowerShell** window and run:

```powershell
# Navigate to backend-core
cd backend-core

# Build the project
mvn clean install

# Run Spring Boot
mvn spring-boot:run
# or
java -jar target/backend-core-1.0.0.jar
```

**Expected Output:**
```
✓ Tomcat started on port(s): 8080
✓ PBL4 Backend Application started successfully
```

**Verify:** Open http://localhost:8080/api/v1/documents/health/system

---

### Step 4: Start Frontend (Next.js)

Open **new Terminal/PowerShell** window and run:

```powershell
# Navigate to frontend-web
cd frontend-web

# Install dependencies
npm install

# Run development server
npm run dev
```

**Expected Output:**
```
✓ ready - started server on http://localhost:3000
```

**Verify:** Open http://localhost:3000 in browser

---

## ✅ Verification Checklist

After starting all services, verify everything is working:

### 1. AI Service Health
```bash
curl http://localhost:8000/health
```

Expected response:
```json
{
  "status": "healthy",
  "service": "PBL4 AI Service",
  "version": "1.0.0"
}
```

### 2. Backend Health
```bash
curl http://localhost:8080/api/v1/documents/health/system
```

Expected response:
```json
{
  "success": true,
  "backend": "healthy",
  "aiService": "healthy"
}
```

### 3. Frontend
- Open http://localhost:3000
- Should see dashboard with "Recent Scans" table
- "Upload Scan" button visible
- System status indicators showing green

---

## 🧪 Testing the Complete System

### Test 1: Upload & Process Document

1. Go to http://localhost:3000
2. Click "📤 Upload Scan" button
3. Select an image file (JPG, PNG, etc.)
4. Wait for processing to complete
5. Document should appear in "Recent Scans" table with status "COMPLETED"

### Test 2: API Testing with curl

```bash
# Get all documents
curl -X GET http://localhost:8080/api/v1/documents

# Get recent documents
curl -X GET http://localhost:8080/api/v1/documents/recent

# Upload document (replace path/to/image.jpg with actual image)
curl -X POST http://localhost:8080/api/v1/documents/upload \
  -F "file=@path/to/image.jpg" \
  -F "notes=Test document"

# Get document by ID
curl -X GET http://localhost:8080/api/v1/documents/1
```

### Test 3: Postman Collection

Create a new Postman collection with these endpoints:

```
GET  http://localhost:8080/api/v1/documents
GET  http://localhost:8080/api/v1/documents/recent
GET  http://localhost:8080/api/v1/documents/{id}
POST http://localhost:8080/api/v1/documents/upload
PUT  http://localhost:8080/api/v1/documents/{id}
DELETE http://localhost:8080/api/v1/documents/{id}
GET  http://localhost:8000/api/v1/process-image
```

---

## 📁 Directory Structure After Setup

```
PBL4/
├── README.md                          # Architecture overview
├── .env.example                       # Environment template
├── SETUP_GUIDE.md                     # This file
│
├── ai-service/                        # Python FastAPI
│   ├── main.py
│   ├── requirements.txt
│   ├── venv/                          # Virtual environment (after setup)
│   └── README.md
│
├── backend-core/                      # Java Spring Boot
│   ├── pom.xml
│   ├── src/
│   │   ├── main/java/com/pbl4/
│   │   └── main/resources/
│   ├── target/                        # Build artifacts (after mvn build)
│   └── README.md
│
└── frontend-web/                      # Next.js React
    ├── package.json
    ├── src/
    │   ├── app/
    │   ├── components/
    │   └── services/
    ├── node_modules/                  # Dependencies (after npm install)
    ├── .next/                         # Build output (after npm build)
    └── README.md
```

---

## 🔧 Environment Configuration

### Backend Configuration

Edit `backend-core/src/main/resources/application.properties`:

```properties
# Database connection
spring.datasource.url=jdbc:postgresql://localhost:5432/pbl4_db
spring.datasource.username=postgres
spring.datasource.password=postgres

# AI Service URL
ai.service.url=http://localhost:8000
```

### Frontend Configuration

Create `frontend-web/.env.local`:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
NEXT_PUBLIC_AI_SERVICE_URL=http://localhost:8000
```

### AI Service Configuration

Create `ai-service/.env`:

```env
ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080
PYTHON_ENV=development
```

---

## 🐛 Troubleshooting

### Issue: Database connection failed

**Solution:**
```bash
# Check PostgreSQL is running
psql -U postgres

# Verify database was created
\l

# If not created, run:
CREATE DATABASE pbl4_db;
```

### Issue: Port already in use

**Solution:** Change port in `application.properties` or kill process:
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# macOS/Linux
lsof -i :8080
kill -9 <PID>
```

### Issue: AI Service not responding

**Solution:** Ensure FastAPI is running:
```bash
# Check if process is running
curl http://localhost:8000/health

# Restart if needed
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

### Issue: npm install fails

**Solution:**
```bash
# Clear cache and retry
npm cache clean --force
npm install

# Update npm
npm install -g npm@latest
```

---

## 📊 System Architecture Verification

All three services should be accessible:

| Service | URL | Status |
|---------|-----|--------|
| Frontend | http://localhost:3000 | ✅ Green |
| Backend | http://localhost:8080 | ✅ Green |
| AI Service | http://localhost:8000 | ✅ Green |
| Database | localhost:5432 | ✅ Connected |

---

## 🔄 Communication Flow

```
User (Browser)
    ↓
Frontend (http://localhost:3000)
    ↓
Backend API (http://localhost:8080)
    ↓
AI Service (http://localhost:8000)
    ↓
PostgreSQL Database
```

1. User uploads image via Next.js frontend
2. Frontend sends to Spring Boot backend
3. Backend saves metadata and forwards image to AI service
4. AI service processes image (OpenCV, OCR)
5. Results returned to backend
6. Backend stores processed data in PostgreSQL
7. Frontend displays results

---

## 📱 Supported File Formats

**Image Upload:**
- JPEG (.jpg, .jpeg)
- PNG (.png)
- TIFF (.tiff, .tif)
- BMP (.bmp)

**File Size Limit:** 10MB

---

## 🚀 Production Deployment

### Docker Compose (Optional)

Create `docker-compose.yml` in root:

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15
    ports:
      - "5432:5432"
    environment:
      POSTGRES_DB: pbl4_db
      POSTGRES_PASSWORD: postgres

  ai-service:
    build: ./ai-service
    ports:
      - "8000:8000"
    environment:
      ALLOWED_ORIGINS: http://localhost:3000,http://localhost:8080

  backend:
    build: ./backend-core
    ports:
      - "8080:8080"
    depends_on:
      - postgres
      - ai-service
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/pbl4_db

  frontend:
    build: ./frontend-web
    ports:
      - "3000:3000"
```

**Run:** `docker-compose up -d`

---

## 📞 Support & Resources

- **Main README:** See [README.md](./README.md)
- **Backend Docs:** See [backend-core/README.md](./backend-core/README.md)
- **AI Service Docs:** See [ai-service/README.md](./ai-service/README.md)
- **Frontend Docs:** See [frontend-web/README.md](./frontend-web/README.md)

---

## ✨ Next Steps

After successful setup:

1. ✅ Test file uploads
2. ✅ Review extracted text output
3. ✅ Explore API endpoints with Postman
4. ✅ Customize styling in Tailwind
5. ✅ Add additional features as needed

---

**Happy Scanning! 🎉**

