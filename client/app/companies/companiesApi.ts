import { baseApi } from '@/app/shared/api/baseApi';
import { Company } from './types';

const COMPANIES_URL = "companies"

export const companiesApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    getCompanies: builder.query<{
      content: Company[];
      last: boolean;
    }, number>({
      query: (page) => ({
        url: COMPANIES_URL,
        params: { page, size: 25 },
      }),
      serializeQueryArgs: ({ endpointName }) => endpointName,
      merge: (currentCache, newItems) => ({
        ...newItems,
        content: [...(currentCache?.content || []), ...newItems.content],
      }),
      forceRefetch: ({ currentArg, previousArg }) => currentArg !== previousArg,
    }),
    getCompany: builder.query<Company, string>({
      query: (id) => `${COMPANIES_URL}/${id}`,
    }),
  }),
});

export const { useGetCompaniesQuery, useGetCompanyQuery } = companiesApi;
