'use client';

import React, { useState, useEffect } from 'react';
import Sidebar from '@/components/Sidebar';
import Dashboard from '@/components/Dashboard';
import UploadButton from '@/components/UploadButton';
import { documentApi } from '@/services/api';

export default function Home() {
  const [refreshKey, setRefreshKey] = useState(0);
  const [systemHealth, setSystemHealth] = useState<{
    backend: string;
    aiService: string;
  } | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    checkSystemHealth();
  }, []);

  const checkSystemHealth = async () => {
    try {
      const response = await documentApi.checkSystemHealth();
      if (response.success && response.data) {
        setSystemHealth(response.data);
      }
    } catch (error) {
      console.error('Error checking system health:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleUploadSuccess = () => {
    // Refresh dashboard after successful upload
    setRefreshKey((key) => key + 1);
  };

  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar Navigation */}
      <Sidebar />

      {/* Main Content Area */}
      <main className="flex-1 overflow-auto">
        {/* Header */}
        <header className="bg-white shadow-sm border-b border-gray-200 sticky top-0 z-40">
          <div className="px-8 py-6">
            <div className="flex justify-between items-center">
              <div>
                <h1 className="text-3xl font-bold text-gray-900">
                  📊 PBL4 Document Scanning Dashboard
                </h1>
                <p className="text-gray-600 mt-1">
                  Manage and process your scanned documents with AI-powered extraction
                </p>
              </div>
              <UploadButton onSuccess={handleUploadSuccess} />
            </div>

            {/* System Health Status */}
            {!loading && systemHealth && (
              <div className="mt-4 flex gap-4 text-sm">
                <span
                  className={`px-3 py-1 rounded-full font-semibold ${
                    systemHealth.backend === 'healthy'
                      ? 'bg-green-100 text-green-800'
                      : 'bg-red-100 text-red-800'
                  }`}
                >
                  Backend: {systemHealth.backend}
                </span>
                <span
                  className={`px-3 py-1 rounded-full font-semibold ${
                    systemHealth.aiService === 'healthy'
                      ? 'bg-green-100 text-green-800'
                      : 'bg-red-100 text-red-800'
                  }`}
                >
                  AI Service: {systemHealth.aiService}
                </span>
              </div>
            )}
          </div>
        </header>

        {/* Dashboard Content */}
        <div className="p-8">
          <Dashboard key={refreshKey} onUploadSuccess={handleUploadSuccess} />

          {/* Quick Info Cards */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8">
            <div className="bg-white rounded-lg shadow p-6">
              <div className="text-4xl mb-2">📄</div>
              <h3 className="text-lg font-semibold text-gray-800">Documents Scanned</h3>
              <p className="text-gray-600 text-sm mt-1">View all uploaded documents</p>
            </div>

            <div className="bg-white rounded-lg shadow p-6">
              <div className="text-4xl mb-2">⚡</div>
              <h3 className="text-lg font-semibold text-gray-800">AI Processing</h3>
              <p className="text-gray-600 text-sm mt-1">Automatic text extraction & OCR</p>
            </div>

            <div className="bg-white rounded-lg shadow p-6">
              <div className="text-4xl mb-2">💾</div>
              <h3 className="text-lg font-semibold text-gray-800">Secure Storage</h3>
              <p className="text-gray-600 text-sm mt-1">All data safely stored in PostgreSQL</p>
            </div>
          </div>

          {/* Features Section */}
          <div className="bg-white rounded-lg shadow p-6 mt-8">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">🎯 Features</h2>
            <ul className="space-y-2 text-gray-700">
              <li>✅ Upload and scan documents in multiple formats (JPEG, PNG, etc.)</li>
              <li>✅ AI-powered image processing (deskew, denoise, enhance)</li>
              <li>✅ Automatic text extraction via OCR</li>
              <li>✅ Track document processing status in real-time</li>
              <li>✅ Download processed documents and extracted text</li>
              <li>✅ Organized dashboard with search and filter capabilities</li>
            </ul>
          </div>

          {/* Integration Info */}
          <div className="bg-blue-50 border border-blue-200 rounded-lg p-6 mt-8">
            <h3 className="text-lg font-semibold text-blue-900 mb-2">🔗 System Architecture</h3>
            <p className="text-blue-800 text-sm">
              This frontend connects to:
            </p>
            <ul className="text-sm text-blue-800 mt-2 space-y-1 ml-4">
              <li>
                • <strong>Backend API:</strong> http://localhost:8080 (Spring Boot)
              </li>
              <li>
                • <strong>AI Service:</strong> http://localhost:8000 (Python FastAPI)
              </li>
              <li>
                • <strong>Database:</strong> PostgreSQL (pbl4_db)
              </li>
            </ul>
          </div>
        </div>
      </main>
    </div>
  );
}

