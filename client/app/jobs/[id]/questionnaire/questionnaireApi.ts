import axiosInstance from "@/app/shared/utils/axios";
import { Question } from "./types";

const QUESTIONS_URL = "/questionnaires";

export async function fetchQuestions(questionnaireId: number): Promise<Question[]> {
  try {
    const response = await axiosInstance.get<Question[]>(`${QUESTIONS_URL}/${questionnaireId}/questions`);
    return response.data;
  } catch (error) {
    console.error("Error fetching questions:", error);
    throw error;
  }
}
