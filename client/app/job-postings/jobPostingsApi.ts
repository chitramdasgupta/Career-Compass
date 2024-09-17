import { baseApi } from "@/app/shared/api/baseApi";
import { JobPostingsResponse } from "./types";

const USERS_URL = "/users";

export const jobPostingsApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getJobPostings: builder.query<JobPostingsResponse, number>({
      query: (page = 0) => ({
        url: `${USERS_URL}/me/jobs`,
        method: "GET",
        params: { page, size: 25 },
      }),
    }),
  }),
});

export const { useGetJobPostingsQuery } = jobPostingsApi;
