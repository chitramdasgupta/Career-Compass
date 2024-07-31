import React from "react";
import Skeleton from "@mui/material/Skeleton";

const SkeletonContainer: React.FC = () => (
  <div className="border-2 mb-3 px-2">
    <Skeleton variant="text" width="60%" height={28} animation="wave" />
    <Skeleton variant="text" width="100%" animation="wave" />
    <Skeleton variant="text" width="100%" animation="wave" />
    <Skeleton variant="text" width="40%" animation="wave" />
  </div>
);

export const SkeletonList: React.FC<{ count: number }> = ({ count }) => (
  <>
    {Array.from({ length: count }).map((_, index) => (
      <SkeletonContainer key={index} />
    ))}
  </>
);
