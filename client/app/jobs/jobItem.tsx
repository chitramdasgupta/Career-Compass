import { Job } from "@/app/jobs/types";
import React from "react";
import { Card, CardContent, CardHeader } from "@mui/material";

interface JobItemProps {
  job: Job;
  onSelect: (job: Job) => void;
}

export const JobItem: React.FC<JobItemProps> = ({ job, onSelect }) => {
  return (
    <Card
      className="mb-4 hover:cursor-pointer"
      onClick={() => onSelect(job)}
      sx={{
        "&:hover .MuiCardHeader-title": {
          textDecoration: "underline",
        },
      }}
    >
      <CardHeader title={job.title} subheader={job.company.name} />
      <CardContent>
        <p>{job.description}</p>
      </CardContent>
    </Card>
  );
};
