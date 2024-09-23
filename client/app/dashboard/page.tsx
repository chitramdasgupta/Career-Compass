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
import Link from "next/link";
import { SkeletonList } from "../posts/components/PostSkeleton/skeletonList";

const Dashboard: React.FC = () => {
  const { data, isLoading } = useGetBookmarkedJobsQuery(0);
  const bookmarkedJobs = data?.content || [];
  const submittedApplications = [];

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
              {isLoading ? (
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
              {submittedApplications.slice(0, 3).map((application) => (
                <ListItem key={application.id}>
                  <ListItemIcon>
                    <AssignmentIcon />
                  </ListItemIcon>
                  <ListItemText
                    primary={application.title}
                    secondary={`Status: ${application.status}`}
                  />
                </ListItem>
              ))}
              {submittedApplications.length > 3 && (
                <ListItem>
                  <Button
                    variant="outlined"
                    color="primary"
                    onClick={() =>
                      alert("Navigate to all submitted applications")
                    }
                    style={{ justifyContent: "flex-start" }}
                  >
                    See More ({submittedApplications.length - 3})
                  </Button>
                </ListItem>
              )}
            </List>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
};

export default Dashboard;
