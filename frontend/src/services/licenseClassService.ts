import axios from '../api/axios';

export interface LicenseClass {
    id: number;
    code: string;
    description: string;
}

export const getAllLicenseClasses = async (): Promise<LicenseClass[]> => {
    try {
        const response = await axios.get('/license-classes');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching license classes:', error);
        throw new Error('Failed to fetch license classes');
    }
}; 