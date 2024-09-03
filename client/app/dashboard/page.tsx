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

const bookmarkedJobs = [
  { id: 1, title: "Frontend Developer at XYZ Corp", company: "XYZ Corp" },
  { id: 2, title: "Backend Developer at ABC Inc", company: "ABC Inc" },
  {
    id: 3,
    title: "Full Stack Developer at Tech Solutions",
    company: "Tech Solutions",
  },
  { id: 4, title: "Data Scientist at Data Inc", company: "Data Inc" },
];

const submittedApplications = [
  {
    id: 1,
    title: "Frontend Developer at XYZ Corp",
    status: "Interview Scheduled",
  },
  {
    id: 2,
    title: "Backend Developer at ABC Inc",
    status: "Application Under Review",
  },
  {
    id: 3,
    title: "Full Stack Developer at Tech Solutions",
    status: "Rejected",
  },
  {
    id: 4,
    title: "Data Analyst at Data Inc",
    status: "Application Under Review",
  },
];

const Dashboard: React.FC = () => {
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
              {bookmarkedJobs.slice(0, 3).map((job) => (
                <ListItem key={job.id}>
                  <ListItemIcon>
                    <BookmarkIcon />
                  </ListItemIcon>
                  <ListItemText primary={job.title} secondary={job.company} />
                </ListItem>
              ))}
              {bookmarkedJobs.length > 3 && (
                <ListItem>
                  <Button
                    variant="outlined"
                    color="primary"
                    onClick={() => alert("Navigate to all bookmarked jobs")}
                    style={{ justifyContent: "flex-start" }}
                  >
                    See More ({bookmarkedJobs.length - 3})
                  </Button>
                </ListItem>
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
