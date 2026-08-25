# 🔐 Hướng Dẫn Quản Lý File Nhạy Cảm & Mật Khẩu

**Danh sách đầy đủ các file chứa mật khẩu/credentials và cách xử lý**

---

## ⚠️ Các File KHÔNG Nên Push Lên GitHub

### 1️⃣ **Environment Files (.env)**

| File | Đường Dẫn | Chứa Gì | Xử Lý |
|------|---------|--------|-------|
| `.env` | `C:\Users\imdrx\IdeaProjects\PBL4\.env` | **Mật khẩu DB, API keys** | ❌ KHÔNG push |
| `.env.local` | `C:\Users\imdrx\IdeaProjects\PBL4\.env.local` | Local development secrets | ❌ KHÔNG push |
| `.env.production` | `C:\Users\imdrx\IdeaProjects\PBL4\.env.production` | Production secrets | ❌ KHÔNG push |

**Nội dung ví dụ `.env` (KHÔNG PUSH):**
```env
# Database Credentials
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/pbl4_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_secret_password_here  ⚠️ SENSITIVE

# AI Service
AI_SERVICE_URL=http://localhost:8000

# JWT Secret (nếu có authentication)
JWT_SECRET=your_secret_key_here  ⚠️ SENSITIVE
```

---

### 2️⃣ **Backend Configuration Files**

| File | Đường Dẫn | Chứa Gì | Xử Lý |
|------|---------|--------|-------|
| `application.properties` | `backend-core/src/main/resources/application.properties` | Database password | ✅ Đã loại trừ trong .gitignore |
| `application-prod.properties` | `backend-core/src/main/resources/application-prod.properties` | Production secrets | ❌ KHÔNG push |

**Chi tiết `application.properties` (NGUY HIỂM - KHÔNG PUSH):**
```properties
# ⚠️ DATABASE PASSWORD - KEEP SECRET
spring.datasource.username=postgres
spring.datasource.password=your_password_here

# ⚠️ API_KEY IF NEEDED
api.key=secret_key_here

# ⚠️ JWT TOKEN SECRET
jwt.secret=super_secret_token_key
```

---

### 3️⃣ **Frontend Environment Files**

| File | Đường Dẫn | Chứa Gì | Xử Lý |
|------|---------|--------|-------|
| `.env.local` | `frontend-web/.env.local` | Secret API keys | ❌ KHÔNG push |
| `.env.production.local` | `frontend-web/.env.production.local` | Production secrets | ❌ KHÔNG push |
| `config.json` | `frontend-web/config.json` | API credentials | ❌ KHÔNG push |

**Chi tiết `.env.local` (NGUY HIỂM):**
```env
# ⚠️ Secret Keys
NEXT_PUBLIC_API_SECRET=your_secret_key
NEXT_PUBLIC_API_TOKEN=your_token
NEXT_PUBLIC_DB_PASSWORD=do_not_share
```

---

### 4️⃣ **AI Service Environment Files**

| File | Đường Dẫn | Chứa Gì | Xử Lý |
|------|---------|--------|-------|
| `.env` | `ai-service/.env` | API keys, secrets | ❌ KHÔNG push |
| `config.py` | `ai-service/config.py` | Database credentials | ❌ KHÔNG push |

**Chi tiết `ai-service/.env` (NGUY HIỂM):**
```env
# ⚠️ SECURITY KEYS
API_KEY=secret_key_from_provider
DB_PASSWORD=secret_password
SECRET_TOKEN=secret_token_here
ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080
```

---

### 5️⃣ **Credential & Key Files**

| File | Đường Dẫn | Chứa Gì | Xử Lý |
|------|---------|--------|-------|
| `credentials.json` | `credentials.json` | Google/AWS credentials | ❌ KHÔNG push |
| `auth.json` | `auth.json` | Authentication tokens | ❌ KHÔNG push |
| `*.key` | `*.key` (bất kỳ file .key) | Private keys | ❌ KHÔNG push |
| `*.pem` | `*.pem` (bất kỳ file .pem) | SSL certificates | ❌ KHÔNG push |
| `password.txt` | `password.txt` | Plaintext passwords | ❌ KHÔNG push |
| `secrets.json` | `secrets.json` | All kind of secrets | ❌ KHÔNG push |

---

### 6️⃣ **Docker Compose Override**

| File | Đường Dẫn | Chứa Gì | Xử Lý |
|------|---------|--------|-------|
| `docker-compose.override.yml` | `docker-compose.override.yml` | Local environment overrides | ❌ KHÔNG push |

**Ví dụ (NGUY HIỂM):**
```yaml
version: '3.8'
services:
  postgres:
    environment:
      POSTGRES_PASSWORD: your_dev_password  ⚠️ SENSITIVE
```

---

### 7️⃣ **Database Files**

| File | Đường Dẫn | Chứa Gì | Xử Lý |
|------|---------|--------|-------|
| `*.sql` | `*.sql` (bất kỳ file SQL) | Seed data với credentials | ❌ KHÔNG push |
| `*.sqlite` / `*.db` | `*.db`, `*.sqlite` | Database files | ❌ KHÔNG push |

---

### 8️⃣ **Private Directories**

| Thư Mục | Đường Dẫn | Chứa Gì | Xử Lý |
|--------|---------|--------|-------|
| `private/` | `private/` | Tất cả file riêng tư | ❌ KHÔNG push |
| `secrets/` | `secrets/` | Tất cả secrets | ❌ KHÔNG push |
| `.private/` | `.private/` | Hidden private files | ❌ KHÔNG push |

---

## ✅ Các File ĐƯỢC PHÉP Push

### ✅ Environment Templates (ANsafe)

| File | Đường Dẫn | Chứa Gì | Xử Lý |
|------|---------|--------|-------|
| `.env.example` | `.env.example` | ✅ CHỈ có template, KHÔNG password | ✅ CÓ THỂPUSH |
| `.env.develop.example` | `backend-core/.env.develop.example` | ✅ Template cho dev | ✅ CÓ THỂ PUSH |

**Ví dụ `.env.example` (SAFE TO PUSH):**
```env
# Database Configuration (Use your own values)
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/pbl4_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password_here

# AI Service
AI_SERVICE_URL=http://localhost:8000

# Frontend
NEXT_PUBLIC_API_URL=http://localhost:8080
```

---

## 🛡️ .gitignore Protection Rules

**Hiên tại `.C:\Users\imdrx\IdeaProjects\PBL4\.gitignore` đã có:**

```gitignore
# ==========================================
# ENVIRONMENT & CONFIGURATION (SENSITIVE)
# ==========================================
.env                      <- KHÔNG PUSH .env file
.env.local                <- KHÔNG PUSH local override
.env.*.local              <- KHÔNG PUSH any local-specific
.env.production.local     <- KHÔNG PUSH production local
*.key                     <- KHÔNG PUSH key files
*.pem                     <- KHÔNG PUSH certificate files
secrets/                  <- KHÔNG PUSH secrets folder
credentials.json          <- KHÔNG PUSH credentials
auth.json                 <- KHÔNG PUSH auth files
password*.txt             <- KHÔNG PUSH password files

# ==========================================
# PRIVATE FILES
# ==========================================
private/                  <- KHÔNG PUSH private folder
.private/                 <- KHÔNG PUSH .private folder
config.json               <- KHÔNG PUSH config files
```

---

## 📋 Checklist Trước Khi Push

### 🔍 Bước 1: Kiểm Tra File Staging

```bash
# Xem các file chuẩn bị push
git status

# Xem chi tiết thay đổi
git diff --cached
```

### 🔍 Bước 2: Tìm File Nhạy Cảm

```bash
# Tìm .env files
git ls-files --others --ignored --exclude-standard | grep -i "\.env"

# Tìm key files
git ls-files --others --ignored --exclude-standard | grep -i "\.key\|\.pem"

# Tìm credential files
git ls-files --others --ignored --exclude-standard | grep -i "credential\|password\|secret\|auth"
```

### ✅ Bước 3: Loại Trừ File Nhạy Cảm

```bash
# Nếu nhầm đã add file nhạy cảm
git reset HEAD <file_path>

# Xóa file khỏi staging nhưng giữ local
git rm --cached <file_path>
```

### 🔄 Bước 4: Commit & Push An Toàn

```bash
# Chỉ stage file cần thiết
git add -A
git commit -m "commit message"
git push origin main
```

---

## 📝 Template: Tạo .env File Sau Khi Clone

### Sau khi `git clone https://github.com/WWWTQD/PBL4.git`, tạo:

**1️⃣ Backend .env**
```bash
# Location: backend-core/src/main/resources/application.properties
# Copy từ .env.example
cp .env.example .env

# Edit .env với password của bạn
# Thay đổi:
spring.datasource.password=your_actual_password
ai.service.url=http://localhost:8000
```

**2️⃣ Frontend .env.local**
```bash
# Location: frontend-web/.env.local
# Tạo file mới
echo 'NEXT_PUBLIC_API_URL=http://localhost:8080' > .env.local
echo 'NEXT_PUBLIC_AI_SERVICE_URL=http://localhost:8000' >> .env.local
```

**3️⃣ AI Service .env**
```bash
# Location: ai-service/.env
# Copy từ template
echo 'ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080' > .env
echo 'PYTHON_ENV=development' >> .env
```

---

## 🚨 Nếu Vẫn Push Nhầm Sensitive Files

### Nếu đã push file nhạy cảm lên GitHub:

```bash
# 1. Remove file từ history (CẢNH BÁO: Rewrite history)
git rm --cached <file>
git commit --amend -m "Remove sensitive file"

# 2. Force push (CẢNH BÁO: Rewrite remote)
git push --force origin main

# 3. Change password/keys ngay lập tức!
# Vì chúng đã public trên GitHub!
```

---

## 📊 Comparison: PUSH vs NO PUSH

| File | Type | Push? | Lý Do |
|------|------|-------|-------|
| `.env` | Mật khẩu | ❌ NO | Chứa SPRING_DATASOURCE_PASSWORD |
| `.env.example` | Template | ✅ YES | Chỉ có placeholder, KHÔNG password |
| `application.properties` | Config | ❌ NO | Chứa database password |
| `*.key` | Keys | ❌ NO | Private keys không bao giờ push |
| `*.pem` | Certificates | ❌ NO | SSL private keys |
| `credentials.json` | Credentials | ❌ NO | API credentials |
| `docker-compose.override.yml` | Override | ❌ NO | Local secrets override |
| `README.md` | Documentation | ✅ YES | Và toàn bộ documentation |
| `pom.xml` | Config | ✅ YES | KHÔNG chứa password |
| `package.json` | Config | ✅ YES | KHÔNG chứa password |
| `requirements.txt` | Dependencies | ✅ YES | KHÔNG chứa password |

---

## 🔒 Best Practices

### 1️⃣ Luôn Tạo `.env.example`
```bash
# Template từ .env nhưng KHÔNG password
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/pbl4_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password_here  ← placeholder
```

### 2️⃣ Luôn Add Vào .gitignore
```
.env
.env.local
.env.*.local
*.key
*.pem
secrets/
credentials.json
password*.txt
```

### 3️⃣ Luôn Check Trước Push
```bash
# Verify git sẽ push gì
git diff --cached --name-only

# KHÔNG nên thấy:
# .env
# credentials.json
# *.key
# *.pem
# password.txt
```

### 4️⃣ Luôn Sử Dụng Environment Variables
```java
// Backend - Spring Boot
@Value("${spring.datasource.password}")
private String dbPassword;

@Value("${ai.service.url}")
private String aiServiceUrl;
```

```typescript
// Frontend - Next.js
const apiUrl = process.env.NEXT_PUBLIC_API_URL;
const aiServiceUrl = process.env.NEXT_PUBLIC_AI_SERVICE_URL;
```

```python
# AI Service - FastAPI
import os
db_password = os.getenv("DB_PASSWORD")
api_key = os.getenv("API_KEY")
```

---

## 📞 Quick Reference

### Files To NEVER Push:
```
❌ .env
❌ .env.local
❌ .env.production
❌ *.key
❌ *.pem
❌ credentials.json
❌ auth.json
❌ password*.txt
❌ config.json (với secrets)
❌ secrets/ (folder)
❌ private/ (folder)
❌ docker-compose.override.yml
```

### Files Safe To Push:
```
✅ .env.example
✅ README.md
✅ pom.xml
✅ package.json
✅ requirements.txt
✅ *.properties (config files)
✅ *.json (config files không có secrets)
✅ docker-compose.yml (mẫu)
✅ Source code (Java, Python, TypeScript)
✅ Documentation
```

---

## 🎓 Tóm Tắt

1. **Tạo `.env.example`** - Template KHÔNG password
2. **Add vào `.gitignore`** - Real `.env` files
3. **Check trước push** - `git diff --cached --name-only`
4. **Sử dụng environment variables** - KHÔNG hardcode password
5. **Lần sau** - Copy `.env.example` → `.env` → thêm password riêng

---

**✅ Bạn giờ biết cách quản lý file nhạy cảm an toàn!**


