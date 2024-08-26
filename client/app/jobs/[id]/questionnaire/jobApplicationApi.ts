import axiosInstance from "@/app/shared/utils/axios";

export async function submitJobApplication(jobId: string, responses: Record<string, string>): Promise<void> {
  try {
    await axiosInstance.post("http://localhost:8080/job-applications/create", {
      jobId,
      responses,
    });
  } catch (error) {
    console.error("Error submitting job application:", error);
    throw error;
  }
}