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
import { useSearch } from '../../context/SearchContext';

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
  const [courseSessions, setCourseSessions] = useState<CourseSession[]>([]);
  const [createLoading, setCreateLoading] = useState(false);
  const [createError, setCreateError] = useState<string | null>(null);
  const [createForm, setCreateForm] = useState({
    studentId: '',
    instructorId: '',
    appointmentTypeId: '',
    appointmentTime: '',
    status: 'PENDING'
  });
  const { searchTerm } = useSearch();

  // 1. Modal açıldığında verileri çek ve Student rolündeyse kendi ID'sini ata
  useEffect(() => {
    const fetchData = async () => {
      try {
        const [insData, typeData, sessionData, studData] = await Promise.all([
          getAllInstructors(),
          getAllAppointmentTypes(),
          getAllCourseSessions(),
          getAllStudents()
        ]);
        setInstructors(insData);
        setAppointmentTypes(typeData);
        setCourseSessions(sessionData);
        setStudents(studData);
      } catch (err) {
        console.error("Error fetching form data:", err);
        setError("Failed to load form data");
      }
    };

    if (isCreateModalOpen) {
      fetchData();

      if (userRole === 'STUDENT' && userId) {
        setCreateForm(prev => ({
          ...prev,
          studentId: userId.toString()
        }));
      }
    }
  }, [isCreateModalOpen, userRole, userId]);

  // 2. Randevuları yükle
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

  // 3. Yeni randevu oluştur
  const handleCreateAppointment = async (e: React.FormEvent) => {
    e.preventDefault();
    setCreateLoading(true);
    setCreateError(null);

    try {
      checkAuth();
      const token = localStorage.getItem('token');
      if (!token) throw new Error('No token found');

      const payload = {
        studentId: userRole === 'STUDENT' && userId ? userId : parseInt(createForm.studentId),
        instructorId: parseInt(createForm.instructorId),
        appointmentTypeId: parseInt(createForm.appointmentTypeId),
        appointmentTime: createForm.appointmentTime,
        status: 'PENDING',
        courseSessionId: null
      };

      console.log("Creating appointment with payload:", payload);

      const response = await fetch('http://localhost:8080/api/appointments/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(payload)
      });

      const text = await response.text();
      if (!response.ok) throw new Error(text || 'Failed to create appointment');
      const data = JSON.parse(text);

      setCreateForm({
        studentId: '',
        instructorId: '',
        appointmentTypeId: '',
        appointmentTime: '',
        status: 'PENDING'
      });
      setIsCreateModalOpen(false);
      await fetchAppointments();
    } catch (err: any) {
      console.error("Error creating appointment:", err);
      setCreateError(err.message || 'An error occurred');
    } finally {
      setCreateLoading(false);
    }
  };

  // 4. Status güncelleme (admin)
  const handleStatusUpdate = async (appointmentId: number, newStatus: string) => {
    try {
      setLoading(true);
      setError(null);
      checkAuth();
      const token = localStorage.getItem("token");
      if (!token) throw new Error("No token found");
      if (!isAdmin || userRole !== 'ADMIN') throw new Error("Only admins can update status");
      if (!userId) throw new Error("User ID missing");

      let endpoint = "";
      let payload: any = { appointmentId, approverId: userId };
      if (newStatus === "APPROVED") endpoint = "/api/appointments/approve";
      else if (newStatus === "REJECTED") endpoint = "/api/appointments/reject";
      else throw new Error("Invalid status");

      const res = await axios.put(`http://localhost:8080${endpoint}`, payload, {
        headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' }
      });
      if (res.status === 200) await fetchAppointments();
      else throw new Error("Failed to update status");
    } catch (err: any) {
      console.error("Error updating status:", err);
      if (err.response?.status === 403) setError("Permission denied. Admin only.");
      else setError(err.response?.data?.message || err.message || "Failed to update status.");
    } finally {
      setLoading(false);
    }
  };

  // 5. Filtreleme
  const filteredAppointments = appointments
    .filter(appt => selectedStatus === "ALL" || appt.status === selectedStatus)
    .filter(appt =>
      !searchTerm ||
      appt.studentName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      appt.instructorName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      appt.appointmentTypeName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
      String(appt.id).includes(searchTerm)
    );

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
            onChange={e => setSelectedStatus(e.target.value)}
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
        {filteredAppointments.length ? (
          filteredAppointments.map(appt => (
            <AppointmentCard
              key={appt.id}
              appointment={appt}
              onStatusUpdate={handleStatusUpdate}
              isAdmin={isAdmin}
              onAppointmentUpdate={fetchAppointments}
            />
          ))
        ) : (
          !loading && <p>No appointments found.</p>
        )}
      </div>

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
              {userRole !== 'STUDENT' ? (
                <div>
                  <label className="block text-sm font-medium text-gray-700">Student</label>
                  <select
                    name="studentId"
                    value={createForm.studentId}
                    onChange={e => setCreateForm({ ...createForm, studentId: e.target.value })}
                    required
                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                  >
                    <option value="">Select Student</option>
                    {students.map(s => (
                      <option key={s.id} value={s.id}>{s.fullName}</option>
                    ))}
                  </select>
                </div>
              ) : (
                <div>
                  <label className="block text-sm font-medium text-gray-700">Student</label>
                  <input
                    type="text"
                    value={students.find(s => s.id === Number(createForm.studentId))?.fullName || 'Yourself'}
                    disabled
                    className="mt-1 block w-full border border-gray-300 rounded-md p-2 bg-gray-100"
                  />
                  <input type="hidden" name="studentId" value={createForm.studentId} />
                </div>
              )}

              <div>
                <label className="block text-sm font-medium text-gray-700">Instructor</label>
                <select
                  name="instructorId"
                  value={createForm.instructorId}
                  onChange={e => setCreateForm({ ...createForm, instructorId: e.target.value })}
                  required
                  className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                >
                  <option value="">Select Instructor</option>
                  {instructors.map(i => (
                    <option key={i.id} value={i.id}>{i.firstName} {i.lastName}</option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Appointment Type</label>
                <select
                  name="appointmentTypeId"
                  value={createForm.appointmentTypeId}
                  onChange={e => setCreateForm({ ...createForm, appointmentTypeId: e.target.value })}
                  required
                  className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                >
                  <option value="">Select Appointment Type</option>
                  {appointmentTypes.map(t => (
                    <option key={t.id} value={t.id}>{t.name}</option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">Appointment Time</label>
                <input
                  type="datetime-local"
                  name="appointmentTime"
                  value={createForm.appointmentTime}
                  onChange={e => setCreateForm({ ...createForm, appointmentTime: e.target.value })}
                  required
                  className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                />
              </div>

              {createError && <div className="text-red-600 text-sm">{createError}</div>}

              <button
                type="submit"
                disabled={createLoading}
                className="w-full bg-red-600 text-white py-2 rounded-md hover:bg-red-700 transition-colors disabled:opacity-50"
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
