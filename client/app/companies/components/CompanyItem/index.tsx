import React from "react";
import { Company } from "../../types";
import Link from "next/link";
import Card from "@mui/material/Card";
import CardHeader from "@mui/material/CardHeader";
import CardContent from "@mui/material/CardContent";

interface CompanyProps {
  company: Company;
}

const CompanyItem: React.FC<CompanyProps> = ({ company }) => {
  return (
    <Link key={company.id} href={`/companies/${company.id}`}>
      <Card className="mb-4 cursor-pointer">
        <CardHeader title={company.name} />
        <CardContent>
          <p>{company?.description || ""}</p>
        </CardContent>
      </Card>
    </Link>
  );
};

export default CompanyItem;
