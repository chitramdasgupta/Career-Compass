"use client";

import React, { useCallback, useEffect, useState } from "react";
import { Job } from "@/app/jobs/types";
import { fetchJobs } from "@/app/jobs/jobsApi";
import { JobDescription } from "./JobDescription";
import { PaginatedResponse } from "../shared/types/paginatedResponseType";
import { Virtuoso } from "react-virtuoso";
import { JobItem } from "./jobItem";
import { SkeletonList } from "../posts/components/PostSkeleton/skeletonList";
import { useRouter } from "next/navigation";

export default function Jobs() {
  const router = useRouter();
  const [jobs, setJobs] = useState<Job[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedJob, setSelectedJob] = useState<Job | null>(null);
  const [pageNumber, setPageNumber] = useState(0);
  const [hasMore, setHasMore] = useState(true);

  const loadMore = useCallback(() => {
    if (!hasMore) return;

    fetchJobs(pageNumber)
      .then((response: PaginatedResponse<Job>) => {
        console.log({ response });
        setLoading(false);
        setJobs((jobs) => [...jobs, ...response.content]);
        if (selectedJob == null) setSelectedJob(response.content[0]);

        if (response.last) {
          setHasMore(false);
        } else {
          setPageNumber((prevPageNumber) => prevPageNumber + 1);
        }
      })
      .catch((error: Error) => {
        console.error("Error fetching jobs:", error);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [hasMore, pageNumber, selectedJob]);

  useEffect(() => {
    loadMore();
  }, []);

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Jobs</h1>
      <div className="md:flex md:gap-4">
        <div className="md:w-1/2" style={{ height: "90vh" }}>
          {loading ? (
            <SkeletonList count={6} />
          ) : (
            <Virtuoso
              data={jobs}
              endReached={loadMore}
              itemContent={(_index, job) => {
                return (
                  <JobItem job={job} key={job.id} onSelect={setSelectedJob} />
                );
              }}
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
