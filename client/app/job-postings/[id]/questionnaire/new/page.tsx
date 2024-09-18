"use client";

import React, { useState } from "react";
import { useForm, useFieldArray, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  TextField,
  Button,
  Box,
  Typography,
  Select,
  MenuItem,
  IconButton,
} from "@mui/material";
import { z } from "zod";
import { useParams, useRouter } from "next/navigation";
import CustomSnackbar from "@/app/shared/components/CustomSnackbar";
import {
  Severity,
  SnackbarMessage,
} from "@/app/shared/components/CustomSnackbar/types";
import DeleteIcon from "@mui/icons-material/Delete";
import { useCreateQuestionnaireMutation } from "@/app/jobs/jobsApi";

const questionSchema = z.object({
  text: z.string().min(1, "Question text is required"),
  type: z.enum(["TEXT", "MULTI_SELECT", "SINGLE_SELECT"]),
});

const questionnaireSchema = z.object({
  description: z.string().min(1, "Description is required"),
  questionnaireQuestions: z
    .array(
      z.object({
        question: questionSchema,
        displayOrder: z.number().int().positive(),
      }),
    )
    .min(1, "At least one question is required"),
});

type QuestionnaireInput = z.infer<typeof questionnaireSchema>;

export default function NewQuestionnaire() {
  const params = useParams();
  const jobId = Number(params.id);

  console.log(jobId);

  const router = useRouter();
  const [snackbarMessage, setSnackbarMessage] =
    useState<SnackbarMessage | null>(null);
  const [createQuestionnaire, { isLoading }] = useCreateQuestionnaireMutation();

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<QuestionnaireInput>({
    resolver: zodResolver(questionnaireSchema),
    defaultValues: {
      description: "",
      questionnaireQuestions: [
        { question: { text: "", type: "TEXT" }, displayOrder: 1 },
      ],
    },
  });

  const { fields, append, remove } = useFieldArray({
    control,
    name: "questionnaireQuestions",
  });

  const onSubmit = async (data: QuestionnaireInput) => {
    try {
      const result = await createQuestionnaire({
        jobId: jobId,
        questionnaire: data,
      }).unwrap();
      router.push(`/job-postings/${result.id}`);
      setSnackbarMessage({
        message: "Questionnaire successfully created!",
        severity: Severity.Success,
      });
    } catch (err) {
      setSnackbarMessage({
        message: "Questionnaire could not be created.",
        severity: Severity.Error,
      });
    }
  };

  return (
    <>
      <Box sx={{ maxWidth: 800, margin: "auto", mt: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Create Questionnaire for Job {jobId}
        </Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Controller
            name="description"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label="Questionnaire Description"
                fullWidth
                margin="normal"
                error={!!errors.description}
                helperText={errors.description?.message}
              />
            )}
          />
          {fields.map((field, index) => (
            <Box
              key={field.id}
              sx={{ display: "flex", gap: 2, alignItems: "flex-start", mb: 2 }}
            >
              <Controller
                name={`questionnaireQuestions.${index}.question.text`}
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label="Question"
                    fullWidth
                    margin="normal"
                    error={
                      !!errors.questionnaireQuestions?.[index]?.question?.text
                    }
                    helperText={
                      errors.questionnaireQuestions?.[index]?.question?.text
                        ?.message
                    }
                  />
                )}
              />
              <Controller
                name={`questionnaireQuestions.${index}.question.type`}
                control={control}
                render={({ field }) => (
                  <Select {...field} label="Type" sx={{ minWidth: 120, mt: 2 }}>
                    <MenuItem value="TEXT">Text</MenuItem>
                    <MenuItem value="MULTI_SELECT">Multi Select</MenuItem>
                    <MenuItem value="SINGLE_SELECT">Single Select</MenuItem>
                  </Select>
                )}
              />
              <IconButton onClick={() => remove(index)} sx={{ mt: 3 }}>
                <DeleteIcon />
              </IconButton>
            </Box>
          ))}
          <Button
            type="button"
            onClick={() =>
              append({
                question: { text: "", type: "TEXT" },
                displayOrder: fields.length + 1,
              })
            }
            variant="outlined"
            sx={{ mt: 2, mb: 2 }}
          >
            Add Question
          </Button>
          <Button
            type="submit"
            variant="contained"
            color="primary"
            fullWidth
            sx={{ mt: 2 }}
            disabled={isLoading}
          >
            {isLoading ? "Creating..." : "Create Questionnaire"}
          </Button>
        </form>
      </Box>
      <CustomSnackbar snackbarMessage={snackbarMessage} />
    </>
  );
}
