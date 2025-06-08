export interface StudentDetail {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    birthDate: string;
    termId: number;
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
