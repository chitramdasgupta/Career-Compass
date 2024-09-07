import { baseApi } from '@/app/shared/api/baseApi';
import { LoginResponse, LoginUserDto, RegisterUserDto } from "./types";

const AUTH_URL = "/auth"
const USERS_URL = "/users"

export const authApi = baseApi.injectEndpoints({
  endpoints: (builder) => ({
    registerUser: builder.mutation<void, RegisterUserDto>({
      query: (registerUserDto) => ({
        url: `${AUTH_URL}/signup`,
        method: 'POST',
        body: registerUserDto,
      }),
    }),

    loginUser: builder.mutation<LoginResponse, LoginUserDto>({
      query: (loginUserDto) => ({
        url: `${AUTH_URL}/login`,
        method: 'POST',
        body: loginUserDto,
      }),
    }),

    logoutUser: builder.mutation<void, void>({
      query: () => ({
        url: `${AUTH_URL}/logout`,
        method: 'POST',
      }),
    }),

    // Move this endpoint to users API
    getCurrentUser: builder.query<any, void>({
      query: () => ({
        url: `${USERS_URL}/me`,
        method: 'GET',
      }),
    }),
  }),
});

export const {
  useRegisterUserMutation,
  useLoginUserMutation,
  useLogoutUserMutation,
  useGetCurrentUserQuery,
} = authApi;
