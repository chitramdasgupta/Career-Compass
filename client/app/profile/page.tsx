"use client";

import React, { useEffect, useState } from "react";
import {
  Container,
  CircularProgress,
  useTheme,
  useMediaQuery,
  Typography,
} from "@mui/material";
import { fetchCurrentUser } from "./profileApi";
import { User } from "./types";
import { ProfileDetails } from "./_components/ProfileDetails";
import { EditModal } from "./_components/EditModal";
import { z } from "zod";

export default function Profile() {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [openPersonalModal, setOpenPersonalModal] = useState(false);
  const [openEducationalModal, setOpenEducationalModal] = useState(false);

  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down("sm"));

  const modalStyle = {
    position: "absolute" as "absolute",
    top: isMobile ? 0 : "50%",
    left: isMobile ? 0 : "50%",
    transform: isMobile ? "none" : "translate(-50%, -50%)",
    width: isMobile ? "100%" : 400,
    height: isMobile ? "100%" : "auto",
    bgcolor: "background.paper",
    boxShadow: 24,
    p: 4,
    overflow: "auto",
  };

  useEffect(() => {
    fetchCurrentUser()
      .then((userData) => {
        setUser(userData);
        setLoading(false);
      })
      .catch((error) => {
        console.error("Error fetching user data:", error);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return (
      <Container
        maxWidth="sm"
        style={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          height: "100vh",
        }}
      >
        <CircularProgress />
      </Container>
    );
  }

  if (!user) {
    return (
      <Container maxWidth="sm">
        <Typography variant="h6">Error loading user data</Typography>
      </Container>
    );
  }

  const personalSchema = z.object({
    firstName: z.string().min(1, "First Name is required"),
    middleName: z.string().optional(),
    lastName: z.string().min(1, "Last Name is required"),
  });

  const educationalSchema = z.object({
    degree: z.string().min(1, "Degree is required"),
    department: z.string().min(1, "Department is required"),
  });

  return (
    <Container maxWidth="xl">
      <Typography variant="h4" gutterBottom className="p-4">
        User Profile
      </Typography>

      <ProfileDetails
        user={user}
        title="Personal Details"
        fields={[
          { label: "Email", value: user.email },
          { label: "First Name", value: user.firstName },
          { label: "Middle Name", value: user.middleName },
          { label: "Last Name", value: user.lastName },
        ]}
        onEdit={() => setOpenPersonalModal(true)}
      />

      <ProfileDetails
        user={user}
        title="Educational Qualifications"
        fields={[
          { label: "Degree", value: user.degree },
          { label: "Department", value: user.department },
        ]}
        onEdit={() => setOpenEducationalModal(true)}
      />

      <EditModal
        open={openPersonalModal}
        handleClose={() => setOpenPersonalModal(false)}
        formData={user} // Pass the user data as default values
        modalTitle="Edit Personal Details"
        fields={[
          {
            name: "firstName",
            label: "First Name",
            validation: personalSchema.shape.firstName,
          },
          {
            name: "middleName",
            label: "Middle Name",
            validation: personalSchema.shape.middleName,
          },
          {
            name: "lastName",
            label: "Last Name",
            validation: personalSchema.shape.lastName,
          },
        ]}
        handleSubmit={(data) => {
          console.log("Submitting personal data:", data);
          setOpenPersonalModal(false);
        }}
        modalStyle={modalStyle}
      />

      <EditModal
        open={openEducationalModal}
        handleClose={() => setOpenEducationalModal(false)}
        formData={user}
        modalTitle="Edit Educational Qualifications"
        fields={[
          {
            name: "degree",
            label: "Degree",
            validation: educationalSchema.shape.degree,
          },
          {
            name: "department",
            label: "Department",
            validation: educationalSchema.shape.department,
          },
        ]}
        handleSubmit={(data) => {
          console.log("Submitting educational data:", data);
          setOpenEducationalModal(false);
        }}
        modalStyle={modalStyle}
      />
    </Container>
  );
}
