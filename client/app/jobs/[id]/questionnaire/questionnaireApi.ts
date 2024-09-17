import { baseApi } from '@/app/shared/api/baseApi';
import { Question } from "./types";

const QUESTIONS_URL = "/questionnaires";

export const questionnaireApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getQuestions: builder.query<Question[], number>({
      query: (questionnaireId) => `${QUESTIONS_URL}/${questionnaireId}/questions`,
    }),
  }),
});

export const { useGetQuestionsQuery } = questionnaireApi;
