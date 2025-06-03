import { useEffect, useState } from "react";
import { getAllUsers } from "../services/userService";
import type { User } from "../types/user";
import axios from "axios";
import type { StudentDetail } from "../types/StudentDetail";

export default function UsersPage() {
    const [users, setUsers] = useState<User[]>([]);
    const [filteredUsers, setFilteredUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [selectedRole, setSelectedRole] = useState<string>("ALL");
    const [selectedUser, setSelectedUser] = useState<User | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [studentDetail, setStudentDetail] = useState<StudentDetail | null>(null);
    const [loadingDetail, setLoadingDetail] = useState(false);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const data = await getAllUsers();
                setUsers(data);
                setFilteredUsers(data);
            } catch (err) {
                setError("Failed to load users");
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchUsers();
    }, []);

    useEffect(() => {
        if (selectedRole === "ALL") {
            setFilteredUsers(users);
        } else {
            setFilteredUsers(users.filter(user => user.role === selectedRole));
        }
    }, [selectedRole, users]);

    const handleViewDetails = async (user: User) => {
        setSelectedUser(user);
        setIsModalOpen(true);
        setLoadingDetail(true);
        setStudentDetail(null);
        setError(null);

        if (user.role === "STUDENT") {
            try {
                const token = localStorage.getItem("token");
                if (!token) {
                    throw new Error("No token found");
                }

                const response = await axios.get(`http://localhost:8080/api/student/${user.id}/details`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (response.data && response.data.data) {
                    setStudentDetail(response.data.data);
                } else {
                    throw new Error("Invalid response format");
                }
            } catch (err: any) {
                console.error("Error fetching student details:", err);
                if (err.response?.status === 403) {
                    setError("You don't have permission to view this student's details. Please make sure you are logged in with the correct role.");
                } else if (err.response?.status === 404) {
                    setError("Student not found. The student may have been removed from the system.");
                } else {
                    setError("Failed to load student details. Please try again.");
                }
            } finally {
                setLoadingDetail(false);
            }
        }
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
        setSelectedUser(null);
        setStudentDetail(null);
    };

    if (loading) {
        return (
            <div className="p-6">
                <div className="flex items-center justify-center">
                    <div className="w-8 h-8 border-4 border-red-500 border-t-transparent rounded-full animate-spin"></div>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="p-6">
                <div className="bg-red-50 border border-red-200 text-red-800 px-4 py-3 rounded">
                    {error}
                </div>
            </div>
        );
    }

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Users</h1>
                <div className="flex items-center space-x-2">
                    <label htmlFor="roleFilter" className="text-sm font-medium text-gray-700">
                        Filter by Role:
                    </label>
                    <select
                        id="roleFilter"
                        value={selectedRole}
                        onChange={(e) => setSelectedRole(e.target.value)}
                        className="mt-1 block w-48 pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-red-500 focus:border-red-500 sm:text-sm rounded-md"
                    >
                        <option value="ALL">All Roles</option>
                        <option value="ADMIN">Admin</option>
                        <option value="EMPLOYEE">Employee</option>
                        <option value="INSTRUCTOR">Instructor</option>
                        <option value="STUDENT">Student</option>
                    </select>
                </div>
            </div>
            <div className="bg-white rounded-lg shadow overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                ID
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Username
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Full Name
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Role
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Actions
                            </th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {filteredUsers.map((user) => (
                            <tr key={user.id} className="hover:bg-gray-50">
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {user.id}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                                    {user.username}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    {user.fullName}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full
                                        ${user.role === 'ADMIN' ? 'bg-purple-100 text-purple-800' :
                                        user.role === 'EMPLOYEE' ? 'bg-blue-100 text-blue-800' :
                                        user.role === 'INSTRUCTOR' ? 'bg-green-100 text-green-800' :
                                        'bg-gray-100 text-gray-800'}`}>
                                        {user.role}
                                    </span>
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    <button
                                        onClick={() => handleViewDetails(user)}
                                        className="text-red-600 hover:text-red-900 bg-red-50 hover:bg-red-100 px-3 py-1 rounded-md text-sm font-medium transition-colors duration-200"
                                    >
                                        View Details
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {/* User Details Modal */}
            {isModalOpen && selectedUser && (
                <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full flex items-center justify-center">
                    <div className="relative bg-white rounded-lg shadow-xl max-w-4xl w-full mx-4">
                        <div className="flex items-start justify-between p-4 border-b rounded-t">
                            <h3 className="text-xl font-semibold text-gray-900">
                                User Details
                            </h3>
                            <button
                                onClick={handleCloseModal}
                                className="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm p-1.5 ml-auto inline-flex items-center"
                            >
                                <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                                    <path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd" />
                                </svg>
                            </button>
                        </div>
                        <div className="p-6 space-y-4">
                            {selectedUser.role === "STUDENT" ? (
                                loadingDetail ? (
                                    <div className="flex justify-center items-center py-8">
                                        <div className="w-8 h-8 border-4 border-red-500 border-t-transparent rounded-full animate-spin"></div>
                                    </div>
                                ) : studentDetail ? (
                                    <div className="space-y-6">
                                        <div className="grid grid-cols-2 gap-4">
                                            <div>
                                                <label className="block text-sm font-medium text-gray-700">ID</label>
                                                <p className="mt-1 text-sm text-gray-900">{studentDetail.id}</p>
                                            </div>
                                            <div>
                                                <label className="block text-sm font-medium text-gray-700">Full Name</label>
                                                <p className="mt-1 text-sm text-gray-900">{studentDetail.firstName} {studentDetail.lastName}</p>
                                            </div>
                                            <div>
                                                <label className="block text-sm font-medium text-gray-700">Email</label>
                                                <p className="mt-1 text-sm text-gray-900">{studentDetail.email}</p>
                                            </div>
                                            <div>
                                                <label className="block text-sm font-medium text-gray-700">Birth Date</label>
                                                <p className="mt-1 text-sm text-gray-900">{new Date(studentDetail.birthDate).toLocaleDateString()}</p>
                                            </div>
                                            <div>
                                                <label className="block text-sm font-medium text-gray-700">Term</label>
                                                <p className="mt-1 text-sm text-gray-900">{studentDetail.termName}</p>
                                            </div>
                                        </div>

                                        <div>
                                            <h4 className="text-lg font-medium text-gray-900 mb-4">Appointments</h4>
                                            {studentDetail.appointments.length > 0 ? (
                                                <div className="bg-gray-50 rounded-lg p-4">
                                                    <div className="grid grid-cols-1 gap-4">
                                                        {studentDetail.appointments.map((appointment) => (
                                                            <div key={appointment.id} className="bg-white p-4 rounded-lg shadow">
                                                                <div className="grid grid-cols-2 gap-4">
                                                                    <div>
                                                                        <p className="text-sm font-medium text-gray-500">Course</p>
                                                                        <p className="mt-1 text-sm text-gray-900">{appointment.courseName}</p>
                                                                    </div>
                                                                    <div>
                                                                        <p className="text-sm font-medium text-gray-500">Instructor</p>
                                                                        <p className="mt-1 text-sm text-gray-900">{appointment.instructorName}</p>
                                                                    </div>
                                                                    <div>
                                                                        <p className="text-sm font-medium text-gray-500">Type</p>
                                                                        <p className="mt-1 text-sm text-gray-900">{appointment.appointmentTypeName}</p>
                                                                    </div>
                                                                    <div>
                                                                        <p className="text-sm font-medium text-gray-500">Date & Time</p>
                                                                        <p className="mt-1 text-sm text-gray-900">
                                                                            {new Date(appointment.appointmentTime).toLocaleString()}
                                                                        </p>
                                                                    </div>
                                                                    <div>
                                                                        <p className="text-sm font-medium text-gray-500">Status</p>
                                                                        <span className={`mt-1 inline-flex px-2 py-1 text-xs font-semibold rounded-full
                                                                            ${appointment.status === 'COMPLETED' ? 'bg-green-100 text-green-800' :
                                                                            appointment.status === 'CANCELLED' ? 'bg-red-100 text-red-800' :
                                                                            'bg-yellow-100 text-yellow-800'}`}>
                                                                            {appointment.status}
                                                                        </span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        ))}
                                                    </div>
                                                </div>
                                            ) : (
                                                <p className="text-sm text-gray-500">No appointments found.</p>
                                            )}
                                        </div>
                                    </div>
                                ) : (
                                    <p className="text-sm text-red-500">Failed to load student details.</p>
                                )
                            ) : (
                                <div className="grid grid-cols-2 gap-4">
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700">ID</label>
                                        <p className="mt-1 text-sm text-gray-900">{selectedUser.id}</p>
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700">Username</label>
                                        <p className="mt-1 text-sm text-gray-900">{selectedUser.username}</p>
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700">Full Name</label>
                                        <p className="mt-1 text-sm text-gray-900">{selectedUser.fullName}</p>
                                    </div>
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700">Role</label>
                                        <span className={`mt-1 inline-flex px-2 py-1 text-xs font-semibold rounded-full
                                            ${selectedUser.role === 'ADMIN' ? 'bg-purple-100 text-purple-800' :
                                            selectedUser.role === 'EMPLOYEE' ? 'bg-blue-100 text-blue-800' :
                                            selectedUser.role === 'INSTRUCTOR' ? 'bg-green-100 text-green-800' :
                                            'bg-gray-100 text-gray-800'}`}>
                                            {selectedUser.role}
                                        </span>
                                    </div>
                                </div>
                            )}
                        </div>
                        <div className="flex items-center justify-end p-4 border-t border-gray-200 rounded-b">
                            <button
                                onClick={handleCloseModal}
                                className="text-gray-500 bg-white hover:bg-gray-100 focus:ring-4 focus:outline-none focus:ring-red-300 rounded-lg border border-gray-200 text-sm font-medium px-5 py-2.5 hover:text-gray-900 focus:z-10"
                            >
                                Close
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
