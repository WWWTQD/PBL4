# 🔐 Danh Sách File Sensitive - Tóm Tắt Nhanh

**Quick Reference - Các file KHÔNG được push lên GitHub**

---

## 📍 Các File Chứa Mật Khẩu/Credentials

### 🔴 KHÔNG PUSH - Priority: CRITICAL

#### Environment Files (Mật khẩu Database)
```
❌ .env                              <- Chứa database password
❌ .env.local                        <- Development secrets
❌ .env.production                   <- Production secrets
❌ .env.production.local
❌ .env.*.local
```

**Đường dẫn:**
```
C:\Users\imdrx\IdeaProjects\PBL4\.env
C:\Users\imdrx\IdeaProjects\PBL4\.env.local
C:\Users\imdrx\IdeaProjects\PBL4\.env.production
```

---

#### Backend Configuration
```
❌ backend-core/src/main/resources/application.properties
   (Chứa: spring.datasource.password)

❌ backend-core/src/main/resources/application-prod.properties
   (Chứa: production database password)
```

**Mật khẩu có trong file:**
```properties
# NGUY HIỂM - KHÔNG PUSH
spring.datasource.password=your_actual_password
spring.datasource.username=postgres
jwt.secret=secret_value
api.key=api_key_value
```

---

#### Frontend Configuration
```
❌ frontend-web/.env.local           <- NEXT_PUBLIC secrets
❌ frontend-web/.env.production.local
❌ frontend-web/config.json          (nếu có credentials)
```

**Đường dẫn:**
```
C:\Users\imdrx\IdeaProjects\PBL4\frontend-web\.env.local
C:\Users\imdrx\IdeaProjects\PBL4\frontend-web\.env.production.local
```

---

#### AI Service Configuration
```
❌ ai-service/.env                   <- API keys, DB password
❌ ai-service/config.py              (nếu hardcode secrets)
```

**Đường dẫn:**
```
C:\Users\imdrx\IdeaProjects\PBL4\ai-service\.env
```

---

### 🔴 KHÔNG PUSH - Priority: HIGH

#### Credential Files
```
❌ credentials.json                  <- Google/AWS keys
❌ auth.json                         <- Authentication tokens
❌ password.txt                      <- Plaintext passwords
❌ secrets.json                      <- All secrets
❌ config.json                       <- Configuration với credentials
```

#### Key & Certificate Files
```
❌ *.key                             <- Private keys
❌ *.pem                             <- SSL certificates
❌ *.crt                             <- Certificates
❌ id_rsa                            <- SSH keys
❌ id_rsa.pub
```

#### Database Files
```
❌ *.sql                             <- Database dumps
❌ *.sqlite                          <- SQLite database
❌ *.sqlite3
❌ *.db
❌ dump.sql
```

---

### 🟡 KHÔNG PUSH - Priority: MEDIUM

#### Docker Files
```
❌ docker-compose.override.yml       <- Local environment overrides
❌ .dockerignore.local
```

#### Private Directories
```
❌ private/                          <- Private folder
❌ secrets/                          <- Secrets folder
❌ .private/                         <- Hidden private folder
❌ .secrets/
```

#### Log Files (Optional)
```
❌ *.log                             <- Application logs
❌ logs/
```

---

## ✅ ĐƯỢC PUSH - Templates & Safe Files

### ✅ Environment Templates
```
✅ .env.example                      <- Template KHÔNG password
                                        Được push để template
✅ .env.develop.example
✅ backend-core/.env.example
✅ frontend-web/.env.example
✅ ai-service/.env.example
```

**Nội dung ví dụ (SAFE):**
```env
# These are PLACEHOLDERS, not actual passwords
SPRING_DATASOURCE_PASSWORD=your_password_here
SPRING_DATASOURCE_USERNAME=postgres
AI_SERVICE_URL=http://localhost:8000
```

---

### ✅ Configuration Files (KHÔNG password)
```
✅ pom.xml                           <- Maven config (SAFE)
✅ package.json                      <- Node config (SAFE)
✅ requirements.txt                  <- Python deps (SAFE)
✅ application.properties.sample
   (nhưng KHÔNG password)
```

### ✅ Documentation & Source
```
✅ README.md
✅ SENSITIVE_FILES_GUIDE.md
✅ src/ (tất cả source code)
✅ docker-compose.yml (template)
✅ Dockerfile
✅ .gitignore
```

---

## 🚀 Cách Sử Dụng Lần Sau

### Khi Clone Project Lần Đầu
```bash
git clone https://github.com/WWWTQD/PBL4.git
cd PBL4

# 1. Tạo .env từ template
cp .env.example .env

# 2. Edit .env với password của bạn
# Windows: notepad .env
# macOS: nano .env
# Linux: vim .env

# 3. Tương tự cho frontend
cd frontend-web
cp .env.example .env.local
# Edit .env.local nếu cần

cd ../ai-service
cp .env.example .env
# Edit .env nếu cần
```

---

### Trước Khi Push
```bash
# 1. Verify không có file sensitive
git status
# Không nên thấy: .env, credentials.json, *.key, etc.

# 2. Check chi tiết
git diff --cached --name-only
# Không nên thấy file nhạy cảm nào

# 3. Commit & Push an toàn
git add -A
git commit -m "Add my changes"
git push origin main
```

---

### Nếu Nhầm Push File Sensitive
```bash
# 1. Xóa khỏi git history
git rm --cached .env
git commit -m "Remove .env file"
git push origin main

# 2. ⚠️ LƯU Ý: Password đã public trên GitHub!
# Phải đổi password ngay lập tức!

# 3. Add vào .gitignore (nếu chưa có)
echo ".env" >> .gitignore
git add .gitignore
git commit -m "Add .env to gitignore"
git push origin main
```

---

## 📋 .gitignore Đã Được Setup

**File:** `C:\Users\imdrx\IdeaProjects\PBL4\.gitignore`

Đã chứa:
```gitignore
# Environment files
.env
.env.local
.env.*.local
*.key
*.pem
secrets/
credentials.json
auth.json
password*.txt
private/
```

**Status:** ✅ Bảo vệ tự động - không cần lo!

---

## 🔍 Commands Hữu Ích

### Tìm File Sensitive
```bash
# Tìm .env files
git ls-files --others --ignored --exclude-standard | grep -i "\.env"

# Tìm key files
git ls-files --others --ignored --exclude-standard | grep -i "\.key\|\.pem"

# Tìm credentials
git ls-files --others --ignored --exclude-standard | grep -i "credential\|password"
```

### Kiểm Tra Trước Push
```bash
# Xem tất cả file sẽ push
git diff --cached --name-only

# Xem chi tiết thay đổi
git diff --cached

# Xem status
git status
```

### Unstage File
```bash
# Unstage cụ thể
git reset HEAD .env

# Nếu đã commit, sửa
git reset --soft HEAD~1
git reset HEAD .env
git commit -m "Updated commit without .env"
```

---

## ⚡ Checklist Trước Push

- [ ] `.env` file: KHÔNG thêm vào staging
- [ ] `credentials.json`: KHÔNG thấy trong `git status`
- [ ] `*.key`, `*.pem`: KHÔNG thấy trong `git diff --cached`
- [ ] Password files: KHÔNG có trong staging
- [ ] `docker-compose.override.yml`: Not in staging
- [ ] Run `git status` - chỉ thấy public files
- [ ] Run `git diff --cached --name-only` - SAFE to push
- [ ] OK để push! ✅

---

## 📞 Summary

| File | Đường Dẫn | Push? | Ghi Chú |
|------|---------|-------|--------|
| `.env` | PBL4/ | ❌ | Chứa password |
| `.env.example` | PBL4/ | ✅ | Template safe |
| `.env.local` | PBL4/ | ❌ | Local secrets |
| `application.properties` | backend-core/src/main/resources/ | ❌ | Password DB |
| `.env` | ai-service/ | ❌ | API keys |
| `.env.local` | frontend-web/ | ❌ | Secrets |
| `credentials.json` | PBL4/ | ❌ | AWS/Google keys |
| `*.key` | PBL4/ | ❌ | Private keys |
| `*.pem` | PBL4/ | ❌ | Certificates |
| `password.txt` | PBL4/ | ❌ | Plaintext pwd |
| `secrets/` | PBL4/secrets/ | ❌ | Secret folder |
| `private/` | PBL4/private/ | ❌ | Private folder |

---

## ✨ Bottom Line

**Nhớ:**
1. ✅ `.env.example` được push (template)
2. ❌ `.env` KHÔNG push (có password)
3. ✅ Tất cả documentation được push
4. ❌ Tất cả credentials KHÔNG push
5. ✅ .gitignore đã bảo vệ bạn

**Lần sau:** Copy `.env.example` → `.env` → thêm password → git sẽ ignore nó tự động!

---

**📄 File:** `SENSITIVE_FILES_GUIDE.md` (chi tiết đầy đủ)

**✅ Bạn giờ biết cách xử lý file sensitive!**


