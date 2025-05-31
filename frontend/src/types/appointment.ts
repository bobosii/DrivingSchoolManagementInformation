export interface AppointmentResponse {
    studentId: number;
    studentName: string;
    instructorId: number;
    instructorName: string;
    courseSessionId: number;
    courseName: string;
    appointmentTypeId: number;
    appointmentTypeName: string;
    status: string;
    requestedAt: string;
    approvedAt?: string | null;
    appointmentTime: string;
}
