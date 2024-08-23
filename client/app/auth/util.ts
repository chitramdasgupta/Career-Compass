import { NextRequest } from "next/server";

export const isAuthenticated = (req: NextRequest) => {
  const token = req.cookies.get('token');
  return !!token;
};
