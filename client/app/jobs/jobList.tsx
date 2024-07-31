import React from "react";
import { Job } from "@/app/jobs/types";
import { JobItem } from "@/app/jobs/jobItem";
import { SkeletonList } from "../posts/components/PostSkeleton/skeletonList";

interface JobListProps {
  jobs: Job[];
  loading: boolean;
}

export const JobList: React.FC<JobListProps> = ({ jobs, loading }) => {
  if (loading) {
    return <SkeletonList count={6} />;
  }

  return (
    <>
      {jobs.map((job) => (
        <JobItem job={job} key={job.id} />
      ))}
    </>
  );
};

export default JobList;
