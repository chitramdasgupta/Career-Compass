import { Company } from "@/app/companies/types";

export interface Job {
  id: number;
  title: string;
  description: string | null;
  company: Company;
}
