export interface AppointmentResponse {
    id: number;
    studentId: number;
    studentName: string;
    instructorId: number;
    instructorName: string;
    appointmentTypeId: number;
    appointmentTypeName: string;
    status: string;
    requestedAt: string;
    approvedAt?: string | null;
    appointmentTime: string;
}
