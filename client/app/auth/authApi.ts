import axiosInstance from "../shared/utils/axios";
import Cookies from 'js-cookie';
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
    Cookies.set('token', response.data.token, { expires: 7, path: '/' });
    return response.data;
  } catch (error) {
    console.error("Error logging in user:", error);
    throw error;
  }
}
