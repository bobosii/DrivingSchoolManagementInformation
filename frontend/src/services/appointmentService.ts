import axios from "axios";
import type { AppointmentResponse } from "../types/appointment";

export const getAllAppointments = async (): Promise<AppointmentResponse[]> => {
    const token = localStorage.getItem("token");
    const res = await axios.get("http://localhost:8080/api/appointments/all", {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });
    return res.data.data;
};
