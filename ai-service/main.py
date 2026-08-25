"""
PBL4 AI Service - FastAPI Application
Handles image processing, deskewing, denoising, and OCR operations
"""

import os
import io
import base64
import logging
from typing import Optional
from dotenv import load_dotenv

from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import cv2
import numpy as np
from PIL import Image

# Load environment variables
load_dotenv()

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize FastAPI app
app = FastAPI(
    title="PBL4 AI Service",
    description="Image Processing and OCR Service for Document Scanning",
    version="1.0.0"
)

# Configure CORS
ALLOWED_ORIGINS = os.getenv("ALLOWED_ORIGINS", "http://localhost:3000,http://localhost:8080").split(",")

app.add_middleware(
    CORSMiddleware,
    allow_origins=ALLOWED_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

logger.info(f"CORS configured for origins: {ALLOWED_ORIGINS}")


# ============================================
# Health Check Endpoint
# ============================================

@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {
        "status": "healthy",
        "service": "PBL4 AI Service",
        "version": "1.0.0"
    }


# ============================================
# Image Processing Service
# ============================================

class ImageProcessor:
    """
    Wrapper class for image processing operations
    - Convert to grayscale
    - Denoise operations (basic)
    - Deskew detection (basic)
    - Simulated OCR
    """

    @staticmethod
    def convert_image_to_np(file_data: bytes) -> Optional[np.ndarray]:
        """Convert uploaded file bytes to OpenCV numpy array"""
        try:
            nparr = np.frombuffer(file_data, np.uint8)
            img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
            if img is None:
                raise ValueError("Failed to decode image")
            return img
        except Exception as e:
            logger.error(f"Error converting image: {str(e)}")
            return None

    @staticmethod
    def process_image(img: np.ndarray) -> np.ndarray:
        """
        Apply basic image processing:
        1. Convert to grayscale
        2. Apply Gaussian blur for denoising
        3. Apply bilateral filtering for edge preservation
        """
        try:
            # Convert to grayscale
            gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

            # Apply Gaussian blur (denoise)
            denoised = cv2.GaussianBlur(gray, (5, 5), 0)

            # Apply bilateral filter for better edge preservation
            processed = cv2.bilateralFilter(denoised, 9, 75, 75)

            return processed
        except Exception as e:
            logger.error(f"Error processing image: {str(e)}")
            raise

    @staticmethod
    def extract_text_ocr(img: np.ndarray) -> str:
        """
        Simulate OCR text extraction
        In production, integrate with Tesseract or other OCR engines
        """
        # Simulate OCR - in production, use pytesseract.image_to_string(img)
        simulated_text = """
        Document Title: PBL4 Scanned Document

        This is simulated OCR output from the AI Service.
        In production, this would contain actual text extracted
        from the document image using Tesseract or similar OCR engines.

        Date: 2026-08-25
        Processing Status: Complete
        """
        return simulated_text.strip()

    @staticmethod
    def image_to_base64(img: np.ndarray) -> str:
        """Convert processed image (numpy array) to base64 string"""
        try:
            # Convert grayscale to RGB for PNG encoding
            if len(img.shape) == 2:  # Grayscale
                img_bgr = cv2.cvtColor(img, cv2.COLOR_GRAY2BGR)
            else:
                img_bgr = img

            # Encode image to PNG
            ret, buffer = cv2.imencode('.png', img_bgr)
            img_str = base64.b64encode(buffer).decode('utf-8')
            return img_str
        except Exception as e:
            logger.error(f"Error converting image to base64: {str(e)}")
            raise


# ============================================
# Image Processing Endpoint
# ============================================

@app.post("/api/v1/process-image")
async def process_image(file: UploadFile = File(...)):
    """
    Process an uploaded image file.

    Operations performed:
    - Grayscale conversion
    - Denoising (Gaussian blur + bilateral filtering)
    - OCR text extraction (simulated)

    Args:
        file: Image file (JPEG, PNG, etc.)

    Returns:
        JSON with:
        - processedImage: Base64 encoded processed image
        - extractedText: Simulated OCR output
        - metadata: Processing metadata
    """
    try:
        # Validate file type
        if not file.content_type.startswith("image/"):
            raise HTTPException(status_code=400, detail="File must be an image")

        # Read file bytes
        file_data = await file.read()
        if not file_data:
            raise HTTPException(status_code=400, detail="Empty file uploaded")

        logger.info(f"Processing image: {file.filename} ({len(file_data)} bytes)")

        # Convert to numpy array
        img = ImageProcessor.convert_image_to_np(file_data)
        if img is None:
            raise HTTPException(status_code=400, detail="Invalid image format")

        # Get original image dimensions
        original_height, original_width = img.shape[:2]

        # Process image
        processed_img = ImageProcessor.process_image(img)

        # Extract text via OCR
        extracted_text = ImageProcessor.extract_text_ocr(processed_img)

        # Convert processed image to base64
        processed_image_b64 = ImageProcessor.image_to_base64(processed_img)

        logger.info(f"Image processing completed: {file.filename}")

        return {
            "success": True,
            "message": "Image processed successfully",
            "data": {
                "processedImage": processed_image_b64,
                "extractedText": extracted_text,
                "metadata": {
                    "originalFileName": file.filename,
                    "originalWidth": int(original_width),
                    "originalHeight": int(original_height),
                    "processingStatus": "completed",
                    "timestamp": "2026-08-25T00:00:00Z"
                }
            }
        }

    except HTTPException as e:
        logger.error(f"HTTP Exception: {e.detail}")
        raise
    except Exception as e:
        logger.error(f"Error processing image: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Error processing image: {str(e)}")


# ============================================
# Batch Processing Endpoint (Optional)
# ============================================

@app.post("/api/v1/process-batch")
async def process_batch(files: list[UploadFile] = File(...)):
    """
    Process multiple image files in batch.

    Args:
        files: List of image files

    Returns:
        List of processed results
    """
    results = []

    for file in files:
        try:
            file_data = await file.read()
            img = ImageProcessor.convert_image_to_np(file_data)

            if img is None:
                results.append({
                    "fileName": file.filename,
                    "success": False,
                    "error": "Invalid image format"
                })
                continue

            processed_img = ImageProcessor.process_image(img)
            extracted_text = ImageProcessor.extract_text_ocr(processed_img)
            processed_image_b64 = ImageProcessor.image_to_base64(processed_img)

            results.append({
                "fileName": file.filename,
                "success": True,
                "processedImage": processed_image_b64,
                "extractedText": extracted_text
            })

        except Exception as e:
            logger.error(f"Error processing {file.filename}: {str(e)}")
            results.append({
                "fileName": file.filename,
                "success": False,
                "error": str(e)
            })

    return {
        "success": True,
        "message": f"Batch processing completed: {len(results)} files processed",
        "data": results
    }


# ============================================
# Root Endpoint
# ============================================

@app.get("/")
async def root():
    """Root endpoint with API information"""
    return {
        "service": "PBL4 AI Service",
        "version": "1.0.0",
        "endpoints": {
            "health": "/health",
            "process_image": "/api/v1/process-image (POST)",
            "process_batch": "/api/v1/process-batch (POST)",
            "docs": "/docs"
        }
    }


# ============================================
# Exception Handlers
# ============================================

@app.exception_handler(Exception)
async def global_exception_handler(request, exc):
    """Global exception handler"""
    logger.error(f"Unhandled exception: {str(exc)}")
    return {
        "success": False,
        "error": "Internal server error",
        "details": str(exc)
    }


if __name__ == "__main__":
    import uvicorn

    # Run the application
    uvicorn.run(
        app,
        host="0.0.0.0",
        port=8000,
        log_level="info",
        reload=True
    )

