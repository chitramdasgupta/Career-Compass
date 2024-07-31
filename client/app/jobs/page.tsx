"use client";

import React, { useEffect, useState } from "react";
import { Job } from "@/app/jobs/types";
import { fetchJobs } from "@/app/jobs/jobsApi";
import { JobList } from "@/app/jobs/jobList";

export default function Jobs() {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    fetchJobs()
      .then((fetchedJobs) => {
        setJobs(fetchedJobs);
      })
      .catch((error: Error) => {
        console.error("Error fetching jobs:", error);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  return (
    <div>
      <h1 className="my-2">Posts</h1>
      <JobList jobs={jobs} loading={loading} />
    </div>
  );
}
