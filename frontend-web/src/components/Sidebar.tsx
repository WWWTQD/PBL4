'use client';

/**
 * Sidebar Component
 * Navigation sidebar for the application
 */

import React from 'react';
import Link from 'next/link';

interface NavItem {
  label: string;
  href: string;
  icon: string;
}

const navItems: NavItem[] = [
  { label: 'Dashboard', href: '/', icon: '📊' },
  { label: 'Scans', href: '/scans', icon: '📄' },
  { label: 'Upload', href: '/upload', icon: '📤' },
  { label: 'Settings', href: '/settings', icon: '⚙️' },
];

export default function Sidebar() {
  return (
    <aside className="w-64 bg-slate-900 text-white min-h-screen p-6">
      {/* Logo/Title */}
      <div className="mb-12">
        <h1 className="text-2xl font-bold text-blue-400">PBL4</h1>
        <p className="text-sm text-gray-400 mt-1">Document Scan System</p>
      </div>

      {/* Navigation Menu */}
      <nav className="space-y-2">
        {navItems.map((item) => (
          <Link
            key={item.href}
            href={item.href}
            className="flex items-center px-4 py-3 rounded-lg text-gray-300 hover:bg-slate-800 hover:text-white transition-colors"
          >
            <span className="mr-3 text-lg">{item.icon}</span>
            <span>{item.label}</span>
          </Link>
        ))}
      </nav>

      {/* Footer */}
      <div className="mt-12 pt-6 border-t border-gray-700">
        <p className="text-xs text-gray-500">v1.0.0</p>
      </div>
    </aside>
  );
}

