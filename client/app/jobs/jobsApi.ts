import { baseApi } from '@/app/shared/api/baseApi';
import { Job } from "@/app/jobs/types";

const JOBS_URL = "jobs";

export const jobsApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getJobs: builder.query<{
      content: Job[];
      last: boolean;
    }, number>({
      query: (page) => ({
        url: JOBS_URL,
        params: { page, size: 25 },
      }),
      serializeQueryArgs: ({ endpointName }) => endpointName,
      merge: (currentCache, newItems) => ({
        ...newItems,
        content: [...(currentCache?.content || []), ...newItems.content],
      }),
      forceRefetch: ({ currentArg, previousArg }) => currentArg !== previousArg,
    }),
    getJob: builder.query<Job, string>({
      query: (id) => `${JOBS_URL}/${id}`,
    }),
  }),
});

export const { useGetJobsQuery, useGetJobQuery } = jobsApi;
