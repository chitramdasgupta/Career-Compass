import React from "react";
import { Card, CardContent, Typography } from "@mui/material";
import Link from "next/link";
import { JobPosting } from "../types";

interface JobPostingCardProps {
  job: JobPosting;
}

const JobPostingCard: React.FC<JobPostingCardProps> = ({ job }) => {
  return (
    <Link
      href={`/job-postings/${job.id}`}
      passHref
      style={{ textDecoration: "none" }}
    >
      <Card sx={{ height: "100%", display: "flex", flexDirection: "column" }}>
        <CardContent>
          <Typography variant="h6" component="div">
            {job.title}
          </Typography>
          <Typography variant="body2" color="text.secondary">
            {job.company.name}
          </Typography>
          <Typography variant="body2">
            {job.country} - {job.jobLocation}
          </Typography>
        </CardContent>
      </Card>
    </Link>
  );
};

export default JobPostingCard;
