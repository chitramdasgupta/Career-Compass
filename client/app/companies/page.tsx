"use client";

import React, { useState } from "react";
import { Virtuoso } from "react-virtuoso";
import { SkeletonList } from "../posts/components/PostSkeleton/skeletonList";
import CompanyItem from "./components/CompanyItem";
import { useGetCompaniesQuery } from "./companiesApi";

export default function Companies() {
  const [page, setPage] = useState(0);
  const { data, isLoading, isFetching, isError } = useGetCompaniesQuery(page, {
    refetchOnMountOrArgChange: true,
  });

  if (isError) return <div>Error loading companies</div>;

  const companies = data?.content || [];
  const hasMore = !data?.last;

  const loadMore = () => {
    if (!isFetching && hasMore) {
      setPage((prevPage) => prevPage + 1);
    }
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Companies</h1>
      <div style={{ height: "85vh" }}>
        {isLoading && companies.length === 0 ? (
          <SkeletonList count={6} />
        ) : (
          <Virtuoso
            data={companies}
            endReached={loadMore}
            overscan={200}
            itemContent={(_, company) => (
              <CompanyItem company={company} key={company.id} />
            )}
          />
        )}
      </div>
    </div>
  );
}
