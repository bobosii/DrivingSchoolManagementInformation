import { useState, useEffect } from "react";
import type { AppointmentResponse } from "../../types/appointment"
import axios from "axios";
import { getAllInstructors, type Instructor } from "../../services/instructorService";
import { getAllCourseSessions, type CourseSession } from "../../services/courseSessionService";
import { getAllAppointmentTypes, type AppointmentType } from "../../services/appointmentTypeService";
import { deleteAppointment } from '../../services/appointmentService';

type Props = {
    appointment: AppointmentResponse;
    onStatusUpdate: (appointmentId: number, newStatus: string) => Promise<void>;
    isAdmin: boolean;
    onAppointmentUpdate: () => Promise<void>;
};

const AppointmentCard = ({ appointment, onStatusUpdate, isAdmin, onAppointmentUpdate }: Props) => {
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [editForm, setEditForm] = useState({
        instructorId: appointment.instructorId,
        appointmentTypeId: appointment.appointmentTypeId,
        appointmentTime: new Date(appointment.appointmentTime).toISOString().slice(0, 16),
        status: appointment.status
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [instructors, setInstructors] = useState<Instructor[]>([]);
    const [courseSessions, setCourseSessions] = useState<CourseSession[]>([]);
    const [appointmentTypes, setAppointmentTypes] = useState<AppointmentType[]>([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [instructorsData, appointmentTypesData] = await Promise.all([
                    getAllInstructors(),
                    getAllAppointmentTypes()
                ]);
                setInstructors(instructorsData);
                setAppointmentTypes(appointmentTypesData);
            } catch (err) {
                console.error("Error fetching data:", err);
                setError("Failed to load form data");
            }
        };

        if (isEditModalOpen) {
            fetchData();
        }
    }, [isEditModalOpen]);

    const handleEdit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError(null);

        try {
            const token = localStorage.getItem("token");
            if (!token) {
                throw new Error("No token found");
            }

            const response = await axios.put(
                `http://localhost:8080/api/appointments/${appointment.id}`,
                {
                    ...editForm,
                    status: editForm.status
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );

            if (response.status === 200) {
                await onAppointmentUpdate();
                setIsEditModalOpen(false);
            }
        } catch (err: any) {
            setError(err.response?.data?.message || "Failed to update appointment");
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async () => {
        if (window.confirm('Are you sure you want to delete this appointment?')) {
            try {
                await deleteAppointment(appointment.id);
                await onAppointmentUpdate();
            } catch (error) {
                console.error('Error deleting appointment:', error);
                alert('Failed to delete appointment');
            }
        }
    };

    console.log("AppointmentCard - isAdmin:", isAdmin); // Debug için
    console.log("AppointmentCard - appointment status:", appointment.status); // Debug için
    console.log("AppointmentCard - appointment id:", appointment.id); // Debug için

    return (
        <>
            <div className="p-4 border rounded shadow-md">
                <div className="flex justify-between items-start mb-4">
                    <div>
                        <h3 className="text-lg font-semibold text-gray-800">Appointment #{appointment.id}</h3>
                        <p className="text-sm text-gray-600">Status: {appointment.status}</p>
                    </div>
                    {isAdmin && appointment.status !== 'PENDING' && (
                        <button
                            onClick={handleDelete}
                            className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition-colors"
                        >
                            Delete
                        </button>
                    )}
                </div>
                <p><strong>Student:</strong> {appointment.studentName}</p>
                <p><strong>Instructor:</strong> {appointment.instructorName}</p>
                <p><strong>Type:</strong> {appointment.appointmentTypeName}</p>
                <p><strong>Time:</strong> {new Date(appointment.appointmentTime).toLocaleString()}</p>
                
                <div className="mt-4 flex gap-2">
                    {isAdmin && appointment.status === "PENDING" && (
                        <>
                            <button
                                onClick={() => onStatusUpdate(appointment.id, "APPROVED")}
                                className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 transition-colors"
                            >
                                Approve
                            </button>
                            <button
                                onClick={() => onStatusUpdate(appointment.id, "REJECTED")}
                                className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 transition-colors"
                            >
                                Reject
                            </button>
                        </>
                    )}
                    {isAdmin && (
                        <button
                            onClick={() => setIsEditModalOpen(true)}
                            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition-colors"
                        >
                            Edit
                        </button>
                    )}
                </div>
            </div>

            {/* Edit Modal */}
            {isEditModalOpen && (
                <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg shadow-xl w-full max-w-md mx-4 p-6 relative">
                        <button
                            onClick={() => setIsEditModalOpen(false)}
                            className="absolute top-2 right-2 text-gray-400 hover:text-gray-700"
                        >
                            <svg className="w-6 h-6" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                        <h2 className="text-xl font-bold mb-4">Edit Appointment</h2>
                        <form onSubmit={handleEdit} className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Instructor</label>
                                <select
                                    value={editForm.instructorId}
                                    onChange={(e) => setEditForm({ ...editForm, instructorId: Number(e.target.value) })}
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                >
                                    <option value="">Select Instructor</option>
                                    {instructors.map((instructor) => (
                                        <option key={instructor.id} value={instructor.id}>
                                            {instructor.fullName}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Appointment Type</label>
                                <select
                                    value={editForm.appointmentTypeId}
                                    onChange={(e) => setEditForm({ ...editForm, appointmentTypeId: Number(e.target.value) })}
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                >
                                    <option value="">Select Appointment Type</option>
                                    {appointmentTypes.map((type) => (
                                        <option key={type.id} value={type.id}>
                                            {type.name}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Status</label>
                                <select
                                    value={editForm.status}
                                    onChange={(e) => setEditForm({ ...editForm, status: e.target.value })}
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                >
                                    <option value="PENDING">Pending</option>
                                    <option value="APPROVED">Approved</option>
                                    <option value="REJECTED">Rejected</option>
                                    <option value="CANCELLED">Cancelled</option>
                                </select>
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Appointment Time</label>
                                <input
                                    type="datetime-local"
                                    value={editForm.appointmentTime}
                                    onChange={(e) => setEditForm({ ...editForm, appointmentTime: e.target.value })}
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                />
                            </div>
                            {error && <div className="text-red-600 text-sm">{error}</div>}
                            <button
                                type="submit"
                                disabled={loading}
                                className="w-full bg-blue-600 text-white py-2 rounded-md font-medium hover:bg-blue-700 transition-colors disabled:opacity-50"
                            >
                                {loading ? "Updating..." : "Update"}
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </>
    );
};

export default AppointmentCard;
