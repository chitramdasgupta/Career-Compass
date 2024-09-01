"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { fetchJob } from "../../jobsApi";
import { fetchQuestions } from "./questionnaireApi";
import { Question } from "./types";
import { submitJobApplication } from "./jobApplicationApi";
import QuestionnaireForm from "./QuestionnaireForm";

const QuestionnairePage = () => {
  const [questions, setQuestions] = useState<Question[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const router = useRouter();

  const params = useParams();
  const jobId = params?.id as string;

  useEffect(() => {
    if (jobId) {
      fetchJob(jobId)
        .then((job) => fetchQuestions(job.questionnaire.id))
        .then((questions) => {
          console.log(questions);
          const sortedQuestions = questions;
          // const sortedQuestions = questions.sort(
          //   (a, b) => a.displayOrder - b.displayOrder,
          // );
          setQuestions(sortedQuestions);
          setLoading(false);
        })
        .catch(() => {
          setError("Failed to load questions");
          setLoading(false);
        });
    }
  }, [jobId]);

  const onSubmit = async (data: any) => {
    setSubmitError(null);

    try {
      await submitJobApplication(jobId, data.responses);
      router.push("/jobs");
    } catch {
      setSubmitError("Failed to submit form. Please try again.");
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  return (
    <QuestionnaireForm
      questions={questions}
      onSubmit={onSubmit}
      isSubmitting={false}
      submitError={submitError}
    />
  );
};

export default QuestionnairePage;
