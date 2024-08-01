import React from "react";
import { Job } from "@/app/jobs/types";
import { JobItem } from "@/app/jobs/jobItem";
import { SkeletonList } from "../posts/components/PostSkeleton/skeletonList";
interface JobListProps {
  jobs: Job[];
  loading: boolean;
  onJobSelect: (job: Job) => void;
}

export const JobList: React.FC<JobListProps> = ({
  jobs,
  loading,
  onJobSelect,
}) => {
  if (loading) {
    return <SkeletonList count={6} />;
  }

  return (
    <div>
      {jobs.map((job) => (
        <JobItem job={job} key={job.id} onSelect={onJobSelect} />
      ))}
    </div>
  );
};

export default JobList;
