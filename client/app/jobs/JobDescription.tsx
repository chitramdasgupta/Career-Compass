import React from "react";
import { Job } from "@/app/jobs/types";
import { SkeletonList } from "../posts/components/PostSkeleton/skeletonList";
import Button from "@mui/material/Button";
import LaunchIcon from "@mui/icons-material/Launch";
import { Card, CardActions, CardContent, CardHeader } from "@mui/material";
import Link from "next/link";
import { BookmarkAddOutlined } from "@mui/icons-material";
import {
  useAddBookmarkMutation,
  useDeleteBookmarkMutation,
} from "@/app/shared/api/bookmarksApi";

interface JobDescriptionProps {
  job: Job | null;
}

export const JobDescription: React.FC<JobDescriptionProps> = ({ job }) => {
  const [addBookmark, { isLoading: isAddingBookmark }] =
    useAddBookmarkMutation();
  const [removeBookmark, { isLoading: isRemovingBookmark }] =
    useDeleteBookmarkMutation();

  if (job == null) {
    return <SkeletonList count={1} />;
  }

  const handleBookmarkToggle = async () => {
    try {
      if (job.bookmarked) {
        await removeBookmark(job.id).unwrap();
      } else {
        await addBookmark(job.id).unwrap();
      }
      // Update the cache here
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
          color={job.bookmarked ? "warning" : "secondary"}
          endIcon={<BookmarkAddOutlined />}
          onClick={handleBookmarkToggle}
          disabled={isAddingBookmark || isRemovingBookmark}
        >
          {job.bookmarked ? "Bookmarked" : "Bookmark"}
        </Button>
      </CardActions>
      <CardContent>
        <p>{job.description}</p>
      </CardContent>
    </Card>
  );
};
