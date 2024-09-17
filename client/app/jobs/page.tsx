"use client";

import React, { useState } from "react";
import { Virtuoso } from "react-virtuoso";
import { SkeletonList } from "../posts/components/PostSkeleton/skeletonList";
import { JobItem } from "./jobItem";
import { JobDescription } from "./JobDescription";
import { useGetJobsQuery } from "./jobsApi";

export default function Jobs() {
  const [page, setPage] = useState(0);
  const [selectedJob, setSelectedJob] = useState(null);

  const { data, isLoading, isFetching } = useGetJobsQuery(page, {
    refetchOnMountOrArgChange: true,
  });

  const jobs = data?.content || [];
  const hasMore = !data?.last;

  const loadMore = () => {
    if (!isFetching && hasMore) {
      setPage((prevPage) => prevPage + 1);
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Jobs</h1>
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
    </div>
  );
}
