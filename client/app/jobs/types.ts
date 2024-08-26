import { Company } from "@/app/companies/types";

export interface Job {
  id: number;
  title: string;
  description: string | null;
  minimumRequirement: string | null;
  desiredRequirement: string | null;
  city: string | null;
  country: string;
  jobLocation: string;
  minimumSalary: number | null;
  maximumSalary: number | null;
  currency: string | null;
  questionnaireId: number;
  company: Company;
}
