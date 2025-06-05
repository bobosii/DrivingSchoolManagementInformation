import axios from '../api/axios';

export interface VehicleType {
    id: number;
    name: string;
}

export const getAllVehicleTypes = async (): Promise<VehicleType[]> => {
    try {
        const response = await axios.get('/vehicle-types');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching vehicle types:', error);
        throw new Error('Failed to fetch vehicle types');
    }
}; 