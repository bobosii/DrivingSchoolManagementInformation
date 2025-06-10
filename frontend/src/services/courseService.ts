import axios from '../api/axios';

export interface Course {
    id: number;
    name: string;
    courseType: string;
}

export interface CreateCourseRequest {
    name: string;
    courseType: string;
}

export const getAllCourses = async (): Promise<Course[]> => {
    try {
        const response = await axios.get('/courses');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching courses:', error);
        throw new Error('Kurslar alınırken bir hata oluştu');
    }
};

export const createCourse = async (request: CreateCourseRequest): Promise<Course> => {
    try {
        if (!request.name || !request.courseType) {
            throw new Error('Kurs adı ve tipi zorunludur');
        }
        const response = await axios.post('/courses', request);
        return response.data.data;
    } catch (error: any) {
        console.error('Error creating course:', error);
        throw new Error(error.response?.data?.message || 'Kurs oluşturulurken bir hata oluştu');
    }
};

export const updateCourse = async (id: number, request: CreateCourseRequest): Promise<Course> => {
    try {
        if (!request.name || !request.courseType) {
            throw new Error('Kurs adı ve tipi zorunludur');
        }
        const response = await axios.put(`/courses/${id}`, request);
        return response.data.data;
    } catch (error: any) {
        console.error('Error updating course:', error);
        throw new Error(error.response?.data?.message || 'Kurs güncellenirken bir hata oluştu');
    }
};

export const deleteCourse = async (id: number): Promise<void> => {
    try {
        await axios.delete(`/courses/${id}`);
    } catch (error: any) {
        console.error('Error deleting course:', error);
        throw new Error(error.response?.data?.message || 'Kurs silinirken bir hata oluştu');
    }
}; 