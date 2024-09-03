import { Company } from "./types";
import axiosInstance from "../shared/utils/axios";

const COMPANIES_URL = "http://localhost:8080/companies";

export async function fetchCompanies(page: number = 1, size: number = 25): Promise<Company[]> {
  try {
    const response = await axiosInstance.get<Company[]>(`${COMPANIES_URL}?page=${page}&size=${size}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching companies:", error);
    throw error;
  }
}

export async function fetchCompany(id: string): Promise<Company> {
    try {
      const response = await axiosInstance.get<Company>(`${COMPANIES_URL}/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching company with ID ${id}:`, error);
      throw error;
    }
}