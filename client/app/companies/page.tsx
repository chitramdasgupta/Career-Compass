"use client";

import React, { useEffect, useState } from "react";
import { Company } from "./types";
import { fetchCompanies } from "./companiesApi";
import CompanyList from "./components/CompanyList";

export default function Companies() {
  const [companies, setCompanies] = useState<Company[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);
    fetchCompanies()
      .then((fetchedCompanies) => {
        setCompanies(fetchedCompanies);
      })
      .catch((error: Error) => {
        console.error("Error fetching companies:", error);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Companies</h1>
      <CompanyList companies={companies} loading={loading} />
    </div>
  );
}
