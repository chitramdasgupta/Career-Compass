import React from "react";
import { Paper, Typography, Grid, IconButton } from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import { User } from "../../types";

interface ProfileDetailsProps {
  user: User;
  title: string;
  fields: { label: string; value: string | undefined }[];
  onEdit: () => void;
}

export function ProfileDetails({
  user,
  title,
  fields,
  onEdit,
}: ProfileDetailsProps) {
  return (
    <Paper
      elevation={3}
      style={{ padding: "20px", marginBottom: "20px", position: "relative" }}
    >
      <Typography variant="h5" gutterBottom>
        {title}
        <IconButton
          aria-label="edit"
          onClick={onEdit}
          style={{ position: "absolute", right: 20, top: 20 }}
        >
          <EditIcon />
        </IconButton>
      </Typography>
      <Grid container spacing={2}>
        {fields.map((field, index) => (
          <Grid item xs={12} sm={6} key={index}>
            <Typography>
              <strong>{field.label}:</strong> {field.value || "Not provided"}
            </Typography>
          </Grid>
        ))}
      </Grid>
    </Paper>
  );
}
