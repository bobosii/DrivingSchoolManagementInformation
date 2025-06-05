import { useEffect, useState } from "react";
import type { AppointmentResponse } from "../../types/appointment";
import { getAllAppointments } from "../../services/appointmentService";
import AppointmentCard from "./AppointmentsCard";
import axios from "axios";
import { useAuth } from "../../context/AuthContext";
import { getAllInstructors, type Instructor } from "../../services/instructorService";
import { getAllAppointmentTypes, type AppointmentType } from "../../services/appointmentTypeService";
import { getAllStudents, type Student } from "../../services/studentService";
import { getAllCourseSessions, type CourseSession } from "../../services/courseSessionService";
import { Form, Select } from "antd";

const AppointmentsPage = () => {
    const [appointments, setAppointments] = useState<AppointmentResponse[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);
    const [selectedStatus, setSelectedStatus] = useState<string>("ALL");
    const { isAdmin, userId, userRole, checkAuth } = useAuth();
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
    const [students, setStudents] = useState<Student[]>([]);
    const [instructors, setInstructors] = useState<Instructor[]>([]);
    const [appointmentTypes, setAppointmentTypes] = useState<AppointmentType[]>([]);
    const [createLoading, setCreateLoading] = useState(false);
    const [createError, setCreateError] = useState<string | null>(null);
    const [courseSessions, setCourseSessions] = useState<CourseSession[]>([]);
    const [createForm, setCreateForm] = useState({
        studentId: '',
        instructorId: '',
        appointmentTypeId: '',
        appointmentTime: '',
        status: 'PENDING'
    });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [instructorsData, appointmentTypesData, courseSessionsData, studentsData] = await Promise.all([
                    getAllInstructors(),
                    getAllAppointmentTypes(),
                    getAllCourseSessions(),
                    getAllStudents()
                ]);
                console.log('Course Sessions Data:', courseSessionsData);
                console.log('Students Data:', studentsData);
                setInstructors(instructorsData);
                setAppointmentTypes(appointmentTypesData);
                setCourseSessions(courseSessionsData);
                setStudents(studentsData);
            } catch (err) {
                console.error("Error fetching data:", err);
                setError("Failed to load form data");
            }
        };

        if (isCreateModalOpen) {
            fetchData();
        }
    }, [isCreateModalOpen]);

    const fetchAppointments = async () => {
        try {
            const data = await getAllAppointments();
            setAppointments(data);
            setError(null);
        } catch (err) {
            console.error("Error fetching appointments:", err);
            setError("Failed to load appointments.");
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchAppointments();
    }, []);

    const handleCreateAppointment = async (e: React.FormEvent) => {
        e.preventDefault();
        setCreateLoading(true);
        setCreateError(null);

        try {
            const token = localStorage.getItem('token');
            if (!token) {
                throw new Error('No token found');
            }

            console.log('Sending appointment data:', {
                studentId: parseInt(createForm.studentId),
                instructorId: parseInt(createForm.instructorId),
                appointmentTypeId: parseInt(createForm.appointmentTypeId),
                appointmentTime: createForm.appointmentTime,
                status: 'PENDING',
                courseSessionId: null
            });

            const response = await fetch('http://localhost:8080/api/appointments/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({
                    studentId: parseInt(createForm.studentId),
                    instructorId: parseInt(createForm.instructorId),
                    appointmentTypeId: parseInt(createForm.appointmentTypeId),
                    appointmentTime: createForm.appointmentTime,
                    status: 'PENDING',
                    courseSessionId: null
                })
            });

            const responseText = await response.text();
            console.log('Raw response:', responseText);

            if (!response.ok) {
                throw new Error(responseText || 'Failed to create appointment');
            }

            let responseData;
            try {
                responseData = JSON.parse(responseText);
            } catch (e) {
                console.error('Error parsing response:', e);
                throw new Error('Invalid response from server');
            }

            setCreateForm({
                studentId: '',
                instructorId: '',
                appointmentTypeId: '',
                appointmentTime: '',
                status: 'PENDING'
            });
            setIsCreateModalOpen(false);
            await fetchAppointments();
        } catch (err) {
            console.error('Error creating appointment:', err);
            setCreateError(err instanceof Error ? err.message : 'An error occurred');
        } finally {
            setCreateLoading(false);
        }
    };

    const handleStatusUpdate = async (appointmentId: number, newStatus: string) => {
        try {
            setLoading(true);
            setError(null);
            
            // Token'ı kontrol et ve gerekirse yenile
            checkAuth();
            
            const token = localStorage.getItem("token");
            if (!token) {
                throw new Error("No token found");
            }

            // Admin kontrolü
            console.log("Current user role:", userRole); // Debug için
            console.log("Is admin:", isAdmin); // Debug için
            
            if (!isAdmin || userRole !== 'ADMIN') {
                throw new Error("Only admins can update appointment status");
            }

            // Debug için userId kontrolü
            console.log("Current userId:", userId);
            if (!userId) {
                throw new Error("User ID not found. Please try logging out and logging in again.");
            }

            // AppointmentId kontrolü
            console.log("AppointmentId:", appointmentId); // Debug için
            if (!appointmentId) {
                throw new Error("Appointment ID is required");
            }

            let endpoint = "";
            let payload = {};

            switch (newStatus) {
                case "APPROVED":
                    endpoint = "http://localhost:8080/api/appointments/approve";
                    payload = {
                        appointmentId: appointmentId,
                        approverId: userId
                    };
                    break;
                case "REJECTED":
                    endpoint = "http://localhost:8080/api/appointments/reject";
                    payload = {
                        appointmentId: appointmentId,
                        approverId: userId
                    };
                    break;
                default:
                    throw new Error("Invalid status");
            }

            console.log("Sending request to:", endpoint);
            console.log("With payload:", payload);
            console.log("With token:", token);

            const response = await axios.put(endpoint, payload, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.status === 200) {
                await fetchAppointments();
            } else {
                throw new Error("Failed to update appointment status");
            }
        } catch (err: any) {
            console.error("Error updating appointment status:", err);
            if (err.response?.status === 403) {
                setError("You don't have permission to update appointment status. Please make sure you are logged in as an admin.");
            } else {
                setError(err.response?.data?.message || err.message || "Failed to update appointment status.");
            }
        } finally {
            setLoading(false);
        }
    };

    const handleAppointmentUpdate = async () => {
        await fetchAppointments();
    };

    const filteredAppointments = selectedStatus === "ALL" 
        ? appointments 
        : appointments.filter(appt => appt.status === selectedStatus);

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Appointments</h1>
                <div className="flex items-center gap-4">
                    {(isAdmin || userRole === 'STUDENT') && (
                        <button
                            onClick={() => setIsCreateModalOpen(true)}
                            className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 transition-colors"
                        >
                            Create Appointment
                        </button>
                    )}
                    <select
                        value={selectedStatus}
                        onChange={(e) => setSelectedStatus(e.target.value)}
                        className="border border-gray-300 rounded-md px-4 py-2 focus:outline-none focus:ring-2 focus:ring-red-500"
                    >
                        <option value="ALL">All Statuses</option>
                        <option value="PENDING">Pending</option>
                        <option value="APPROVED">Approved</option>
                        <option value="REJECTED">Rejected</option>
                        <option value="CANCELLED">Cancelled</option>
                    </select>
                </div>
            </div>

            {loading && (
                <div className="flex justify-center items-center py-8">
                    <div className="w-8 h-8 border-4 border-red-500 border-t-transparent rounded-full animate-spin"></div>
                </div>
            )}
            
            {error && (
                <div className="bg-red-50 border border-red-200 text-red-800 px-4 py-3 rounded mb-4">
                    {error}
                </div>
            )}

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                {filteredAppointments?.length ? (
                    filteredAppointments.map((appt) => (
                        <AppointmentCard 
                            key={appt.id}
                            appointment={appt}
                            onStatusUpdate={handleStatusUpdate}
                            isAdmin={isAdmin}
                            onAppointmentUpdate={handleAppointmentUpdate}
                        />
                    ))
                ) : (
                    !loading && <p>No appointments found.</p>
                )}
            </div>

            {/* Create Appointment Modal */}
            {isCreateModalOpen && (
                <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg shadow-xl w-full max-w-md mx-4 p-6 relative">
                        <button
                            onClick={() => setIsCreateModalOpen(false)}
                            className="absolute top-2 right-2 text-gray-400 hover:text-gray-700"
                        >
                            <svg className="w-6 h-6" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                        <h2 className="text-xl font-bold mb-4">Create Appointment</h2>
                        <form onSubmit={handleCreateAppointment} className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Student</label>
                                <select
                                    name="studentId"
                                    value={createForm.studentId}
                                    onChange={(e) => setCreateForm({ ...createForm, studentId: e.target.value })}
                                    required
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                >
                                    <option value="">Select Student</option>
                                    {students.map((student) => (
                                        <option key={student.id} value={student.id}>
                                            {student.firstName} {student.lastName}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Instructor</label>
                                <select
                                    name="instructorId"
                                    value={createForm.instructorId}
                                    onChange={(e) => setCreateForm({ ...createForm, instructorId: e.target.value })}
                                    required
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                >
                                    <option value="">Select Instructor</option>
                                    {instructors.map((instructor) => (
                                        <option key={instructor.id} value={instructor.id}>
                                            {instructor.firstName} {instructor.lastName}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Appointment Type</label>
                                <select
                                    name="appointmentTypeId"
                                    value={createForm.appointmentTypeId}
                                    onChange={(e) => setCreateForm({ ...createForm, appointmentTypeId: e.target.value })}
                                    required
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
                                <label className="block text-sm font-medium text-gray-700">Appointment Time</label>
                                <input
                                    type="datetime-local"
                                    name="appointmentTime"
                                    value={createForm.appointmentTime}
                                    onChange={(e) => setCreateForm({ ...createForm, appointmentTime: e.target.value })}
                                    required
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                />
                            </div>
                            {createError && <div className="text-red-600 text-sm">{createError}</div>}
                            <button
                                type="submit"
                                disabled={createLoading}
                                className="w-full bg-red-600 text-white py-2 rounded-md font-medium hover:bg-red-700 transition-colors disabled:opacity-50"
                            >
                                {createLoading ? "Creating..." : "Create Appointment"}
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default AppointmentsPage;

