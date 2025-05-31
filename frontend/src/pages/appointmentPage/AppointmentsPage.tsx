import { useEffect, useState } from "react";
import type { AppointmentResponse } from "../../types/appointment";
import { getAllAppointments } from "../../services/appointmentService";
import AppointmentCard from "./AppointmentsCard";

const AppointmentsPage = () => {
    const [appointments, setAppointments] = useState<AppointmentResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchAppointments = async () => {
            try {
                const data = await getAllAppointments();
                setAppointments(data);
            } catch (err) {
                console.log(err);
                setError("Failed to load appointments.");
            } finally {
                setLoading(false);
            }
        };

        fetchAppointments();
    }, []);

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Appointments</h1>

            {loading && <p>Loading...</p>}
            {error && <p className="text-red-500">{error}</p>}

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {appointments?.length ? (
                    appointments.map((appt) => (
                        <AppointmentCard key={`${appt.courseSessionId}-${appt.appointmentTime}`} appointment={appt} />
                    ))
                ) : (
                    !loading && <p>No appointments found.</p>
                )}
            </div>
        </div>
    );
};

export default AppointmentsPage;

