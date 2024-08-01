"use client";

import React, { useEffect, useState } from "react";
import { Job } from "@/app/jobs/types";
import { fetchJobs } from "@/app/jobs/jobsApi";
import { JobList } from "@/app/jobs/jobList";
import { JobDescription } from "./JobDescription";

export default function Jobs() {
  const [jobs, setJobs] = useState<Job[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedJob, setSelectedJob] = useState<Job | null>(null);

  useEffect(() => {
    setLoading(true);
    fetchJobs()
      .then((fetchedJobs) => {
        setJobs(fetchedJobs);
        setSelectedJob(fetchedJobs[0]);
      })
      .catch((error: Error) => {
        console.error("Error fetching jobs:", error);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  const handleJobSelect = (job: Job) => {
    setSelectedJob(job);
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Jobs</h1>
      <div className="md:flex md:gap-4">
        <div className="md:w-1/2">
          <JobList
            jobs={jobs}
            loading={loading}
            onJobSelect={handleJobSelect}
          />
        </div>
        <div className="md:w-1/2 md:sticky md:top-4 invisible md:visible">
          <JobDescription job={selectedJob} />
        </div>
      </div>
    </div>
  );
}
