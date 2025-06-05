import axios from "axios";

export interface Student {
    id: number;
    firstName: string;
    lastName: string;
    fullName: string;
    email: string;
    birthDate: string;
}

export const getAllStudents = async (): Promise<Student[]> => {
    const token = localStorage.getItem("token");
    if (!token) {
        throw new Error("No token found");
    }

    try {
        const response = await axios.get("http://localhost:8080/api/student", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        return response.data.data;
    } catch (error) {
        console.error("Error fetching students:", error);
        throw error;
    }
}; 