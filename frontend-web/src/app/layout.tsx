import type { Metadata } from 'next';
import './globals.css';

export const metadata: Metadata = {
  title: 'PBL4 - Document Scanning System',
  description: 'AI-powered document scanning and processing system',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}

