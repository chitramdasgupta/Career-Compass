"use client";

import React from "react";
import {
  Container,
  Typography,
  Paper,
  Grid,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
  Button,
} from "@mui/material";
import BookmarkIcon from "@mui/icons-material/Bookmark";
import AssignmentIcon from "@mui/icons-material/Assignment";
import { useGetBookmarkedJobsQuery } from "@/app/shared/api/bookmarksApi";
import { useGetSubmittedJobApplicationsQuery } from "@/app/shared/api/jobApplicationsApi";
import Link from "next/link";
import { SkeletonList } from "../posts/components/PostSkeleton/skeletonList";

const Dashboard: React.FC = () => {
  const { data: bookmarkedData, isLoading: isBookmarkedLoading } =
    useGetBookmarkedJobsQuery(0);
  const { data: submittedData, isLoading: isSubmittedLoading } =
    useGetSubmittedJobApplicationsQuery(0);

  const bookmarkedJobs = bookmarkedData?.content || [];
  const submittedApplications = submittedData?.content || [];

  return (
    <Container maxWidth="lg" style={{ marginTop: "20px" }}>
      <Typography variant="h4" gutterBottom>
        Dashboard
      </Typography>
      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <Paper elevation={3} style={{ padding: "20px" }}>
            <Typography variant="h5" gutterBottom>
              Bookmarked Jobs
            </Typography>
            <List>
              {isBookmarkedLoading ? (
                <SkeletonList count={3} />
              ) : (
                <>
                  {bookmarkedJobs.slice(0, 3).map((job) => (
                    <ListItem key={job.id}>
                      <ListItemIcon>
                        <BookmarkIcon />
                      </ListItemIcon>
                      <ListItemText
                        primary={job.title}
                        secondary={job.company.name}
                      />
                    </ListItem>
                  ))}
                  {bookmarkedJobs.length > 0 && (
                    <ListItem>
                      <Link href="/dashboard/bookmarked-jobs" passHref>
                        <Button
                          variant="outlined"
                          color="primary"
                          style={{ justifyContent: "flex-start" }}
                        >
                          See All
                        </Button>
                      </Link>
                    </ListItem>
                  )}
                </>
              )}
            </List>
          </Paper>
        </Grid>
        <Grid item xs={12} md={6}>
          <Paper elevation={3} style={{ padding: "20px" }}>
            <Typography variant="h5" gutterBottom>
              Submitted Job Applications
            </Typography>
            <List>
              {isSubmittedLoading ? (
                <SkeletonList count={3} />
              ) : (
                <>
                  {submittedApplications.slice(0, 3).map((job) => (
                    <ListItem key={job.id}>
                      <ListItemIcon>
                        <AssignmentIcon />
                      </ListItemIcon>
                      <ListItemText
                        primary={job.title}
                        secondary={job.company.name}
                      />
                    </ListItem>
                  ))}
                  {submittedApplications.length > 0 && (
                    <ListItem>
                      <Link href="/dashboard/submitted-applications" passHref>
                        <Button
                          variant="outlined"
                          color="primary"
                          style={{ justifyContent: "flex-start" }}
                        >
                          See All
                        </Button>
                      </Link>
                    </ListItem>
                  )}
                </>
              )}
            </List>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
};

export default Dashboard;
