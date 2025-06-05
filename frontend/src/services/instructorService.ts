import axios from "axios";

export interface Instructor {
    id: number;
    firstName: string;
    lastName: string;
    fullName: string;
}

export const getAllInstructors = async (): Promise<Instructor[]> => {
    const token = localStorage.getItem("token");
    if (!token) {
        throw new Error("No token found");
    }

    try {
        console.log("Token being sent:", token); // Debug log
        const res = await axios.get("http://localhost:8080/api/instructors", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        console.log("Response:", res.data); // Debug log
        return res.data.data;
    } catch (error) {
        console.error("Error fetching instructors:", error);
        if (axios.isAxiosError(error)) {
            console.error("Response data:", error.response?.data); // Debug log
            console.error("Response status:", error.response?.status); // Debug log
        }
        throw error;
    }
}; 