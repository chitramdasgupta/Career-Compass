import { Job } from "@/app/jobs/types";
import React from "react";
import { Card, CardContent, CardHeader } from "@mui/material";

interface JobItemProps {
  job: Job;
}

export const JobItem: React.FC<JobItemProps> = ({ job }) => {
  return (
    <Card className="my-4">
      <CardHeader title={job.title} subheader={job.company.name} />
      <CardContent>
        <p>{job.description}</p>
      </CardContent>
    </Card>
  );
};
