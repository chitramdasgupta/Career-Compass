import axios from "axios";
import { Company } from "./types";

const COMPANIES_URL = "http://localhost:8080/companies/";

export async function fetchCompanies(): Promise<Company[]> {
  try {
    const response = await axios.get<Company[]>(COMPANIES_URL);
    return response.data;
  } catch (error) {
    console.error("Error fetching companies:", error);
    throw error;
  }
}

export async function fetchCompany(id: string): Promise<Company> {
    try {
      const response = await axios.get<Company>(`${COMPANIES_URL}${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching company with ID ${id}:`, error);
      throw error;
    }
}