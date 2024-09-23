import { baseApi } from "@/app/shared/api/baseApi";
import { Job } from "@/app/jobs/types";

const JOB_APPLICATIONS_URL = "job-applications";

export const jobApplicationsApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getSubmittedJobApplications: builder.query<
      {
        content: Job[];
        last: boolean;
      },
      number
    >({
      query: (page) => ({
        url: JOB_APPLICATIONS_URL,
        params: { page, size: 25 },
      }),
      serializeQueryArgs: ({ endpointName }) => endpointName,
      merge: (currentCache, newItems) => ({
        ...newItems,
        content: [...(currentCache?.content || []), ...newItems.content],
      }),
      forceRefetch: ({ currentArg, previousArg }) => currentArg !== previousArg,
    }),
  }),
});

export const { useGetSubmittedJobApplicationsQuery } = jobApplicationsApi;
