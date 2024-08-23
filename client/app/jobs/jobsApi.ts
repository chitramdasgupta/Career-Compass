import { PaginatedResponse } from "../shared/types/paginatedResponseType";
import { Job } from "@/app/jobs/types";
import axiosInstance from "../shared/utils/axios";

const JOBS_URL = "/jobs";

export async function fetchJobs(page: number, size: number = 25): Promise<PaginatedResponse<Job>> {
  try {
    const response = await axiosInstance.get<PaginatedResponse<Job>>(`${JOBS_URL}?page=${page}&size=${size}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching jobs:", error);
    throw error;
  }
}
