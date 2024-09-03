export type QualificationDegree = string;

export interface User {
  id: number;
  email: string;
  firstName: string | null;
  middleName: string | null;
  lastName: string | null;
  degree: QualificationDegree | null;
  department: string | null;
}