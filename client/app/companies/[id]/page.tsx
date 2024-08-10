"use client";

import React, { useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { Company } from "../types";
import { fetchCompany } from "../companiesApi";
import Card from "@mui/material/Card";
import { CardContent, CardHeader } from "@mui/material";
import { SkeletonList } from "@/app/posts/components/PostSkeleton/skeletonList";

export default function CompanyShow() {
  const params = useParams();
  const id = params.id as string;
  const [company, setCompany] = useState<Company | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (id) {
      setLoading(true);
      fetchCompany(id)
        .then((fetchedCompany) => {
          setCompany(fetchedCompany);
        })
        .catch((error: Error) => {
          console.error("Error fetching company:", error);
        })
        .finally(() => {
          setLoading(false);
        });
    }
  }, [id]);

  if (loading) {
    return <SkeletonList count={0} />;
  }

  if (!company) {
    return <p>Company not found</p>;
  }

  return (
    <div className="container mx-auto p-4">
      <Card>
        <CardHeader title={company.name} />
        <CardContent>
          <p>{company.description}</p>
        </CardContent>
      </Card>
    </div>
  );
}
