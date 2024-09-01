import axiosInstance from "@/app/shared/utils/axios";

export async function addBookmark(jobId: number): Promise<void> {
  try {
    await axiosInstance.post(`/bookmarks/${jobId}`);
  } catch (error) {
    console.error("Error adding bookmark:", error);
    throw error;
  }
}

export async function removeBookmark(jobId: number): Promise<void> {
  try {
    await axiosInstance.delete(`/bookmarks/${jobId}`);
  } catch (error) {
    console.error("Error removing bookmark:", error);
    throw error;
  }
}
