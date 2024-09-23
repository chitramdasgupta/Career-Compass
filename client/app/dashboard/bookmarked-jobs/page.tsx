"use client";

import React, { useState } from "react";
import { Virtuoso } from "react-virtuoso";
import { Container, Typography } from "@mui/material";
import {
  useGetBookmarkedJobsQuery,
  useDeleteBookmarkMutation,
} from "@/app/shared/api/bookmarksApi";
import { JobItem } from "@/app/jobs/jobItem";
import { JobDescription } from "@/app/jobs/JobDescription";
import { SkeletonList } from "@/app/posts/components/PostSkeleton/skeletonList";

export default function BookmarkedJobs() {
  const [page, setPage] = useState(0);
  const [selectedJob, setSelectedJob] = useState(null);
  const { data, isLoading, isFetching } = useGetBookmarkedJobsQuery(page);
  const [deleteBookmark] = useDeleteBookmarkMutation();

  const jobs = data?.content || [];
  const hasMore = !data?.last;

  const loadMore = () => {
    if (!isFetching && hasMore) {
      setPage((prevPage) => prevPage + 1);
    }
  };

  return (
    <Container maxWidth="lg" style={{ marginTop: "20px" }}>
      <Typography variant="h4" gutterBottom>
        Bookmarked Jobs
      </Typography>
      <div className="md:flex md:gap-4">
        <div className="md:w-1/2" style={{ height: "85vh" }}>
          {isLoading && jobs.length === 0 ? (
            <SkeletonList count={6} />
          ) : (
            <Virtuoso
              data={jobs}
              endReached={loadMore}
              itemContent={(_index, job) => (
                <JobItem job={job} onSelect={setSelectedJob} />
              )}
            />
          )}
        </div>
        <div className="md:w-1/2 md:sticky md:top-4 invisible md:visible">
          <JobDescription job={selectedJob} />
        </div>
      </div>
    </Container>
  );
}
