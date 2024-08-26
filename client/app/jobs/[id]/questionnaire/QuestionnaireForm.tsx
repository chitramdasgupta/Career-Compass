import React from "react";
import { useForm, SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { TextField, Button, CircularProgress, Box } from "@mui/material";
import { z } from "zod";
import { Question } from "./types";

interface FormData {
  responses: Record<string, string>;
}

const schema = z.object({
  responses: z.record(z.string().min(1, "This field is required")),
});

interface QuestionnaireFormProps {
  questions: Question[];
  onSubmit: SubmitHandler<FormData>;
  isSubmitting: boolean;
  submitError: string | null;
}

const QuestionnaireForm: React.FC<QuestionnaireFormProps> = ({
  questions,
  onSubmit,
  isSubmitting,
  submitError,
}) => {
  const {
    handleSubmit,
    register,
    formState: { errors },
  } = useForm<FormData>({
    resolver: zodResolver(schema),
    defaultValues: {
      responses: {},
    },
  });

  return (
    <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
      <h1>Job Application</h1>
      {questions.map((question) => (
        <TextField
          key={question.id}
          {...register(`responses.${question.id}` as const)}
          fullWidth
          label={question.text}
          error={!!errors.responses?.[question.id]}
          helperText={errors.responses?.[question.id]?.message}
          margin="normal"
        />
      ))}

      {submitError && <div style={{ color: "red" }}>{submitError}</div>}

      <Button
        type="submit"
        variant="contained"
        color="primary"
        disabled={isSubmitting}
        startIcon={isSubmitting ? <CircularProgress size={16} /> : null}
      >
        Submit
      </Button>
    </Box>
  );
};

export default QuestionnaireForm;
