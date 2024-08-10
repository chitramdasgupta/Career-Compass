import React from "react";
import { Company } from "../../types";
import { SkeletonList } from "@/app/posts/components/PostSkeleton/skeletonList";
import CompanyItem from "../CompanyItem";

interface CompanyListProps {
  companies: Company[];
  loading: boolean;
}

const CompanyList: React.FC<CompanyListProps> = ({ companies, loading }) => {
  if (loading) {
    return <SkeletonList count={6} />;
  }

  return (
    <>
      {companies.map((company) => (
        <CompanyItem company={company} key={company.id} />
      ))}
    </>
  );
};

export default CompanyList;
