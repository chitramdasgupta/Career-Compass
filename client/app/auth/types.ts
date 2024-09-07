import { Role } from "../shared/types/role";

export interface RegisterUserDto {
  email: string;
  password: string;
  role: Role;
  name: string | undefined;
}

  export interface LoginUserDto {
    email: string;
    password: string;
}

  export interface LoginResponse {
    email: string;
    role: Role;
}
