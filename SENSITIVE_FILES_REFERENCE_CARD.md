# 📞 REFERENCE CARD - File Sensitive Checklist

**In lại cái này để tham khảo lần sau!**

---

## 🔴 KHÔNG PUSH (Copy-Paste vào .gitignore nếu cần)

```
# Environment Files
.env
.env.local
.env.*.local
.env.production.local

# Configuration
application.properties (với password)
docker-compose.override.yml

# Credentials
credentials.json
auth.json
secrets.json
password.txt
password*.txt

# Keys & Certificates
*.key
*.pem
*.crt
id_rsa
id_rsa.pub (maybe)

# Directories
secrets/
private/
.private/
.secrets/

# Database
*.sql (có seed data)
*.sqlite
*.sqlite3
*.db
```

---

## 🟢 CÓ THỂ PUSH

```
# Templates (SAFE!)
.env.example
.env.develop.example

# Config (SAFE!)
pom.xml
package.json
requirements.txt
application.properties.example

# Docker (SAFE!)
docker-compose.yml
Dockerfile

# Everything Else
README.md
*.java
*.py
*.tsx
*.ts
src/
documentation/
``` 

---

## 📍 Sensitive Files Paths

```
Database Password:
  └─ backend-core/src/main/resources/application.properties

Environment Files:
  ├─ C:\Users\imdrx\IdeaProjects\PBL4\.env
  ├─ ai-service/.env
  └─ frontend-web/.env.local

Credentials:
  ├─ credentials.json
  ├─ auth.json
  └─ secrets.json

Keys:
  ├─ *.key
  ├─ *.pem
  └─ id_rsa
```

---

## ✅ Commands Before Push

```bash
# 1. Check status
git status

# 2. Check what will be pushed
git diff --cached --name-only

# 3. MUST NOT SEE:
#    .env
#    credentials.json
#    *.key
#    *.pem
#    password.txt
#    secrets/ or private/

# 4. If good, push!
git add -A
git commit -m "message"
git push origin main
```

---

## 🚨 If Oops! (Accidentally Pushed)

```bash
# 1. Remove from git
git rm --cached .env
git commit -m "Remove .env"
git push origin main

# 2. ⚠️ CHANGE PASSWORD IMMEDIATELY!
#    It's now public on GitHub!
```

---

## 📚 Documentation Files

| File | Purpose | Time |
|------|---------|------|
| SENSITIVE_FILES_SUMMARY.md | This summary | 2 min |
| SENSITIVE_FILES_QUICK_REFERENCE.md | Quick lookup | 5 min |
| SENSITIVE_FILES_GUIDE.md | Detailed guide | 20 min |
| SENSITIVE_FILES_INDEX.md | Navigation guide | 3 min |
| SENSITIVE_FILES_LIST.csv | CSV table | 1 min |

---

## 🔍 FindOperations

```bash
# Find .env files
find . -name ".env*" -type f

# Find key files
find . -name "*.key" -o -name "*.pem"

# Find credentials
find . -name "credentials.json" -o -name "auth.json"

# Git check
git ls-files --others --ignored --exclude-standard
```

---

## 💾 Quick Workflow

```bash
# Step 1: Clone
git clone https://github.com/WWWTQD/PBL4.git
cd PBL4

# Step 2: Create .env
cp .env.example .env

# Step 3: Edit with YOUR password
notepad .env  # Windows
nano .env     # Mac/Linux

# Step 4: Make changes
# ... edit files ...

# Step 5: Verify safe
git status  # .env should NOT appear

# Step 6: Commit & Push
git add -A
git commit -m "Add feature"
git push origin main
```

---

## ⚡ TL;DR

| Action | Command / Check |
|--------|---------|
| Before add | `git status` - no `.env` |
| Before push | `git diff --cached --name-only` - no sensitive files |
| Copy template | `cp .env.example .env` |
| Edit template | `notepad .env` then add real password |
| On .gitignore | `.env`, `*.key`, `secrets/`, `private/` |
| Safe to push | `pom.xml`, `package.json`, `README.md`, source code |

---

## 🎯 Remember

1. ✅ `.env.example` with placeholders → SAFE TO PUSH
2. ❌ `.env` with real password → NEVER PUSH
3. ✅ `pom.xml`, `package.json` → SAFE TO PUSH
4. ❌ `credentials.json`, `*.key` → NEVER PUSH
5. ✅ Documentation, source code → SAFE TO PUSH
6. ❌ Database files, logs → NEVER PUSH

---

## 📌 Bookmark These!

```
SENSITIVE_FILES_SUMMARY.md
└─ Start point for reference

SENSITIVE_FILES_QUICK_REFERENCE.md
└─ Quick answer lookup (5 min)

SENSITIVE_FILES_GUIDE.md
└─ Detailed guide (20 min)

SENSITIVE_FILES_INDEX.md
└─ Navigation guide (all files)

SENSITIVE_FILES_LIST.csv
└─ CSV table for Excel
```

---

## ✨ YOU ARE NOW SAFE!

✅ Passwords not on GitHub  
✅ Credentials protected  
✅ Keys never revealed  
✅ .gitignore properly setup  
✅ All templates ready  
✅ Workflow documented  

---

**Ready for next commit & push!** 🚀

---

**Print This Card! 🖨️**


