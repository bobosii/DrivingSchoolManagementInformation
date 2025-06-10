import axios from '../api/axios';

export interface Instructor {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    licenseNumber: string;
    isActive: boolean;
}

export const getAllInstructors = async (): Promise<Instructor[]> => {
    try {
        const response = await axios.get('/instructors');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching instructors:', error);
        throw new Error('Eğitmenler alınırken bir hata oluştu');
    }
};

export const createInstructor = async (instructor: Omit<Instructor, 'id'>): Promise<Instructor> => {
    try {
        const response = await axios.post('/instructors', instructor);
        return response.data.data;
    } catch (error) {
        console.error('Error creating instructor:', error);
        throw new Error('Eğitmen oluşturulurken bir hata oluştu');
    }
};

export const updateInstructor = async (id: number, instructor: Omit<Instructor, 'id'>): Promise<Instructor> => {
    try {
        const response = await axios.put(`/instructors/${id}`, instructor);
        return response.data.data;
    } catch (error) {
        console.error('Error updating instructor:', error);
        throw new Error('Eğitmen güncellenirken bir hata oluştu');
    }
};

export const deleteInstructor = async (id: number): Promise<void> => {
    try {
        await axios.delete(`/instructors/${id}`);
    } catch (error) {
        console.error('Error deleting instructor:', error);
        throw new Error('Eğitmen silinirken bir hata oluştu');
    }
}; 