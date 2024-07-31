import { Job } from "@/app/jobs/types";
import axios from "axios";

const JOBS_URL = "http://localhost:8080/jobs/";

export async function fetchJobs(): Promise<Job[]> {
  try {
    const response = await axios.get<Job[]>(JOBS_URL);
    return response.data;
  } catch (error) {
    console.error("Error fetching jobs:", error);
    throw error;
  }
}
