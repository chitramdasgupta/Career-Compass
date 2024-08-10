import React from "react";
import { Job } from "@/app/jobs/types";
import { SkeletonList } from "../posts/components/PostSkeleton/skeletonList";
import Button from "@mui/material/Button/";
import LaunchIcon from "@mui/icons-material/Launch";
import {
  Card,
  CardActions,
  CardContent,
  CardHeader,
  Paper,
} from "@mui/material";
import Link from "next/link";
import { BookmarkAdd, BookmarkAddOutlined } from "@mui/icons-material";

interface JobDescriptionProps {
  job: Job | null;
}

export const JobDescription: React.FC<JobDescriptionProps> = ({ job }) => {
  if (job == null) {
    return <SkeletonList count={1} />;
  }

  return (
    <Card className="sticky top-2">
      <CardHeader
        title={job.title}
        subheader={
          <Link href={`companies/${job.company.id}`}>{job.company.name}</Link>
        }
        sx={{ paddingBottom: "8px" }}
      />
      <CardActions sx={{ padding: "0 16px 16px 16px" }}>
        <Button size="small" variant="contained" endIcon={<LaunchIcon />}>
          Apply Now
        </Button>
        <Button
          size="small"
          variant="contained"
          color="secondary"
          endIcon={<BookmarkAddOutlined />}
        >
          Bookmark
        </Button>
      </CardActions>
      <CardContent>
        <p>{job.description}</p>
      </CardContent>
    </Card>
  );
};
