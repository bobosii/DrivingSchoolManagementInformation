import axios from '../api/axios';

export interface Vehicle {
    id: number;
    plate: string;
    brand: string;
    automatic: boolean;
    inspectionDate: string;
    insuranceDate: string;
    vehicleType: {
        id: number;
        name: string;
    };
    licenseClasses: Array<{
        id: number;
        name: string;
    }>;
}

interface VehicleCreateRequest {
    plate: string;
    brand: string;
    automatic: boolean;
    inspectionDate: string;
    insuranceDate: string;
    vehicleTypeId: number;
    licenseClassIds: number[];
}

export const getAllVehicles = async (): Promise<Vehicle[]> => {
    try {
        const response = await axios.get('/vehicles');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching vehicles:', error);
        throw new Error('Failed to fetch vehicles');
    }
};

export const createVehicle = async (vehicle: VehicleCreateRequest): Promise<Vehicle> => {
    try {
        const response = await axios.post('/vehicles', vehicle);
        return response.data.data;
    } catch (error) {
        console.error('Error creating vehicle:', error);
        throw new Error('Failed to create vehicle');
    }
};

export const updateVehicle = async (id: number, vehicle: VehicleCreateRequest): Promise<Vehicle> => {
    try {
        const response = await axios.put(`/vehicles/${id}`, vehicle);
        return response.data.data;
    } catch (error) {
        console.error('Error updating vehicle:', error);
        throw new Error('Failed to update vehicle');
    }
};

export const deleteVehicle = async (id: number): Promise<void> => {
    try {
        await axios.delete(`/vehicles/${id}`);
    } catch (error) {
        console.error('Error deleting vehicle:', error);
        throw new Error('Failed to delete vehicle');
    }
}; 