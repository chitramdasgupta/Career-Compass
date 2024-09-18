"use client";

import React, { useState } from "react";
import { useParams, useRouter } from "next/navigation";
import {
  Typography,
  Box,
  Card,
  CardContent,
  Chip,
  Divider,
  List,
  ListItem,
  ListItemText,
  Button,
} from "@mui/material";
import { useGetJobQuery } from "@/app/jobs/jobsApi";
import { SkeletonList } from "@/app/posts/components/PostSkeleton/skeletonList";
import { usePostJobMutation } from "../jobPostingsApi";
import CustomSnackbar from "@/app/shared/components/CustomSnackbar";
import {
  Severity,
  SnackbarMessage,
} from "@/app/shared/components/CustomSnackbar/types";

export default function JobPostingShow() {
  const router = useRouter();
  const params = useParams();
  const jobId = Number(params.id);

  const [snackbarMessage, setSnackbarMessage] =
    useState<SnackbarMessage | null>(null);

  const {
    data: job,
    isLoading: isLoading,
    error: isError,
    refetch,
  } = useGetJobQuery(jobId);

  const [postJob, { isLoading: isPosting }] = usePostJobMutation();

  if (isLoading) return <SkeletonList count={1} />;
  if (isError) return <Typography>Error loading job posting</Typography>;
  if (!job) return <Typography>Job posting not found</Typography>;

  const handleCreateQuestionnaire = () => {
    router.push(`/job-postings/${jobId}/questionnaire/new`);
  };

  const handlePostJob = async () => {
    try {
      await postJob(jobId).unwrap();
      setSnackbarMessage({
        message: "Job is successfully posted!",
        severity: Severity.Success,
      });
      refetch();
    } catch (error) {
      console.error("Failed to post job:", error);
      setSnackbarMessage({
        message: "Failed to post job.",
        severity: Severity.Error,
      });
    }
  };

  return (
    <>
      <Box sx={{ maxWidth: 800, margin: "auto", mt: 4 }}>
        <Card>
          <CardContent>
            <Typography variant="h4" gutterBottom>
              {job.title}
            </Typography>
            <Typography variant="subtitle1" color="text.secondary" gutterBottom>
              {job.company.name} • {job.jobLocation} • {job.country}
            </Typography>
            <Box sx={{ display: "flex", alignItems: "center", gap: 2, mb: 2 }}>
              <Chip label={job.status} color="primary" />
              {job.status === "QUESTIONNAIRE_PENDING" && (
                <Button
                  variant="contained"
                  color="secondary"
                  onClick={handleCreateQuestionnaire}
                >
                  Create Questionnaire
                </Button>
              )}
              {job.status === "READY_TO_POST" && (
                <Button
                  variant="contained"
                  color="secondary"
                  onClick={handlePostJob}
                  disabled={isPosting}
                >
                  {isPosting ? "Posting..." : "Post Job"}
                </Button>
              )}
            </Box>

            <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
              Job Description
            </Typography>
            <Typography variant="body1" paragraph>
              {job.description}
            </Typography>

            {(job.minimumRequirement || job.desiredRequirement) && (
              <>
                <Typography variant="h6" gutterBottom>
                  Requirements
                </Typography>
                {job.minimumRequirement && (
                  <Typography variant="body1" paragraph>
                    <strong>Minimum:</strong> {job.minimumRequirement}
                  </Typography>
                )}
                {job.desiredRequirement && (
                  <Typography variant="body1" paragraph>
                    <strong>Desired:</strong> {job.desiredRequirement}
                  </Typography>
                )}
              </>
            )}

            {(job.minimumSalary || job.maximumSalary) && (
              <Typography variant="body1" paragraph>
                <strong>Salary Range:</strong>{" "}
                {job.minimumSalary && `${job.minimumSalary} `}
                {job.maximumSalary && `- ${job.maximumSalary} `}
                {job.currency}
              </Typography>
            )}

            <Divider sx={{ my: 2 }} />

            <Typography variant="h6" gutterBottom>
              Questionnaire
            </Typography>
            <List>
              {job.questionnaire
                ? job.questionnaire.questionnaireQuestions.map((qq) => (
                    <ListItem key={qq.id}>
                      <ListItemText
                        primary={qq.question.text}
                        secondary={`Type: ${qq.question.type}`}
                      />
                    </ListItem>
                  ))
                : ""}
            </List>
          </CardContent>
        </Card>

        <Box sx={{ mt: 4 }}>
          <Typography variant="h5" gutterBottom>
            Applicants
          </Typography>
          {/* TODO: Applicants section will be implemented later */}
        </Box>
      </Box>
      <CustomSnackbar snackbarMessage={snackbarMessage} />
    </>
  );
}
