import { Roboto } from "next/font/google";
import "./globals.css";
import React from "react";
import Navbar from "./shared/components/Navbar";
import { Metadata } from "next";

const roboto = Roboto({
  weight: ["400", "700"],
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Career Compass",
  description: "Land your dream job with Career Compass!",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={roboto.className}>
        <Navbar />
        <main className="lg:container lg:mx-auto px-1">{children}</main>
      </body>
    </html>
  );
}
