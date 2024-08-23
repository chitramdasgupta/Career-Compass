import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import { isAuthenticated } from "./app/auth/util";

const publicRoutes = ["/auth/login", "/auth/signup"];

export function middleware(req: NextRequest) {
  if (!isAuthenticated(req)) {
    if (!publicRoutes.includes(req.nextUrl.pathname)) {
      const loginUrl = new URL("/auth/login", req.url);
      return NextResponse.redirect(loginUrl);
    }
  } else {
    if (publicRoutes.includes(req.nextUrl.pathname)) {
      const homeUrl = new URL("/", req.url);
      return NextResponse.redirect(homeUrl);
    }
  }

  return NextResponse.next();
}

export const config = {
  matcher: ['/((?!api|_next/static|_next/image|favicon.ico).*)'],
}
