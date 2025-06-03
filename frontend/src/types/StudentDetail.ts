export interface StudentDetail {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    birthDate: string;
    termName: string;
    appointments: {
        id: number;
        appointmentTime: string;
        status: string;
        instructorName: string;
        courseName: string;
        appointmentTypeName: string;
    }[];
}
