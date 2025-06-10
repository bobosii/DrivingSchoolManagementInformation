import axios from '../api/axios';

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
    isActive: boolean;
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