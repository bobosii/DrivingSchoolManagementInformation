import axios from "axios";
import type { AppointmentResponse } from "../types/appointment";

const getLinkedEntityId = () => {
    const linkedEntityId = localStorage.getItem("linkedEntityId");
    return linkedEntityId ? parseInt(linkedEntityId) : null;
};

const getUserRoleFromToken = () => {
    const token = localStorage.getItem("token");
    if (!token) return null;
    try {
        const decoded = JSON.parse(atob(token.split(".")[1]));
        return decoded.role;
    } catch {
        return null;
    }
};

export const getAllAppointments = async (): Promise<AppointmentResponse[]> => {
    const token = localStorage.getItem("token");
    if (!token) {
        throw new Error("No token found");
    }

    const role = getUserRoleFromToken();
    if (!role) {
        throw new Error("Could not get user role from token");
    }

    let endpoint = "http://localhost:8080/api/appointments/all";
    
    if (role === "STUDENT" || role === "INSTRUCTOR") {
        const linkedEntityId = getLinkedEntityId();
        if (!linkedEntityId) {
            throw new Error("Could not get linked entity ID");
        }

        if (role === "STUDENT") {
            endpoint = `http://localhost:8080/api/appointments/student/${linkedEntityId}`;
        } else if (role === "INSTRUCTOR") {
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
