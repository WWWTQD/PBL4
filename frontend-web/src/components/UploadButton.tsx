'use client';

/**
 * Upload Button Component
 * Handles document file uploads to the backend
 */

import React, { useState, useRef } from 'react';
import { documentApi } from '@/services/api';

interface UploadButtonProps {
  onSuccess?: () => void;
  onError?: (error: string) => void;
}

export default function UploadButton({ onSuccess, onError }: UploadButtonProps) {
  const [isLoading, setIsLoading] = useState(false);
  const [fileName, setFileName] = useState<string>('');
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    setFileName(file.name);
    setIsLoading(true);

    try {
      const response = await documentApi.uploadDocument(file);
      if (response.success) {
        alert('Document uploaded and processed successfully!');
        setFileName('');
        if (fileInputRef.current) {
          fileInputRef.current.value = '';
        }
        onSuccess?.();
      } else {
        const error = response.error || 'Upload failed';
        alert(`Error: ${error}`);
        onError?.(error);
      }
    } catch (error) {
      const errorMsg = error instanceof Error ? error.message : 'Upload failed';
      console.error('Upload error:', error);
      alert(`Error uploading file: ${errorMsg}`);
      onError?.(errorMsg);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="flex items-center gap-4">
      <input
        ref={fileInputRef}
        type="file"
        accept="image/*"
        onChange={handleFileChange}
        disabled={isLoading}
        className="hidden"
        aria-label="Upload document"
      />

      <button
        onClick={() => fileInputRef.current?.click()}
        disabled={isLoading}
        className={`px-6 py-2 rounded-lg font-semibold transition-all ${
          isLoading
            ? 'bg-gray-400 text-gray-600 cursor-not-allowed'
            : 'bg-green-500 text-white hover:bg-green-600 cursor-pointer'
        }`}
      >
        {isLoading ? (
          <>
            <span className="inline-block animate-spin mr-2">⏳</span>
            Uploading...
          </>
        ) : (
          '📤 Upload Scan'
        )}
      </button>

      {fileName && (
        <span className="text-sm text-gray-600">
          Selected: {fileName}
        </span>
      )}
    </div>
  );
}

