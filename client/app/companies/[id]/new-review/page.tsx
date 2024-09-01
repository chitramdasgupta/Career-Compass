"use client";

import React, { useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { Controller, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Box,
  Button,
  Card,
  CardContent,
  Rating,
  TextField,
  Typography,
  Snackbar,
  Alert,
} from "@mui/material";
import { z } from "zod";
import { submitReview } from "./reviewsApi";

const schema = z.object({
  rating: z.number().min(1).max(5),
  review: z.string().max(1000).optional(),
});

type FormData = z.infer<typeof schema>;

export default function NewReview() {
  const params = useParams();
  const router = useRouter();
  const id = params.id as string;
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showSuccess, setShowSuccess] = useState(false);

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<FormData>({
    resolver: zodResolver(schema),
    defaultValues: {
      rating: 0,
      review: "",
    },
  });

  const onSubmitForm = async (data: FormData) => {
    setIsSubmitting(true);
    setSubmitError(null);

    try {
      await submitReview(id, data.rating);
      setShowSuccess(true);
      setTimeout(() => {
        router.push(`/companies/${id}`);
      }, 2000);
    } catch {
      setSubmitError("Failed to submit review. Please try again.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="container mx-auto p-4">
      <Card>
        <CardContent>
          <Typography variant="h5" component="h2" gutterBottom>
            Write a New Review
          </Typography>
          <form onSubmit={handleSubmit(onSubmitForm)}>
            <Box mb={2}>
              <Typography component="legend">Rating</Typography>
              <Controller
                name="rating"
                control={control}
                rules={{ required: true }}
                render={({ field }) => (
                  <Rating
                    {...field}
                    precision={0.5}
                    onChange={(_, value) => {
                      field.onChange(value);
                    }}
                  />
                )}
              />
              {errors.rating && (
                <Typography color="error" variant="caption">
                  Rating is required
                </Typography>
              )}
            </Box>

            <Box mb={2}>
              <Controller
                name="review"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    multiline
                    rows={4}
                    fullWidth
                    label="Review (optional)"
                    error={!!errors.review}
                    helperText={errors.review?.message}
                  />
                )}
              />
            </Box>

            <Button
              type="submit"
              variant="contained"
              color="primary"
              disabled={isSubmitting}
            >
              {isSubmitting ? "Submitting..." : "Submit Review"}
            </Button>
          </form>
        </CardContent>
      </Card>

      <Snackbar
        open={submitError !== null}
        autoHideDuration={6000}
        onClose={() => setSubmitError(null)}
      >
        <Alert
          onClose={() => setSubmitError(null)}
          severity="error"
          sx={{ width: "100%" }}
        >
          {submitError}
        </Alert>
      </Snackbar>

      <Snackbar
        open={showSuccess}
        autoHideDuration={2000}
        onClose={() => setShowSuccess(false)}
      >
        <Alert
          onClose={() => setShowSuccess(false)}
          severity="success"
          sx={{ width: "100%" }}
        >
          Review submitted successfully!
        </Alert>
      </Snackbar>
    </div>
  );
}
