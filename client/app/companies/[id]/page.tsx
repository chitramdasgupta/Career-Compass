"use client";

import React from "react";
import { useParams } from "next/navigation";
import { useGetCompanyQuery } from "../companiesApi";
import Card from "@mui/material/Card";
import { CardContent, CardHeader, Rating } from "@mui/material";
import { SkeletonList } from "@/app/posts/components/PostSkeleton/skeletonList";
import CreateIcon from "@mui/icons-material/Create";
import Link from "next/link";

export default function CompanyShow() {
  const params = useParams();
  const id = params.id as string;

  const { data: company, isLoading, error } = useGetCompanyQuery(id);

  if (isLoading) {
    return <SkeletonList count={1} />;
  }

  if (error || !company) {
    return <p>Error loading company: {error.toString()}</p>;
  }

  return (
    <div className="container mx-auto p-4">
      <Card>
        <CardHeader title={company.name} />
        <CardContent>
          <p>{company.description}</p>
          <hr />
          <Rating
            name="simple-controlled"
            disabled
            value={company.averageRating}
            precision={0.5}
          />
          <span>({company.reviews.length} Reviews)</span>
          <Link href={`/companies/${id}/new-review`}>
            <CreateIcon /> New Review
          </Link>
        </CardContent>
      </Card>
    </div>
  );
}
