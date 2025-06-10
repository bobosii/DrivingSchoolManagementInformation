import axios from '../api/axios';

export interface Classroom {
    id: number;
    name: string;
    capacity: number;
    location: string;
    isActive: boolean;
}

export interface CreateClassroomRequest {
    name: string;
    capacity: number;
    location: string;
}

export const getAllClassrooms = async (): Promise<Classroom[]> => {
    try {
        const response = await axios.get('/classroom');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching classrooms:', error);
        throw new Error('Sınıflar alınırken bir hata oluştu');
    }
};

export const getClassroomById = async (id: number): Promise<Classroom> => {
    try {
        const response = await axios.get(`/classroom/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Error fetching classroom:', error);
        throw new Error('Sınıf bilgileri alınırken bir hata oluştu');
    }
};

export const createClassroom = async (classroom: CreateClassroomRequest): Promise<Classroom> => {
    try {
        const response = await axios.post('/classroom/create', classroom);
        return response.data.data;
    } catch (error) {
        console.error('Error creating classroom:', error);
        throw new Error('Sınıf oluşturulurken bir hata oluştu');
    }
};

export const updateClassroom = async (id: number, classroom: CreateClassroomRequest): Promise<Classroom> => {
    try {
        const response = await axios.put(`/classroom/${id}`, classroom);
        return response.data.data;
    } catch (error) {
        console.error('Error updating classroom:', error);
        throw new Error('Sınıf güncellenirken bir hata oluştu');
    }
};

export const deleteClassroom = async (id: number): Promise<void> => {
    try {
        await axios.delete(`/classroom/${id}`);
    } catch (error) {
        console.error('Error deleting classroom:', error);
        throw new Error('Sınıf silinirken bir hata oluştu');
    }
}; 