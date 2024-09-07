"use client";

import { Box, Button, TextField, Typography } from "@mui/material";
import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { LoginUserDto } from "../types";
import { useRouter } from "next/navigation";
import { useLoginUserMutation } from "../authApi";
import CustomSnackbar from "@/app/shared/components/CustomSnackbar";
import {
  Severity,
  SnackbarMessage,
} from "@/app/shared/components/CustomSnackbar/types";
import Link from "next/link";

const loginSchema = z.object({
  email: z.string().email(),
  password: z.string().min(6),
});

type LoginFormInputs = z.infer<typeof loginSchema>;

export default function Login() {
  const router = useRouter();
  const [loginUser, { isLoading }] = useLoginUserMutation();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormInputs>({ resolver: zodResolver(loginSchema) });

  const [snackbarMessage, setSnackbarMessage] =
    useState<SnackbarMessage | null>(null);

  const onSubmit = async (data: LoginFormInputs) => {
    const loginData: LoginUserDto = {
      email: data.email,
      password: data.password,
    };

    try {
      await loginUser(loginData).unwrap();

      router.push("/jobs");

      setSnackbarMessage({
        message: "Login successful!",
        severity: Severity.Success,
      });
    } catch (error) {
      setSnackbarMessage({
        message: "Login failed. Please check your credentials.",
        severity: Severity.Error,
      });
    }
  };

  return (
    <Box sx={{ maxWidth: 320, mx: "auto", mt: 5 }}>
      <Typography variant="h4" component="h1">
        Login
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
        <Button
          type="submit"
          variant="contained"
          color="primary"
          disabled={isLoading}
        >
          {isLoading ? "Logging in..." : "Login"}
        </Button>
      </form>
      <Box sx={{ mt: 2, textAlign: "center" }}>
        <Typography variant="body2">
          Don't have an account? <Link href="/auth/signup">Sign up here</Link>
        </Typography>
      </Box>
      <CustomSnackbar snackbarMessage={snackbarMessage} />
    </Box>
  );
}
