/**
 * API Service Module
 * Handles all HTTP requests to the Spring Boot backend
 * Base URL: http://localhost:8080
 */

import axios, { AxiosInstance, AxiosError } from 'axios';

// Define API response types
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
  error?: string;
  count?: number;
}

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

// Initialize Axios instance
const apiClient: AxiosInstance = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor
apiClient.interceptors.request.use(
  (config) => {
    console.log(`[API] ${config.method?.toUpperCase()} ${config.url}`);
    return config;
  },
  (error) => {
    console.error('[API] Request error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor
apiClient.interceptors.response.use(
  (response) => {
    console.log(`[API] Response:`, response.data);
    return response;
  },
  (error: AxiosError) => {
    console.error('[API] Response error:', error.message);
    return Promise.reject(error);
  }
);

/**
 * Document API Service
 */
export const documentApi = {
  /**
   * Get all documents
   */
  getAllDocuments: async (): Promise<ApiResponse<Document[]>> => {
    try {
      const response = await apiClient.get<ApiResponse<Document[]>>('/api/v1/documents');
      return response.data;
    } catch (error) {
      console.error('Error fetching documents:', error);
      throw error;
    }
  },

  /**
   * Get recent documents (last 10)
   */
  getRecentDocuments: async (): Promise<ApiResponse<Document[]>> => {
    try {
      const response = await apiClient.get<ApiResponse<Document[]>>('/api/v1/documents/recent');
      return response.data;
    } catch (error) {
      console.error('Error fetching recent documents:', error);
      throw error;
    }
  },

  /**
   * Get document by ID
   */
  getDocumentById: async (id: number): Promise<ApiResponse<Document>> => {
    try {
      const response = await apiClient.get<ApiResponse<Document>>(`/api/v1/documents/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching document ${id}:`, error);
      throw error;
    }
  },

  /**
   * Get documents by status
   */
  getDocumentsByStatus: async (status: DocumentStatus): Promise<ApiResponse<Document[]>> => {
    try {
      const response = await apiClient.get<ApiResponse<Document[]>>(
        `/api/v1/documents/status/${status}`
      );
      return response.data;
    } catch (error) {
      console.error(`Error fetching documents with status ${status}:`, error);
      throw error;
    }
  },

  /**
   * Upload and process a new document
   * This endpoint integrates with the AI service
   */
  uploadDocument: async (
    file: File,
    notes?: string
  ): Promise<ApiResponse<Document>> => {
    try {
      const formData = new FormData();
      formData.append('file', file);
      if (notes) {
        formData.append('notes', notes);
      }

      const response = await apiClient.post<ApiResponse<Document>>(
        '/api/v1/documents/upload',
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error('Error uploading document:', error);
      throw error;
    }
  },

  /**
   * Update document
   */
  updateDocument: async (
    id: number,
    updates: Partial<Document>
  ): Promise<ApiResponse<Document>> => {
    try {
      const response = await apiClient.put<ApiResponse<Document>>(
        `/api/v1/documents/${id}`,
        updates
      );
      return response.data;
    } catch (error) {
      console.error(`Error updating document ${id}:`, error);
      throw error;
    }
  },

  /**
   * Delete document
   */
  deleteDocument: async (id: number): Promise<ApiResponse<void>> => {
    try {
      const response = await apiClient.delete<ApiResponse<void>>(
        `/api/v1/documents/${id}`
      );
      return response.data;
    } catch (error) {
      console.error(`Error deleting document ${id}:`, error);
      throw error;
    }
  },

  /**
   * Check system health
   */
  checkSystemHealth: async (): Promise<ApiResponse<{
    backend: string;
    aiService: string;
  }>> => {
    try {
      const response = await apiClient.get<ApiResponse<{
        backend: string;
        aiService: string;
      }>>('/api/v1/documents/health/system');
      return response.data;
    } catch (error) {
      console.error('Error checking system health:', error);
      throw error;
    }
  },
};

export default apiClient;

