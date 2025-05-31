import type { AppointmentResponse } from "../../types/appointment"

type Props = {
    appointment: AppointmentResponse;
};

const AppointmentCard = ({ appointment }: Props) => {
    return (
        <div className="p-4 border rounded shadow-md">
            <p><strong>Student:</strong> {appointment.studentName}</p>
            <p><strong>Instructor:</strong> {appointment.instructorName}</p>
            <p><strong>Course:</strong> {appointment.courseName}</p>
            <p><strong>Type:</strong> {appointment.appointmentTypeName}</p>
            <p><strong>Status:</strong> {appointment.status}</p>
            <p><strong>Time:</strong> {new Date(appointment.appointmentTime).toLocaleString()}</p>
        </div>
    );
};

export default AppointmentCard;
