export interface StudentDetail {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    birthDate: string;
    termId: number | null;
    termName: string | null;
    appointments: {
        id: number;
        appointmentTime: string;
        status: string;
        instructorName: string;
        courseName: string;
        appointmentTypeName: string;
    }[];
}
