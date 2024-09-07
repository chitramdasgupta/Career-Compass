"use client";

import React, { useState, useEffect } from "react";
import { Snackbar, Alert } from "@mui/material";
import { CustomSnackbarProps, SnackbarMessage } from "./types";

const CustomSnackbar: React.FC<CustomSnackbarProps> = ({ snackbarMessage }) => {
  const [open, setOpen] = useState(false);
  const [currentMessage, setCurrentMessage] = useState<SnackbarMessage | null>(
    null,
  );

  useEffect(() => {
    if (snackbarMessage) {
      setCurrentMessage(snackbarMessage);
      setOpen(true);
    }
  }, [snackbarMessage]);

  const handleClose = (
    event?: React.SyntheticEvent | Event,
    reason?: string,
  ) => {
    if (reason === "clickaway") {
      return;
    }
    setOpen(false);
  };

  const onExited = () => {
    setCurrentMessage(null);
  };

  if (!currentMessage) return null;

  return (
    <Snackbar
      open={open}
      autoHideDuration={6000}
      onClose={handleClose}
      anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
      TransitionProps={{ onExited }}
    >
      <Alert
        onClose={handleClose}
        severity={currentMessage.severity}
        sx={{ width: "100%" }}
      >
        {currentMessage.message}
      </Alert>
    </Snackbar>
  );
};

export default CustomSnackbar;
