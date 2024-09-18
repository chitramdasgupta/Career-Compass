import { baseApi } from "@/app/shared/api/baseApi";
import { JobPostingsResponse, Job } from "./types";

const USERS_URL = "/users";
const JOBS_URL = "/jobs";

export const jobPostingsApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getJobPostings: builder.query<JobPostingsResponse, number>({
      query: (page = 0) => ({
        url: `${USERS_URL}/me/jobs`,
        method: "GET",
        params: { page, size: 25 },
      }),
    }),
    postJob: builder.mutation<Job, number>({
      query: (jobId) => ({
        url: `${JOBS_URL}/${jobId}/post`,
        method: "POST",
      }),
    }),
  }),
});

export const { useGetJobPostingsQuery, usePostJobMutation } = jobPostingsApi;
