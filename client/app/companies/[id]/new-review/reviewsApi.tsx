import axiosInstance from "@/app/shared/utils/axios";

export async function submitReview(
  companyId: string,
  rating: number,
): Promise<void> {
  try {
    await axiosInstance.post(
      `http://localhost:8080/companies/${companyId}/reviews`,
      {
        rating,
      },
    );
  } catch (error) {
    console.error("Error submitting review:", error);
    throw error;
  }
}
