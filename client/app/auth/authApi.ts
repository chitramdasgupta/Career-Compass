import axiosInstance from "../shared/utils/axios";
import { LoginResponse, LoginUserDto, RegisterUserDto } from "./types";

const AUTH_URL = "/auth";

export async function registerUser(registerUserDto: RegisterUserDto): Promise<void> {
  try {
    await axiosInstance.post(`${AUTH_URL}/signup`, registerUserDto);
  } catch (error) {
    console.error("Error registering user:", error);
    throw error;
  }
}

export async function loginUser(loginUserDto: LoginUserDto): Promise<LoginResponse> {
  try {
    const response = await axiosInstance.post<LoginResponse>(`${AUTH_URL}/login`, loginUserDto);
    return response.data;
  } catch (error) {
    console.error("Error logging in user:", error);
    throw error;
  }
}
