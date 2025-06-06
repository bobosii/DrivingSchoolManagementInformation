import axios from '../api/axios';

export interface Instructor {
    id: number;
    firstName: string;
    lastName: string;
    fullName: string;
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