# PBL4 AI Service

**FastAPI-based Image Processing and OCR Service**

This service handles heavy computational tasks for the PBL4 system, including image deskewing, denoising, and OCR text extraction.

## 🚀 Quick Start

### Prerequisites
- Python 3.10+
- Tesseract OCR (optional, for production OCR)

### Installation

1. **Create a virtual environment:**
   ```bash
   python -m venv venv
   
   # Windows
   venv\Scripts\activate
   
   # macOS/Linux
   source venv/bin/activate
   ```

2. **Install dependencies:**
   ```bash
   pip install -r requirements.txt
   ```

3. **Create `.env` file:**
   ```bash
   cp .env.example .env
   # Edit .env with your configuration
   ```

4. **Run the service:**
   ```bash
   uvicorn main:app --reload --host 0.0.0.0 --port 8000
   ```

The API will be available at: **http://localhost:8000**

---

## 📚 API Endpoints

### Health Check
- **GET** `/health`
  - Returns service health status

### Process Single Image
- **POST** `/api/v1/process-image`
  - **Description:** Process a single image (deskew, denoise, OCR)
  - **Request Body:** Form data with `file` (image file)
  - **Returns:** Processed image (base64) + extracted text

  **Example Request:**
  ```bash
  curl -X POST "http://localhost:8000/api/v1/process-image" \
    -H "accept: application/json" \
    -F "file=@document.jpg"
  ```

  **Response:**
  ```json
  {
    "success": true,
    "message": "Image processed successfully",
    "data": {
      "processedImage": "iVBORw0KGgoAAAANSUhEUgAA...",
      "extractedText": "Document Title: ...",
      "metadata": {
        "originalFileName": "document.jpg",
        "originalWidth": 1920,
        "originalHeight": 1080,
        "processingStatus": "completed",
        "timestamp": "2026-08-25T00:00:00Z"
      }
    }
  }
  ```

### Process Batch Images
- **POST** `/api/v1/process-batch`
  - **Description:** Process multiple images
  - **Request Body:** Form data with multiple `files`
  - **Returns:** Array of processed results

  **Example Request:**
  ```bash
  curl -X POST "http://localhost:8000/api/v1/process-batch" \
    -H "accept: application/json" \
    -F "files=@document1.jpg" \
    -F "files=@document2.jpg"
  ```

### Root Endpoint
- **GET** `/`
  - Returns service information and available endpoints

---

## 🛠️ Technologies

- **FastAPI** - Async Python web framework
- **OpenCV** - Image processing
- **Uvicorn** - ASGI server
- **Pillow** - Image manipulation
- **Python-multipart** - File upload handling

---

## 🔧 Architecture

### Image Processing Pipeline

```
Uploaded Image
    ↓
Convert to Grayscale
    ↓
Apply Gaussian Blur (Denoise)
    ↓
Apply Bilateral Filter (Edge Preservation)
    ↓
OCR Text Extraction
    ↓
Encode to Base64
    ↓
Return JSON Response
```

### Key Components

#### ImageProcessor Class
Handles all image processing operations:
- `convert_image_to_np()` - Upload bytes → OpenCV array
- `process_image()` - Grayscale, denoise, filter
- `extract_text_ocr()` - Simulated OCR (later integrated with Tesseract)
- `image_to_base64()` - Convert processed image for API response

---

## 🔐 CORS Configuration

The service is configured to accept requests from:
- `http://localhost:3000` (Next.js Frontend)
- `http://localhost:8080` (Spring Boot Backend)

Modify `ALLOWED_ORIGINS` in `.env` to change this.

---

## 📊 Response Status Codes

| Status | Description |
|--------|-------------|
| 200 | Image processed successfully |
| 400 | Invalid image format or empty file |
| 500 | Server error during processing |

---

## 🧪 Testing

### Manual Testing with curl

```bash
# Health check
curl http://localhost:8000/health

# Process image
curl -X POST "http://localhost:8000/api/v1/process-image" \
  -F "file=@test_image.jpg"

# View API documentation
# Open: http://localhost:8000/docs (Swagger UI)
# Or:   http://localhost:8000/redoc (ReDoc)
```

### Python Testing

```python
import requests

with open("test_image.jpg", "rb") as f:
    files = {"file": f}
    response = requests.post("http://localhost:8000/api/v1/process-image", files=files)
    result = response.json()
    print(result)
```

---

## 📝 Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `ALLOWED_ORIGINS` | CORS allowed origins (comma-separated) | `http://localhost:3000,http://localhost:8080` |
| `PYTHON_ENV` | Environment mode | `development` |

---

## 🎯 Integration with Backend

The Spring Boot backend calls this service via `AiIntegrationService`:

```java
// Example Backend Call
RestTemplate restTemplate = new RestTemplate();
MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
body.add("file", new FileSystemResource("path/to/image.jpg"));

HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.MULTIPART_FORM_DATA);

HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
ResponseEntity<Map> response = restTemplate.exchange(
    "http://localhost:8000/api/v1/process-image",
    HttpMethod.POST,
    entity,
    Map.class
);
```

---

## 🚀 Production Deployment

For production deployment:

1. **Install Tesseract OCR:**
   - Ubuntu: `sudo apt-get install tesseract-ocr`
   - macOS: `brew install tesseract`
   - Windows: Download from [GitHub Tesseract](https://github.com/UB-Mannheim/tesseract/wiki)

2. **Update OCR implementation in `main.py`:**
   ```python
   import pytesseract
   extracted_text = pytesseract.image_to_string(img)
   ```

3. **Run with Gunicorn/Uvicorn:**
   ```bash
   uvicorn main:app --host 0.0.0.0 --port 8000 --workers 4
   ```

4. **Docker Support:**
   ```dockerfile
   FROM python:3.10-slim
   WORKDIR /app
   COPY requirements.txt .
   RUN pip install --no-cache-dir -r requirements.txt
   COPY . .
   CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
   ```

---

## 📄 License

Part of the PBL4 initiative.

---

## 💬 Support

For issues or questions, refer to the main PBL4 README.md.

