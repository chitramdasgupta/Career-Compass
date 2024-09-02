"use client";

import React, { useCallback, useEffect, useState } from "react";
import { Company } from "./types";
import { fetchCompanies } from "./companiesApi";
import { Virtuoso } from "react-virtuoso";
import { SkeletonList } from "../posts/components/PostSkeleton/skeletonList";
import CompanyItem from "./components/CompanyItem";

export default function Companies() {
  const [companies, setCompanies] = useState<Company[]>([]);
  const [loading, setLoading] = useState(true);
  const [pageNumber, setPageNumber] = useState(0);
  const [hasMore, setHasMore] = useState(true);

  const loadMore = useCallback(() => {
    if (!hasMore) return;

    fetchCompanies(pageNumber)
      .then((response) => {
        console.log({ response });
        setLoading(false);
        setCompanies((prevCompanies) => [
          ...prevCompanies,
          ...response.content,
        ]);

        if (response.last) {
          setHasMore(false);
        } else {
          setPageNumber((prevPageNumber) => prevPageNumber + 1);
        }
      })
      .catch((error: Error) => {
        console.error("Error fetching companies:", error);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [hasMore, pageNumber]);

  useEffect(() => {
    loadMore();
  }, []);

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Companies</h1>
      <div style={{ height: "85vh" }}>
        {loading && companies.length === 0 ? (
          <SkeletonList count={6} />
        ) : (
          <Virtuoso
            data={companies}
            endReached={loadMore}
            itemContent={(_index, company) => (
              <CompanyItem company={company} key={company.id} />
            )}
          />
        )}
      </div>
    </div>
  );
}
