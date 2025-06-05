import axios from "axios";

export interface AppointmentType {
    id: number;
    name: string;
}

export const getAllAppointmentTypes = async (): Promise<AppointmentType[]> => {
    const token = localStorage.getItem("token");
    if (!token) {
        throw new Error("No token found");
    }

    try {
        const response = await axios.get("http://localhost:8080/api/appointment-types", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        return response.data.data;
    } catch (error) {
        console.error("Error fetching appointment types:", error);
        throw error;
    }
}; 