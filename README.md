# PBL4: Hệ thống Scan và Ứng dụng AI trong Xử lý, Phục hồi & Đa dạng hóa Định dạng Tài liệu Số

**Document Scanning & AI-Powered Processing System**

## 📋 Architecture Overview

This is a **microservices-inspired monorepo** consisting of three main services:

```
+------------------+          +--------------+          +------------------+
|  Next.js Frontend|<-------->| Spring Boot  |<-------->|  Python FastAPI  |
| (Port 3000)      |  REST    | Backend      |  REST    | AI Service       |
|                  |          | (Port 8080)  |          | (Port 8000)      |
+------------------+          +--------------+          +------------------+
                                      |
                                      v
                              +------------------+
                              | PostgreSQL DB    |
                              | (Port 5432)      |
                              +------------------+
```

### Data Flow
1. **IoT Devices (ESP32)** → Send raw scan data to backend
2. **Spring Boot Backend** → Manages users, documents, metadata; routes to AI service
3. **Python FastAPI** → Heavy image processing (deskew, denoise, OCR)
4. **PostgreSQL** → Persistent storage of document metadata
5. **Next.js Frontend** → Dashboard for users to view, upload, and download processed documents

---

## 🛠️ Tech Stack

| Service | Technology | Port |
|---------|-----------|------|
| **Backend Core** | Java 17+, Spring Boot 3, Spring Data JPA | 8080 |
| **AI Service** | Python 3.10+, FastAPI, OpenCV, Pytesseract | 8000 |
| **Database** | PostgreSQL | 5432 |
| **Frontend** | Next.js 14 (App Router), React, TypeScript, Tailwind CSS | 3000 |

---

## 🚀 Quick Start

### Prerequisites
- **Java 17+** (for Spring Boot)
- **Python 3.10+** (for FastAPI)
- **Node.js 18+** (for Next.js)
- **PostgreSQL 14+**
- **Docker** (optional, for containerization)

### Running All Services Locally

#### 1. Database Setup
```bash
# Create PostgreSQL database
createdb pbl4_db

# Or using psql
psql -U postgres
CREATE DATABASE pbl4_db;
```

#### 2. Start AI Service (FastAPI)
```bash
cd ai-service
python -m venv venv

# Windows
venv\Scripts\activate
# macOS/Linux
source venv/bin/activate

pip install -r requirements.txt
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

#### 3. Start Backend (Spring Boot)
```bash
cd backend-core
# If using Maven
mvn clean install
mvn spring-boot:run

# If using Gradle
gradle build
gradle bootRun
```

#### 4. Start Frontend (Next.js)
```bash
cd frontend-web
npm install
npm run dev
```

Then open your browser and navigate to: **http://localhost:3000**

---

## 📁 Project Structure

```
pbl4-scan-system/
├── README.md                          # This file
│
├── backend-core/                      # Spring Boot microservice
│   ├── src/
│   │   ├── main/java/com/pbl4/
│   │   │   ├── controller/           # REST controllers
│   │   │   ├── service/              # Business logic
│   │   │   ├── model/                # JPA entities
│   │   │   ├── repository/           # Data access
│   │   │   ├── config/               # Spring configuration
│   │   │   └── PBL4Application.java  # Main entry point
│   │   └── resources/
│   │       └── application.properties # Configuration
│   ├── pom.xml                        # Maven dependencies
│   └── README.md
│
├── ai-service/                        # Python FastAPI microservice
│   ├── main.py                        # FastAPI entry point
│   ├── requirements.txt               # Python dependencies
│   ├── routes/
│   │   └── image_processing.py       # Image processing endpoints
│   ├── services/
│   │   └── ocr_service.py            # OCR logic
│   └── README.md
│
├── frontend-web/                      # Next.js web dashboard
│   ├── app/
│   │   ├── page.tsx                   # Home page
│   │   ├── layout.tsx                 # Root layout
│   │   └── api/                       # API routes (if needed)
│   ├── components/                    # Reusable React components
│   │   ├── Sidebar.tsx
│   │   ├── Dashboard.tsx
│   │   └── UploadButton.tsx
│   ├── services/
│   │   └── api.ts                     # API client
│   ├── tailwind.config.ts             # Tailwind CSS config
│   ├── package.json
│   └── README.md
```

---

## 🔌 API Endpoints

### Backend (Spring Boot)
- `GET /api/v1/documents` - List all documents
- `GET /api/v1/documents/{id}` - Get document by ID
- `POST /api/v1/documents/upload` - Upload and process a scan
- `PUT /api/v1/documents/{id}` - Update document
- `DELETE /api/v1/documents/{id}` - Delete document

### AI Service (FastAPI)
- `POST /api/v1/process-image` - Process image (deskew, denoise, OCR)

---

## 🔐 CORS Configuration

Both Spring Boot and FastAPI are configured to accept requests from `http://localhost:3000` (Next.js frontend).

**Spring Boot:** See `backend-core/src/main/java/com/pbl4/config/CorsConfig.java`

**FastAPI:** See `ai-service/main.py` CORSMiddleware setup

---

## 📝 Environment Variables

Create `.env` files in each service:

### Backend (backend-core/.env)
```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/pbl4_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password
AI_SERVICE_URL=http://localhost:8000
```

### AI Service (ai-service/.env)
```
ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080
```

### Frontend (frontend-web/.env.local)
```
NEXT_PUBLIC_API_URL=http://localhost:8080
NEXT_PUBLIC_AI_SERVICE_URL=http://localhost:8000
```

---

## 🧪 Testing

Each service includes test files. Run tests:

```bash
# Backend
cd backend-core && mvn test

# AI Service
cd ai-service && pytest

# Frontend
cd frontend-web && npm test
```

---

## 📚 Documentation

- [Backend Service Documentation](./backend-core/README.md)
- [AI Service Documentation](./ai-service/README.md)
- [Frontend Documentation](./frontend-web/README.md)

---

## 🤝 Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Open a Pull Request

---

## 📄 License

This project is part of the PBL4 initiative.

---

## 💬 Support

For questions or issues, please refer to the individual service READMEs or contact the development team.

