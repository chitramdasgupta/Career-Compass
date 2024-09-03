import axiosInstance from "../shared/utils/axios";
import { User } from "./types";

const USERS_URL = "http://localhost:8080/users";

export async function fetchCurrentUser(): Promise<User> {
  try {
    const response = await axiosInstance.get<User>(`${USERS_URL}/me`);
    return response.data;
  } catch (error) {
    console.error("Error fetching current user:", error);
    throw error;
  }
}