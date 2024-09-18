"use client";

import React, { useState } from "react";
import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  TextField,
  Button,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Box,
  Typography,
} from "@mui/material";
import { z } from "zod";
import { useCreateJobMutation } from "@/app/jobs/jobsApi";
import { useRouter } from "next/navigation";
import CustomSnackbar from "@/app/shared/components/CustomSnackbar";
import {
  Severity,
  SnackbarMessage,
} from "@/app/shared/components/CustomSnackbar/types";

const createJobSchema = z.object({
  title: z.string().min(1, "Title is required"),
  description: z.string().min(1, "Description is required"),
  country: z.string().length(2, "Country code must be 2 characters"),
  jobLocation: z.enum(["OFFICE", "REMOTE", "HYBRID"]),
});

type CreateJobInput = z.infer<typeof createJobSchema>;

export default function NewJobPosting() {
  const router = useRouter();
  const [snackbarMessage, setSnackbarMessage] =
    useState<SnackbarMessage | null>(null);
  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<CreateJobInput>({
    resolver: zodResolver(createJobSchema),
    defaultValues: {
      title: "",
      description: "",
      country: "",
      jobLocation: "OFFICE",
    },
  });

  const [createJob, { isLoading, isError, error }] = useCreateJobMutation();

  const onSubmit = async (data: CreateJobInput) => {
    try {
      const result = await createJob(data).unwrap();
      router.push(`/job-postings/${result.id}/questionnaire/new`);
      setSnackbarMessage({
        message: "Job successfully created! Please create a questionnaire.",
        severity: Severity.Success,
      });
    } catch (err) {
      setSnackbarMessage({
        message: "Job could not be created.",
        severity: Severity.Error,
      });
    }
  };

  return (
    <>
      <Box sx={{ maxWidth: 600, margin: "auto", mt: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          New Job Posting
        </Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Controller
            name="title"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label="Job Title"
                fullWidth
                margin="normal"
                error={!!errors.title}
                helperText={errors.title?.message}
              />
            )}
          />
          <Controller
            name="description"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label="Job Description"
                fullWidth
                multiline
                rows={4}
                margin="normal"
                error={!!errors.description}
                helperText={errors.description?.message}
              />
            )}
          />
          <Controller
            name="country"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label="Country Code"
                fullWidth
                margin="normal"
                error={!!errors.country}
                helperText={errors.country?.message}
              />
            )}
          />
          <FormControl fullWidth margin="normal">
            <InputLabel id="job-location-label">Job Location</InputLabel>
            <Controller
              name="jobLocation"
              control={control}
              render={({ field }) => (
                <Select
                  {...field}
                  labelId="job-location-label"
                  label="Job Location"
                  error={!!errors.jobLocation}
                >
                  <MenuItem value="OFFICE">Office</MenuItem>
                  <MenuItem value="REMOTE">Remote</MenuItem>
                  <MenuItem value="HYBRID">Hybrid</MenuItem>
                </Select>
              )}
            />
          </FormControl>
          <Button
            type="submit"
            variant="contained"
            color="primary"
            fullWidth
            sx={{ mt: 2 }}
            disabled={isLoading}
          >
            {isLoading ? "Creating..." : "Create Job"}
          </Button>
        </form>
        {isError && (
          <Typography color="error" sx={{ mt: 2 }}>
            Error: {(error as any)?.data?.message || "Failed to create job"}
          </Typography>
        )}
      </Box>
      <CustomSnackbar snackbarMessage={snackbarMessage} />
    </>
  );
}
