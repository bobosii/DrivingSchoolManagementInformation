import axios from '../api/axios';
import type { Student } from './studentService';

export interface Course {
    id: number;
    name: string;
    courseType: string;
}

export interface CourseSession {
    id: number;
    courseId: number;
    courseName: string;
    instructorId: number;
    instructorFullName: string;
    classroomId: number;
    classroomName: string;
    startTime: string;
    endTime: string;
    maxStudents: number;
    active: boolean;
    students?: Student[];
}

export interface CreateCourseSessionRequest {
    courseId: number;
    instructorId: number;
    classroomId: number;
    startTime: string;
    endTime: string;
    maxStudents: number;
}

export const getAllCourseSessions = async (): Promise<CourseSession[]> => {
    try {
        const response = await axios.get('/course-sessions');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching course sessions:', error);
        throw new Error('Kurs oturumları alınırken bir hata oluştu');
    }
};

export const createCourseSession = async (request: CreateCourseSessionRequest): Promise<CourseSession> => {
    try {
        const response = await axios.post('/course-sessions', request);
        return response.data.data;
    } catch (error) {
        console.error('Error creating course session:', error);
        throw new Error('Kurs oturumu oluşturulurken bir hata oluştu');
    }
};

export const updateCourseSession = async (id: number, request: CreateCourseSessionRequest): Promise<CourseSession> => {
    try {
        const response = await axios.put(`/course-sessions/${id}`, request);
        return response.data.data;
    } catch (error) {
        console.error('Error updating course session:', error);
        throw new Error('Kurs oturumu güncellenirken bir hata oluştu');
    }
};

export const deleteCourseSession = async (id: number): Promise<void> => {
    try {
        await axios.delete(`/course-sessions/${id}`);
    } catch (error) {
        console.error('Error deleting course session:', error);
        throw new Error('Kurs oturumu silinirken bir hata oluştu');
    }
};

// Bu metodlar artık kullanılmayacak çünkü durum tarihe göre otomatik belirleniyor
export const activateCourseSession = async (id: number): Promise<CourseSession> => {
    try {
        const response = await axios.put(`/course-sessions/${id}/activate`);
        return response.data.data;
    } catch (error) {
        console.error('Error activating course session:', error);
        throw new Error('Kurs oturumu aktifleştirilirken bir hata oluştu');
    }
};

export const deactivateCourseSession = async (id: number): Promise<CourseSession> => {
    try {
        const response = await axios.put(`/course-sessions/${id}/deactivate`);
        return response.data.data;
    } catch (error) {
        console.error('Error deactivating course session:', error);
        throw new Error('Kurs oturumu deaktif edilirken bir hata oluştu');
    }
};

const validateCourseSessionRequest = (request: CreateCourseSessionRequest) => {
    if (!request.courseId) throw new Error('Kurs seçimi zorunludur');
    if (!request.instructorId) throw new Error('Eğitmen seçimi zorunludur');
    if (!request.classroomId) throw new Error('Sınıf seçimi zorunludur');
    if (!request.startTime) throw new Error('Başlangıç zamanı zorunludur');
    if (!request.endTime) throw new Error('Bitiş zamanı zorunludur');
    if (!request.maxStudents || request.maxStudents < 1) throw new Error('Maksimum öğrenci sayısı en az 1 olmalıdır');
    
    const start = new Date(request.startTime);
    const end = new Date(request.endTime);
    
    if (start >= end) {
        throw new Error('Bitiş zamanı başlangıç zamanından sonra olmalıdır');
    }
};

export const assignStudentToSession = async (sessionId: number, studentId: number): Promise<CourseSession> => {
    try {
        const response = await axios.post(`/course-sessions/${sessionId}/students/${studentId}`);
        if (!response.data.success) {
            throw new Error(response.data.message || 'Öğrenci atanırken bir hata oluştu');
        }
        return response.data.data;
    } catch (error: any) {
        console.error('Error assigning student to session:', error);
        if (error.response?.data?.message) {
            throw new Error(error.response.data.message);
        }
        throw new Error('Öğrenci atanırken bir hata oluştu');
    }
};

export const removeStudentFromSession = async (sessionId: number, studentId: number): Promise<CourseSession> => {
    try {
        const response = await axios.delete(`/course-sessions/${sessionId}/students/${studentId}`);
        if (!response.data.success) {
            throw new Error(response.data.message || 'Öğrenci kaldırılırken bir hata oluştu');
        }
        return response.data.data;
    } catch (error: any) {
        console.error('Error removing student from session:', error);
        if (error.response?.data?.message) {
            throw new Error(error.response.data.message);
        }
        throw new Error('Öğrenci kaldırılırken bir hata oluştu');
    }
};

export const getUnassignedStudents = async (termId?: number): Promise<Student[]> => {
    try {
        const response = await axios.get('/course-sessions/unassigned-students', {
            params: { termId }
        });
        return response.data.data;
    } catch (error) {
        console.error('Error fetching unassigned students:', error);
        throw new Error('Atanmamış öğrenciler alınırken bir hata oluştu');
    }
};

export const getMyCourseSessions = async (): Promise<CourseSession[]> => {
    try {
        const response = await axios.get('/student/course-sessions');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching my course sessions:', error);
        throw error;
    }
};

export const getInstructorCourseSessions = async (): Promise<CourseSession[]> => {
    const response = await axios.get('/course-sessions/my');
    return response.data.data;
}; 