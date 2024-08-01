import React from "react";
import { Job } from "@/app/jobs/types";
import { SkeletonList } from "../posts/components/PostSkeleton/skeletonList";

interface JobDescriptionProps {
  job: Job | null;
}

export const JobDescription: React.FC<JobDescriptionProps> = ({ job }) => {
  if (job == null) {
    return <SkeletonList count={1} />;
  }

  return <div className="sticky top-2">{job.description}</div>;
};
