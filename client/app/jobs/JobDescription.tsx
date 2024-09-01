import React, { useEffect, useState } from "react";
import { Job } from "@/app/jobs/types";
import { SkeletonList } from "../posts/components/PostSkeleton/skeletonList";
import Button from "@mui/material/Button";
import LaunchIcon from "@mui/icons-material/Launch";
import { Card, CardActions, CardContent, CardHeader } from "@mui/material";
import Link from "next/link";
import { BookmarkAddOutlined } from "@mui/icons-material";
import { addBookmark, removeBookmark } from "./bookmarkApi";

interface JobDescriptionProps {
  job: Job | null;
}

export const JobDescription: React.FC<JobDescriptionProps> = ({ job }) => {
  const [isBookmarked, setIsBookmarked] = useState(job?.bookmarked || false);

  useEffect(() => {
    if (job) {
      setIsBookmarked(job.bookmarked);
    }
  }, [job]);

  if (job == null) {
    return <SkeletonList count={1} />;
  }

  const handleBookmarkToggle = async () => {
    const jobId = job.id;

    try {
      if (isBookmarked) {
        await removeBookmark(jobId);
      } else {
        await addBookmark(jobId);
      }

      setIsBookmarked(!isBookmarked);
    } catch (error) {
      console.error("Error toggling bookmark:", error);
    }
  };

  return (
    <Card className="sticky top-2">
      <CardHeader
        title={job.title}
        subheader={
          <Link href={`companies/${job.company.id}`}>{job.company.name}</Link>
        }
        sx={{
          paddingBottom: "8px",
          "& .MuiCardHeader-subheader:hover": {
            textDecoration: "underline",
          },
        }}
      />
      <CardActions sx={{ padding: "0 16px 16px 16px" }}>
        <Link href={`/jobs/${job.id}/questionnaire`} passHref>
          <Button
            size="small"
            variant="contained"
            endIcon={<LaunchIcon />}
            component="a"
          >
            Apply Now
          </Button>
        </Link>
        <Button
          size="small"
          variant="contained"
          color={isBookmarked ? "warning" : "secondary"}
          endIcon={<BookmarkAddOutlined />}
          onClick={handleBookmarkToggle}
        >
          {isBookmarked ? "Bookmarked" : "Bookmark"}
        </Button>
      </CardActions>
      <CardContent>
        <p>{job.description}</p>
      </CardContent>
    </Card>
  );
};
