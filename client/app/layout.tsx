import { Roboto } from "next/font/google";
import "./globals.css";
import React from "react";
import Navbar from "./shared/components/Navbar";
import { Metadata } from "next";
import AppNavbar from "./shared/components/AppNavbar";
import { Container } from "@mui/material";
import { Providers } from "./providers";

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
        <Providers>
          <AppNavbar />
          <Container maxWidth="xl">{children}</Container>
        </Providers>
      </body>
    </html>
  );
}
