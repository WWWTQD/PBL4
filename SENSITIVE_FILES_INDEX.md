# 📚 Index: Hướng Dẫn Quản Lý File Sensitive

**Bạn đang tìm thông tin về các file chứa mật khẩu? Đọc đây!**

---

## 📖 Ba File Hướng Dẫn Chính

### 1️⃣ **SENSITIVE_FILES_GUIDE.md** (Chi Tiết - Recommended!)
📍 **Đường dẫn:** `C:\Users\imdrx\IdeaProjects\PBL4\SENSITIVE_FILES_GUIDE.md`

**Dùng khi:** Bạn cần hiểu chi tiết từng file nhạy cảm là gì

**Nội dung:**
- ✅ Danh sách đầy đủ các file NOT to push
- ✅ Danh sách các file CÓ THỂ push
- ✅ Cách tạo `.env.example` template
- ✅ Checklist trước khi push
- ✅ Command để tìm file sensitive
- ✅ Nếu nhầm push - cách fix
- ✅ Best practices

**Ví dụ trong file:**
```
.env - Database Password - ❌ KHÔNG PUSH
.env.example - Template - ✅ CÓ THỂ PUSH
credentials.json - API keys - ❌ KHÔNG PUSH
*.key - Private keys - ❌ KHÔNG PUSH
```

---

### 2️⃣ **SENSITIVE_FILES_QUICK_REFERENCE.md** (Tóm Tắt - Quick Lookup!)
📍 **Đường dẫn:** `C:\Users\imdrx\IdeaProjects\PBL4\SENSITIVE_FILES_QUICK_REFERENCE.md`

**Dùng khi:** Bạn cần quick answer - file này push được không?

**Nội dung:**
- ✅ Danh sách nhanh file KHÔNG push (organized by priority)
- ✅ Danh sách file CÓ THỂ push
- ✅ Cách sử dụng lần sau (copy .env.example)
- ✅ Commands hữu ích (tìm file sensitive, check trước push)
- ✅ Checklist trước push
- ✅ Summary table

**Format:**
```
❌ .env - Chứa password
❌ credentials.json - API keys
✅ .env.example - Template (safe)
✅ README.md - Documentation
```

---

### 3️⃣ **SENSITIVE_FILES_LIST.csv** (CSV Table - For Reference!)
📍 **Đường dẫn:** `C:\Users\imdrx\IdeaProjects\PBL4\SENSITIVE_FILES_LIST.csv`

**Dùng khi:** 
- Bạn muốn import vào Excel/Google Sheets
- Bạn muốn danh sách dạng table
- Bạn muốn tìm kiếm theo Ctrl+F

**Columns:**
| File_Name | Full_Path | Contains | Should_Push | Priority |
|-----------|----------|----------|-------------|----------|
| .env | C:\...\PBL4\.env | Database Password | NO | CRITICAL |
| credentials.json | C:\...\PBL4\credentials.json | Google/AWS Keys | NO | CRITICAL |
| pom.xml | C:\...\backend-core\pom.xml | Dependencies Only | YES | SAFE |

---

## 🎯 Cách Chọn File Đọc

### "Tôi cần answer nhanh!"
👉 Đọc: **SENSITIVE_FILES_QUICK_REFERENCE.md** (5 phút)

### "Tôi cần hiểu kỹ!"
👉 Đọc: **SENSITIVE_FILES_GUIDE.md** (20 phút)

### "Tôi muốn danh sách dạng table/CSV!"
👉 Mở: **SENSITIVE_FILES_LIST.csv** (1 phút)

---

## ⚡ TL;DR (Very Quick Version)

### KHÔNG PUSH (Chứa Mật Khẩu):
```
❌ .env                (database password)
❌ credentials.json    (API keys)
❌ *.key, *.pem        (private keys)
❌ password.txt        (passwords!)
❌ docker-compose.override.yml
❌ secrets/ (folder)
❌ private/ (folder)
```

### CÓ THỂ PUSH (Templates):
```
✅ .env.example        (template without password)
✅ pom.xml             (Maven dependencies)
✅ package.json        (Node dependencies)
✅ requirements.txt    (Python dependencies)
✅ README.md           (Documentation)
✅ docker-compose.yml  (Docker template)
```

---

## 📋 Locations of Key Sensitive Files

| File | Path |
|------|------|
| **Database Password** | `backend-core/src/main/resources/application.properties` |
| **.env** | `C:\Users\imdrx\IdeaProjects\PBL4\.env` |
| **AI Service .env** | `ai-service/.env` |
| **Frontend .env.local** | `frontend-web/.env.local` |
| **Credentials** | `credentials.json` anywhere |
| **Private Keys** | `*.key`, `*.pem` anywhere |

---

## 🔄 Workflow Lần Sau

```bash
# 1. Clone project
git clone https://github.com/WWWTQD/PBL4.git
cd PBL4

# 2. Create .env từ template
cp .env.example .env

# 3. Edit .env với password của bạn
# Windows: notepad .env
# Linux/Mac: nano .env

# 4. Make your changes...

# 5. Before commit - CHECK
git status  # Don't see .env file? Good!
git diff --cached --name-only  # Don't see .env? Good!

# 6. Safe to commit & push
git add -A
git commit -m "My changes"
git push origin main
```

---

## ✅ .gitignore Đã Setup

**File:** `.gitignore` (đã có)

**Status:** ✅ Tự động protect các file sensitive

**Content includes:**
```
.env
.env.local
.env.*.local
*.key
*.pem
secrets/
credentials.json
password*.txt
private/
```

---

## 📞 Commands Quick Reference

### Tìm File Sensitive
```bash
git ls-files --others --ignored --exclude-standard | grep -i "\.env"
```

### Check Trước Push
```bash
git diff --cached --name-only
```

### Unstage File
```bash
git reset HEAD .env
```

### Xem Tất Cả Status
```bash
git status
```

---

## 🎓 Lessons Learned

1. ✅ Luôn tạo `.env.example` template
2. ✅ Luôn add sensitive files vào `.gitignore`
3. ✅ Luôn check `git status` trước push
4. ✅ Sử dụng environment variables, KHÔNG hardcode
5. ✅ Nếu nhầm push password - đổi password ngay! 🚨

---

## 📞 Need Help?

| Question | Answer Location |
|----------|-----------------|
| "Cái file này push được không?" | SENSITIVE_FILES_QUICK_REFERENCE.md |
| "Làm sao để bảo vệ password?" | SENSITIVE_FILES_GUIDE.md (Best Practices) |
| "Show tôi danh sách CSV!" | SENSITIVE_FILES_LIST.csv |
| "Nếu nhầm push sao?" | SENSITIVE_FILES_GUIDE.md (Troubleshooting) |
| "Lần sau làm gì?" | SENSITIVE_FILES_GUIDE.md (Workflow) |

---

## ✨ Bottom Line

**Nhớ 3 file này:**

1. 📄 **SENSITIVE_FILES_GUIDE.md** - Chi tiết đầy đủ
2. 📄 **SENSITIVE_FILES_QUICK_REFERENCE.md** - Tóm tắt nhanh
3. 📄 **SENSITIVE_FILES_LIST.csv** - Danh sách CSV

**Bookmark chúng để lần sau dễ tìm!** 🔖

---

**✅ Bạn giờ biết file nào push được, file nào KHÔNG!**


