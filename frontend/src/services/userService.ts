import axios from "axios";
import type { User } from "../types/user";


export const getAllUsers = async (): Promise<User[]> => {
    const token = localStorage.getItem("token");
    if (!token) {
        throw new Error("No token found");
    }

    try {
        const res = await axios.get("http://localhost:8080/api/admin/users", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        return res.data.data;
    } catch (error) {
        console.error("Error fetching users:", error);
        throw error;
    }
}; 