"use client";

import {
  Box,
  Button,
  TextField,
  Typography,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  FormHelperText,
} from "@mui/material";
import React, { useState } from "react";
import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { RegisterUserDto } from "../types";
import { useRouter } from "next/navigation";
import { useRegisterUserMutation } from "../authApi";
import CustomSnackbar from "@/app/shared/components/CustomSnackbar";
import {
  Severity,
  SnackbarMessage,
} from "@/app/shared/components/CustomSnackbar/types";
import { Role } from "@/app/shared/types/role";
import Link from "next/link";

const signupSchema = z
  .object({
    email: z.string().email(),
    password: z.string().min(6),
    confirmPassword: z.string().min(6),
    role: z.enum([Role.ROLE_CANDIDATE, Role.ROLE_COMPANY]),
    companyName: z.string().optional(),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ["confirmPassword"],
  })
  .refine(
    (data) => {
      if (data.role === Role.ROLE_COMPANY && !data.companyName) {
        return false;
      }
      return true;
    },
    {
      message: "Company name is required for company accounts",
      path: ["companyName"],
    },
  );

type SignupFormInputs = z.infer<typeof signupSchema>;

export default function Signup() {
  const router = useRouter();
  const [registerUser, { isLoading }] = useRegisterUserMutation();
  const {
    register,
    handleSubmit,
    control,
    watch,
    formState: { errors },
  } = useForm<SignupFormInputs>({ resolver: zodResolver(signupSchema) });

  const [snackbarMessage, setSnackbarMessage] =
    useState<SnackbarMessage | null>(null);

  const selectedRole = watch("role");

  const onSubmit = async (data: SignupFormInputs) => {
    const signupData: RegisterUserDto = {
      email: data.email,
      password: data.password,
      role: data.role,
      name: data.role === Role.ROLE_COMPANY ? data.companyName : undefined,
    };

    try {
      await registerUser(signupData).unwrap();
      router.push("/auth/login");
      setSnackbarMessage({
        message: "Signup successful! Please log in.",
        severity: Severity.Success,
      });
    } catch (error) {
      setSnackbarMessage({
        message: "Signup failed. Please try again.",
        severity: Severity.Error,
      });
    }
  };

  return (
    <Box sx={{ maxWidth: 320, mx: "auto", mt: 5 }}>
      <Typography variant="h4" component="h1">
        Sign Up
      </Typography>
      <form onSubmit={handleSubmit(onSubmit)}>
        <TextField
          label="Email"
          fullWidth
          margin="normal"
          {...register("email")}
          error={!!errors.email}
          helperText={errors.email?.message}
        />
        <TextField
          label="Password"
          type="password"
          fullWidth
          margin="normal"
          {...register("password")}
          error={!!errors.password}
          helperText={errors.password?.message}
        />
        <TextField
          label="Confirm Password"
          type="password"
          fullWidth
          margin="normal"
          {...register("confirmPassword")}
          error={!!errors.confirmPassword}
          helperText={errors.confirmPassword?.message}
        />
        <FormControl fullWidth margin="normal" error={!!errors.role}>
          <InputLabel id="role-select-label">Role</InputLabel>
          <Controller
            name="role"
            control={control}
            defaultValue={Role.ROLE_CANDIDATE}
            render={({ field }) => (
              <Select labelId="role-select-label" label="Role" {...field}>
                <MenuItem value={Role.ROLE_CANDIDATE}>Candidate</MenuItem>
                <MenuItem value={Role.ROLE_COMPANY}>Company</MenuItem>
              </Select>
            )}
          />
          <FormHelperText>{errors.role?.message}</FormHelperText>
        </FormControl>
        {selectedRole === Role.ROLE_COMPANY && (
          <TextField
            label="Company Name"
            fullWidth
            margin="normal"
            {...register("companyName")}
            error={!!errors.companyName}
            helperText={errors.companyName?.message}
          />
        )}
        <Button
          type="submit"
          variant="contained"
          color="primary"
          disabled={isLoading}
          fullWidth
          sx={{ mt: 2 }}
        >
          {isLoading ? "Signing up..." : "Sign Up"}
        </Button>
      </form>
      <Box sx={{ mt: 2, textAlign: "center" }}>
        <Typography variant="body2">
          Already have an account? <Link href="/auth/login">Log in here</Link>
        </Typography>
      </Box>
      <CustomSnackbar snackbarMessage={snackbarMessage} />
    </Box>
  );
}
