import { useGetCurrentUserQuery } from "@/app/auth/authApi";
import { Role } from "../../types/role";

export function useAuth() {
  const { data: user, isLoading, isError } = useGetCurrentUserQuery();

  return {
    user,
    isLoading,
    isError,
    isAuthenticated: !!user,
    isCandidate: user?.role === Role.ROLE_CANDIDATE,
    isCompany: user?.role === Role.ROLE_COMPANY,
  };
}
