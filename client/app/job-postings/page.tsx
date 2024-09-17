"use client";

import React, { useState } from "react";
import { Virtuoso } from "react-virtuoso";
import { Button, Typography, Box } from "@mui/material";
import Link from "next/link";
import { useGetJobPostingsQuery } from "./jobPostingsApi";
import JobPostingCard from "./components/JobPostingCard";
import { SkeletonList } from "../posts/components/PostSkeleton/skeletonList";

export default function JobPostings() {
  const [page, setPage] = useState(0);
  const { data, isLoading, isFetching, isError } = useGetJobPostingsQuery(
    page,
    {
      refetchOnMountOrArgChange: true,
    },
  );

  if (isError)
    return <Typography>Error occurred while fetching job postings.</Typography>;

  const jobPostings = data?.content || [];
  const hasMore = !data?.last;

  const loadMore = () => {
    if (!isFetching && hasMore) {
      setPage((prevPage) => prevPage + 1);
    }
  };

  return (
    <Box sx={{ maxWidth: "xl", margin: "auto", p: 4 }}>
      <Typography variant="h4" gutterBottom>
        Job Postings
      </Typography>
      <Button
        component={Link}
        href="/job-postings/new"
        variant="contained"
        color="primary"
        sx={{ marginBottom: 2 }}
      >
        Create New Job Posting
      </Button>
      <Box sx={{ height: "calc(100vh - 200px)" }}>
        {isLoading && jobPostings.length === 0 ? (
          <SkeletonList count={3} />
        ) : (
          <Virtuoso
            data={jobPostings}
            endReached={loadMore}
            overscan={200}
            itemContent={(_, jobPosting) => (
              <Box sx={{ mb: 2 }}>
                <JobPostingCard job={jobPosting} key={jobPosting.id} />
              </Box>
            )}
          />
        )}
      </Box>
    </Box>
  );
}
