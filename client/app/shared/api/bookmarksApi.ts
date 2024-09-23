import { baseApi } from "@/app/shared/api/baseApi";
import { Job } from "@/app/jobs/types";

const BOOKMARKS_URL = "bookmarks";

export const bookmarksApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getBookmarkedJobs: builder.query<
      {
        content: Job[];
        last: boolean;
      },
      number
    >({
      query: (page) => ({
        url: BOOKMARKS_URL,
        params: { page, size: 25 },
      }),
      serializeQueryArgs: ({ endpointName }) => endpointName,
      merge: (currentCache, newItems) => ({
        ...newItems,
        content: [...(currentCache?.content || []), ...newItems.content],
      }),
      forceRefetch: ({ currentArg, previousArg }) => currentArg !== previousArg,
    }),

    addBookmark: builder.mutation<void, number>({
      query: (jobId) => ({
        url: `${BOOKMARKS_URL}/${jobId}`,
        method: "POST",
      }),
    }),

    deleteBookmark: builder.mutation<void, number>({
      query: (jobId) => ({
        url: `${BOOKMARKS_URL}/${jobId}`,
        method: "DELETE",
      }),
    }),
  }),
});

export const {
  useGetBookmarkedJobsQuery,
  useDeleteBookmarkMutation,
  useAddBookmarkMutation
} = bookmarksApi;
