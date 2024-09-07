export enum Severity {
    Success = "success",
    Error = "error",
    Info = "info",
    Warning = "warning",
  }
  
export interface SnackbarMessage {
    message: string;
    severity: Severity;
}
  
export interface CustomSnackbarProps {
    snackbarMessage: SnackbarMessage | null;
  }