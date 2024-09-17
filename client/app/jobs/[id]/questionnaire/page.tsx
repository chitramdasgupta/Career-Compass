"use client";

import { useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { useGetJobQuery } from "../../jobsApi";
import { useGetQuestionsQuery } from "./questionnaireApi";
import QuestionnaireForm from "./QuestionnaireForm";
import { submitJobApplication } from "./jobApplicationApi";

const QuestionnairePage = () => {
  const [submitError, setSubmitError] = useState<string | null>(null);
  const router = useRouter();
  const params = useParams();
  const jobId = params?.id as string;

  const {
    data: job,
    isLoading: isJobLoading,
    error: jobError,
  } = useGetJobQuery(jobId);
  const {
    data: questions,
    isLoading: isQuestionsLoading,
    error: questionsError,
  } = useGetQuestionsQuery(job?.questionnaire.id, { skip: !job });

  const isLoading = isJobLoading || isQuestionsLoading;
  const error = jobError || questionsError;

  const onSubmit = async (data: any) => {
    setSubmitError(null);
    try {
      await submitJobApplication(jobId, data.responses);
      router.push("/jobs");
    } catch {
      setSubmitError("Failed to submit form. Please try again.");
    }
  };

  if (isLoading) return <div>Loading...</div>;
  if (error) return <div>Error: {error.toString()}</div>;

  return (
    <QuestionnaireForm
      questions={questions || []}
      onSubmit={onSubmit}
      isSubmitting={false}
      submitError={submitError}
    />
  );
};

export default QuestionnairePage;
