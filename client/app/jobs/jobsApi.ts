import { PaginatedResponse } from "../shared/types/paginatedResponseType";
import { Job } from "@/app/jobs/types";
import axios from "axios";

const JOBS_URL = "http://localhost:8080/jobs";

export async function fetchJobs(page: number, size: number = 25): Promise<PaginatedResponse<Job>> {
  try {
    const response = await axios.get<PaginatedResponse<Job>>(`${JOBS_URL}?page=${page}&size=${size}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching jobs:", error);
    throw error;
  }
}
