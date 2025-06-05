import axios from "axios";
import type { AppointmentResponse } from "../types/appointment";
import { jwtDecode } from "jwt-decode";

interface DecodedToken {
    sub: string;
    role: string;
    id: number;
    exp: number;
}

const getLinkedEntityId = () => {
    const linkedEntityId = localStorage.getItem("linkedEntityId");
    return linkedEntityId ? parseInt(linkedEntityId) : null;
};

const getRoleFromToken = () => {
    const token = localStorage.getItem("token");
    if (!token) return null;
    try {
        const decoded = jwtDecode<DecodedToken>(token);
        return decoded.role;
    } catch (error) {
        console.error("Error decoding token:", error);
        return null;
    }
};

export const getAllAppointments = async (): Promise<AppointmentResponse[]> => {
    const token = localStorage.getItem("token");
    if (!token) {
        throw new Error("No token found");
    }

    const userRole = getRoleFromToken();
    if (!userRole) {
        throw new Error("Could not get user role");
    }

    let endpoint = "http://localhost:8080/api/appointments/all";
    
    if (userRole === "STUDENT" || userRole === "INSTRUCTOR") {
        const linkedEntityId = getLinkedEntityId();
        if (!linkedEntityId) {
            throw new Error("Could not get linked entity ID");
        }

        if (userRole === "STUDENT") {
            endpoint = `http://localhost:8080/api/appointments/student/${linkedEntityId}`;
        } else if (userRole === "INSTRUCTOR") {
            endpoint = `http://localhost:8080/api/appointments/instructor/${linkedEntityId}`;
        }
    }

    try {
        const res = await axios.get(endpoint, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        return res.data.data;
    } catch (error) {
        console.error("Error fetching appointments:", error);
        throw error;
    }
};

export const deleteAppointment = async (id: number): Promise<void> => {
    const token = localStorage.getItem('token');
    if (!token) {
        throw new Error('No token found');
    }

    const response = await fetch(`http://localhost:8080/api/appointments/${id}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Failed to delete appointment');
    }
};
