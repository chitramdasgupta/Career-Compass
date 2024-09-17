export interface JobPosting {
  id: number;
  title: string;
  description: string;
  company: {
    name: string;
  };
  country: string;
  jobLocation: string;
}

export interface JobPostingsResponse {
  content: JobPosting[];
  totalPages: number;
  totalElements: number;
  number: number;
  last: boolean;
}
