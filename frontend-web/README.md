# Frontend - Next.js Web Dashboard

**Modern Next.js 14 Dashboard for PBL4 Document Scanning System**

A responsive, type-safe React frontend with TypeScript and Tailwind CSS for managing document scans and processing.

## 🚀 Quick Start

### Prerequisites
- **Node.js 18+**
- **npm** or **yarn**
- Backend running on `http://localhost:8080`
- AI Service running on `http://localhost:8000`

### Installation & Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend-web
   ```

2. **Install dependencies:**
   ```bash
   npm install
   # or
   yarn install
   ```

3. **Configure Environment Variables:**
   Create `.env.local` file:
   ```env
   NEXT_PUBLIC_API_URL=http://localhost:8080
   NEXT_PUBLIC_AI_SERVICE_URL=http://localhost:8000
   NEXT_PUBLIC_APP_NAME=PBL4 Scan System
   ```

4. **Run Development Server:**
   ```bash
   npm run dev
   # or
   yarn dev
   ```

   Open browser: **http://localhost:3000**

---

## 📁 Project Structure

```
frontend-web/
├── src/
│   ├── app/
│   │   ├── page.tsx              # Main dashboard page
│   │   ├── layout.tsx            # Root layout wrapper
│   │   └── globals.css           # Global Tailwind styles
│   ├── components/
│   │   ├── Sidebar.tsx           # Navigation sidebar
│   │   ├── Dashboard.tsx         # Recent scans table
│   │   └── UploadButton.tsx      # File upload handler
│   └── services/
│       └── api.ts                # API client & types
├── public/                        # Static assets
├── package.json
├── tsconfig.json
├── tailwind.config.ts
├── next.config.js
├── .eslintrc.json
└── README.md
```

---

## 🎨 UI Components

### Sidebar (`src/components/Sidebar.tsx`)
Navigation component with links to:
- Dashboard (main page)
- Scans (document list)
- Upload (manual upload)
- Settings (configuration)

### Dashboard (`src/components/Dashboard.tsx`)
Displays recent documents in a sortable table:
- Document filename
- Scan date/time
- Processing status (PENDING, PROCESSING, COMPLETED, FAILED)
- Action links (View details, Download)

### UploadButton (`src/components/UploadButton.tsx`)
File upload handler with:
- Image file validation
- Upload progress indication
- Error handling
- Success notifications

---

## 🔌 API Integration

### API Service (`src/services/api.ts`)
Axios-based HTTP client with:

**Endpoints:**
```typescript
// Get all documents
documentApi.getAllDocuments()

// Get recent documents (last 10)
documentApi.getRecentDocuments()

// Get document by ID
documentApi.getDocumentById(id)

// Get documents by status
documentApi.getDocumentsByStatus(status)

// Upload and process document
documentApi.uploadDocument(file, notes)

// Update document
documentApi.updateDocument(id, updates)

// Delete document
documentApi.deleteDocument(id)

// Check system health
documentApi.checkSystemHealth()
```

### TypeScript Types
```typescript
export interface Document {
  id: number;
  fileName: string;
  scanDate: string;
  status: DocumentStatus;
  extractedText?: string;
  fileUrl?: string;
  processedImage?: string;
  createdAt: string;
  updatedAt: string;
  notes?: string;
}

export type DocumentStatus = 'PENDING' | 'PROCESSING' | 'COMPLETED' | 'FAILED' | 'ARCHIVED';
```

---

## 🎯 Features

✅ **Responsive Design** - Mobile-first approach with Tailwind CSS  
✅ **Type Safety** - Full TypeScript support  
✅ **Real-time Updates** - Automatic refresh after uploads  
✅ **Error Handling** - Comprehensive error messages  
✅ **API Integration** - Seamless backend communication  
✅ **System Health** - Real-time backend/AI service status  
✅ **File Upload** - Drag-and-drop supported  
✅ **Status Tracking** - Visual document status indicators  

---

## 🛠️ Technologies

| Technology | Version | Purpose |
|-----------|---------|---------|
| **Next.js** | 14.0.4 | React framework |
| **React** | 18.2.0 | UI library |
| **TypeScript** | 5.3.3 | Type safety |
| **Tailwind CSS** | 3.4.1 | Styling |
| **Axios** | 1.6.2 | HTTP client |

---

## 📝 Configuration Files

### Environment Variables (`.env.local`)
```env
NEXT_PUBLIC_API_URL=http://localhost:8080
NEXT_PUBLIC_AI_SERVICE_URL=http://localhost:8000
NEXT_PUBLIC_APP_NAME=PBL4 Scan System
NEXT_PUBLIC_APP_VERSION=1.0.0
```

### Tailwind Configuration (`tailwind.config.ts`)
Custom color scheme:
- Primary: `#3b82f6` (Blue)
- Secondary: `#10b981` (Green)
- Danger: `#ef4444` (Red)
- Warning: `#f59e0b` (Amber)

### TypeScript Configuration (`tsconfig.json`)
- Target: ES2020
- Module: ESNext
- JSX: react-jsx
- Path aliases: `@/*` → `./src/*`

---

## 🏗️ Build & Deploy

### Development Build
```bash
npm run dev
```

### Production Build
```bash
npm run build
npm start
```

### Type Checking
```bash
npm run type-check
```

### Linting
```bash
npm run lint
```

---

## 🐳 Docker Support

```dockerfile
FROM node:18-alpine as builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM node:18-alpine
WORKDIR /app
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/public ./public
COPY --from=builder /app/package*.json ./
EXPOSE 3000
CMD ["npm", "start"]
```

Build: `docker build -t pbl4-frontend .`

---

## 💡 Usage Examples

### Upload a Document
```typescript
import { documentApi } from '@/services/api';

const file = new File([imageBinary], 'scan.jpg', { type: 'image/jpeg' });
const response = await documentApi.uploadDocument(file, 'Optional notes');

if (response.success) {
  console.log('Processed image:', response.data?.processedImage);
  console.log('Extracted text:', response.data?.extractedText);
}
```

### Fetch Recent Scans
```typescript
const response = await documentApi.getRecentDocuments();
documents = response.data; // List of last 10 documents
```

### Track Document Status
```typescript
const document = await documentApi.getDocumentById(1);
console.log('Status:', document.status); // PENDING, PROCESSING, COMPLETED, etc.
```

---

## 📊 Dashboard Features

1. **Recent Scans Table**
   - Lists last 10 uploaded documents
   - Shows filename, scan date, status
   - Quick action buttons

2. **Upload Section**
   - File upload with progress
   - Image format validation
   - Status feedback

3. **System Health**
   - Real-time backend status
   - AI service connectivity
   - Health indicators

4. **Feature Cards**
   - Documents overview
   - AI processing info
   - Secure storage info

---

## 🎓 Best Practices

- ✅ Componentized architecture
- ✅ Custom hooks for state management
- ✅ Consistent error handling
- ✅ Type safety throughout
- ✅ Responsive design patterns
- ✅ Clean code principles
- ✅ Performance optimization

---

## 🚀 Performance Optimization

- Image optimization via Next.js Image component
- Code splitting and lazy loading
- CSS minification with Tailwind
- TypeScript strict mode for safety

---

## 📄 License

Part of the PBL4 initiative.

---

## 💬 Support

For issues or questions:
1. Check the main PBL4 README.md
2. Review Next.js documentation
3. Check API service documentation

