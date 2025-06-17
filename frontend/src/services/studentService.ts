import axios from '../api/axios';

export interface Student {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    birthDate: string;
    termId: number | null;
    termName: string | null;
    fullName: string;
}

export interface StudentDashboard {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    birthDate: string;
    termName: string | null;
    termId: number | null;
    totalAppointments: number;
    totalCourseSessions: number;
    totalDocuments: number;
}

export const getAllStudents = async (): Promise<Student[]> => {
    try {
        const response = await axios.get('/student');
        return response.data.data.map((student: any) => ({
            ...student,
            fullName: `${student.firstName} ${student.lastName}`
        }));
    } catch (error) {
        console.error('Error fetching students:', error);
        throw new Error('Öğrenciler alınırken bir hata oluştu');
    }
};

export const getStudentDashboard = async (): Promise<StudentDashboard> => {
    try {
        const response = await axios.get('/student/dashboard');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching student dashboard:', error);
        throw new Error('Öğrenci paneli bilgileri alınırken bir hata oluştu');
    }
};

export const studentService = {
    getAllStudents,
    getStudentDashboard,
}; 