import React from "react";
import {
  Box,
  IconButton,
  Modal,
  TextField,
  Typography,
  Button,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { useForm, Controller } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { User } from "../../types";

interface EditModalProps {
  open: boolean;
  handleClose: () => void;
  formData: Partial<User>;
  modalTitle: string;
  fields: { name: keyof User; label: string; validation?: any }[];
  handleSubmit: (data: Partial<User>) => void;
  modalStyle: any;
}

const createSchema = (fields: { name: keyof User; validation?: any }[]) => {
  const shape: any = {};
  fields.forEach((field) => {
    if (field.validation) {
      shape[field.name] = field.validation;
    }
  });
  return z.object(shape);
};

export function EditModal({
  open,
  handleClose,
  formData,
  modalTitle,
  fields,
  handleSubmit: onSubmit,
  modalStyle,
}: EditModalProps) {
  const schema = createSchema(fields);

  const { control, handleSubmit, reset } = useForm<Partial<User>>({
    resolver: zodResolver(schema),
    defaultValues: formData,
  });

  React.useEffect(() => {
    if (open) {
      reset(formData);
    }
  }, [open, formData, reset]);

  return (
    <Modal open={open} onClose={handleClose} aria-labelledby="modal-title">
      <Box sx={modalStyle}>
        <IconButton
          aria-label="close"
          onClick={handleClose}
          style={{ position: "absolute", right: 8, top: 8 }}
        >
          <CloseIcon />
        </IconButton>
        <Typography id="modal-title" variant="h6" component="h2" gutterBottom>
          {modalTitle}
        </Typography>
        {fields.map((field) => (
          <Controller
            key={field.name}
            name={field.name}
            control={control}
            render={({ field: { onChange, onBlur, value, ref } }) => (
              <TextField
                fullWidth
                margin="normal"
                label={field.label}
                onChange={onChange}
                onBlur={onBlur}
                value={value || ""}
                inputRef={ref}
                error={!!field.validation?.error}
                helperText={field.validation?.error?.message}
              />
            )}
          />
        ))}
        <Button
          variant="contained"
          color="primary"
          onClick={handleSubmit(onSubmit)}
          style={{ marginTop: 20 }}
        >
          Save Changes
        </Button>
      </Box>
    </Modal>
  );
}
