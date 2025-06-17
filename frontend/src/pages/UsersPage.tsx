import { useEffect, useState } from "react";
import { getAllUsers, searchUsers } from "../services/userService";
import type { User } from "../services/userService";
import type { StudentDetail } from "../types/StudentDetail";
import { useSearch } from "../context/SearchContext";
import { getAllTerms } from '../services/termService';
import type { Term } from '../services/termService';
import { Edit, Trash2, Eye } from 'lucide-react';
import axios from "../api/axios";

export default function UsersPage() {
    const [users, setUsers] = useState<User[]>([]);
    const [terms, setTerms] = useState<Term[]>([]);
    const [filteredUsers, setFilteredUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [selectedRole, setSelectedRole] = useState<string>("ALL");
    const [selectedUser, setSelectedUser] = useState<User | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [studentDetail, setStudentDetail] = useState<StudentDetail | null>(null);
    const [loadingDetail, setLoadingDetail] = useState(false);
    const [isAddModalOpen, setIsAddModalOpen] = useState(false);
    const [addForm, setAddForm] = useState({ username: "", password: "", fullName: "", role: "STUDENT", firstName: "", lastName: "", email: "", birthDate: "", department: "", certificationNo: "" });
    const [addLoading, setAddLoading] = useState(false);
    const [addError, setAddError] = useState<string | null>(null);
    const [addSuccess, setAddSuccess] = useState<string | null>(null);
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [editForm, setEditForm] = useState<any>(null);
    const [editLoading, setEditLoading] = useState(false);
    const [editError, setEditError] = useState<string | null>(null);
    const [deleteLoading, setDeleteLoading] = useState<number | null>(null);
    const { searchTerm } = useSearch();

    useEffect(() => {
        fetchData();
    }, []);

    useEffect(() => {
        const filtered = searchUsers(users, searchTerm);
        setFilteredUsers(filtered);
    }, [searchTerm, users]);

    const fetchData = async () => {
        try {
            const [usersData, termsData] = await Promise.all([
                getAllUsers(),
                getAllTerms()
            ]);
            setUsers(usersData);
            setFilteredUsers(usersData);
            setTerms(termsData);
            setLoading(false);
        } catch (error) {
            console.error('Error fetching data:', error);
            setError('Veriler alınırken bir hata oluştu');
            setLoading(false);
        }
    };

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

    const handleAddInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setAddForm({ ...addForm, [e.target.name]: e.target.value });
    };

    const handleAddUser = async (e: React.FormEvent) => {
        e.preventDefault();
        setAddLoading(true);
        setAddError(null);
        setAddSuccess(null);
        try {
            const token = localStorage.getItem("token");
            // fullName is not used in backend anymore, but kept for compatibility
            const payload = { ...addForm };
            const response = await axios.post(
                "http://localhost:8080/api/admin/users",
                payload,
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setAddSuccess("User created successfully.");
            setIsAddModalOpen(false);
            setAddForm({ username: "", password: "", fullName: "", role: "STUDENT", firstName: "", lastName: "", email: "", birthDate: "", department: "", certificationNo: "" });
            // Refresh user list
            const data = await getAllUsers();
            setUsers(data);
            setFilteredUsers(selectedRole === "ALL" ? data : data.filter(user => user.role === selectedRole));
        } catch (err: any) {
            setAddError(err.response?.data?.message || "Failed to create user.");
        } finally {
            setAddLoading(false);
        }
    };

    const openEditModal = (user: User) => {
        setEditForm({
            id: user.id,
            username: user.username,
            password: "",
            firstName: user.firstName || "",
            lastName: user.lastName || "",
            email: user.email || "",
            birthDate: user.birthDate || "",
            role: user.role,
            department: user.department || "",
            certificationNo: user.certificationNo || ""
        });
        setIsEditModalOpen(true);
        setEditError(null);
    };

    const handleEditInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setEditForm({ ...editForm, [e.target.name]: e.target.value });
    };

    const handleEditUser = async (e: React.FormEvent) => {
        e.preventDefault();
        setEditLoading(true);
        setEditError(null);
        try {
            const token = localStorage.getItem("token");
            const payload = { ...editForm };
            delete payload.id;
            const response = await axios.put(
                `http://localhost:8080/api/admin/users/${editForm.id}`,
                payload,
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setIsEditModalOpen(false);
            // Refresh user list
            const data = await getAllUsers();
            setUsers(data);
            setFilteredUsers(selectedRole === "ALL" ? data : data.filter(user => user.role === selectedRole));
        } catch (err: any) {
            setEditError(err.response?.data?.message || "Failed to update user.");
        } finally {
            setEditLoading(false);
        }
    };

    const handleDeleteUser = async (userId: number) => {
        if (!window.confirm("Bu kullanıcıyı silmek istediğinize emin misiniz?")) return;
        setDeleteLoading(userId);
        try {
            const token = localStorage.getItem("token");
            await axios.delete(`http://localhost:8080/api/admin/users/${userId}`, {
                headers: { Authorization: `Bearer ${token}` }
            });
            // Refresh user list
            const data = await getAllUsers();
            setUsers(data);
            setFilteredUsers(selectedRole === "ALL" ? data : data.filter(user => user.role === selectedRole));
        } catch (err) {
            alert("Kullanıcı silinirken bir hata oluştu.");
        } finally {
            setDeleteLoading(null);
        }
    };

    const getTermName = (termId: number | null) => {
        if (!termId) return 'Atanmamış';
        const term = terms.find(t => t.id === termId);
        return term ? term.name : 'Atanmamış';
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
        <div className="container mx-auto px-4 py-8">
            <div className="flex flex-col md:flex-row md:justify-between md:items-center mb-6 gap-4">
                <h1 className="text-2xl font-bold">Users</h1>
                <div className="flex gap-4">
                    <select
                        value={selectedRole}
                        onChange={(e) => setSelectedRole(e.target.value)}
                        className="p-2 border rounded"
                    >
                        <option value="ALL">Tüm Roller</option>
                        <option value="STUDENT">Öğrenci</option>
                        <option value="INSTRUCTOR">Eğitmen</option>
                        <option value="EMPLOYEE">Personel</option>
                    </select>
                    <button
                        onClick={() => setIsAddModalOpen(true)}
                        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                    >
                        Yeni Kullanıcı Ekle
                    </button>
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
                                Term
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
                                <td className="px-6 py-4 whitespace-nowrap">
                                    {user.role === 'STUDENT' && user.termId ? getTermName(user.termId) : '-'}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                                    <button
                                        onClick={() => handleViewDetails(user)}
                                        className="text-red-600 hover:text-red-900 bg-red-50 hover:bg-red-100 px-3 py-1 rounded-md text-sm font-medium transition-colors duration-200 mr-2"
                                    >
                                        Detayları Göster
                                    </button>
                                    <button
                                        onClick={() => openEditModal(user)}
                                        className="text-blue-600 hover:text-blue-900 bg-blue-50 hover:bg-blue-100 px-3 py-1 rounded-md text-sm font-medium transition-colors duration-200 mr-2"
                                    >
                                        Düzenle
                                    </button>
                                    <button
                                        onClick={() => handleDeleteUser(user.id)}
                                        disabled={deleteLoading === user.id}
                                        className="text-white bg-red-600 hover:bg-red-700 px-3 py-1 rounded-md text-sm font-medium transition-colors duration-200 disabled:opacity-50"
                                    >
                                        {deleteLoading === user.id ? "Siliniyor..." : "Sil"}
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
                                                <p className="mt-1 text-sm text-gray-900">{getTermName(studentDetail.termId)}</p>
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

            {/* Add User Modal */}
            {isAddModalOpen && (
                <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg shadow-xl w-full max-w-md mx-4 p-6 relative">
                        <button
                            onClick={() => setIsAddModalOpen(false)}
                            className="absolute top-2 right-2 text-gray-400 hover:text-gray-700"
                        >
                            <svg className="w-6 h-6" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
                            </svg>
                        </button>
                        <h2 className="text-xl font-bold mb-4">Kullanıcı Ekle</h2>
                        <form onSubmit={handleAddUser} className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Kullanıcı Adı</label>
                                <input
                                    type="text"
                                    name="username"
                                    value={addForm.username}
                                    onChange={handleAddInputChange}
                                    required
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Şifre</label>
                                <input
                                    type="password"
                                    name="password"
                                    value={addForm.password}
                                    onChange={handleAddInputChange}
                                    required
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                />
                            </div>
                            <div className="grid grid-cols-2 gap-2">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Ad</label>
                                    <input
                                        type="text"
                                        name="firstName"
                                        value={addForm.firstName}
                                        onChange={handleAddInputChange}
                                        required={addForm.role !== "ADMIN"}
                                        className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Soyad</label>
                                    <input
                                        type="text"
                                        name="lastName"
                                        value={addForm.lastName}
                                        onChange={handleAddInputChange}
                                        required={addForm.role !== "ADMIN"}
                                        className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                    />
                                </div>
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">E-posta</label>
                                <input
                                    type="email"
                                    name="email"
                                    value={addForm.email}
                                    onChange={handleAddInputChange}
                                    required={addForm.role !== "ADMIN"}
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Doğum Tarihi</label>
                                <input
                                    type="date"
                                    name="birthDate"
                                    value={addForm.birthDate}
                                    onChange={handleAddInputChange}
                                    required={addForm.role !== "ADMIN"}
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Rol</label>
                                <select
                                    name="role"
                                    value={addForm.role}
                                    onChange={handleAddInputChange}
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                >
                                    <option value="ADMIN">Admin</option>
                                    <option value="EMPLOYEE">Employee</option>
                                    <option value="INSTRUCTOR">Instructor</option>
                                    <option value="STUDENT">Student</option>
                                </select>
                            </div>
                            {addForm.role === "EMPLOYEE" && (
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Departman</label>
                                    <input
                                        type="text"
                                        name="department"
                                        value={addForm.department}
                                        onChange={handleAddInputChange}
                                        className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                        required
                                    />
                                </div>
                            )}
                            {addForm.role === "INSTRUCTOR" && (
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Sertifika No</label>
                                    <input
                                        type="text"
                                        name="certificationNo"
                                        value={addForm.certificationNo}
                                        onChange={handleAddInputChange}
                                        className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                        required
                                    />
                                </div>
                            )}
                            {addError && <div className="text-red-600 text-sm">{addError}</div>}
                            <button
                                type="submit"
                                disabled={addLoading}
                                className="w-full bg-red-600 text-white py-2 rounded-md font-medium hover:bg-red-700 transition-colors disabled:opacity-50"
                            >
                                {addLoading ? "Ekleniyor..." : "Ekle"}
                            </button>
                        </form>
                    </div>
                </div>
            )}

            {/* Edit User Modal */}
            {isEditModalOpen && editForm && (
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
                        <h2 className="text-xl font-bold mb-4">Kullanıcıyı Düzenle</h2>
                        <form onSubmit={handleEditUser} className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Kullanıcı Adı</label>
                                <input
                                    type="text"
                                    name="username"
                                    value={editForm.username}
                                    onChange={handleEditInputChange}
                                    required
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Şifre (değiştirmek için doldurun)</label>
                                <input
                                    type="password"
                                    name="password"
                                    value={editForm.password}
                                    onChange={handleEditInputChange}
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                />
                            </div>
                            <div className="grid grid-cols-2 gap-2">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Ad</label>
                                    <input
                                        type="text"
                                        name="firstName"
                                        value={editForm.firstName}
                                        onChange={handleEditInputChange}
                                        required={editForm.role !== "ADMIN"}
                                        className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Soyad</label>
                                    <input
                                        type="text"
                                        name="lastName"
                                        value={editForm.lastName}
                                        onChange={handleEditInputChange}
                                        required={editForm.role !== "ADMIN"}
                                        className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                    />
                                </div>
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">E-posta</label>
                                <input
                                    type="email"
                                    name="email"
                                    value={editForm.email}
                                    onChange={handleEditInputChange}
                                    required={editForm.role !== "ADMIN"}
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Doğum Tarihi</label>
                                <input
                                    type="date"
                                    name="birthDate"
                                    value={editForm.birthDate}
                                    onChange={handleEditInputChange}
                                    required={editForm.role !== "ADMIN"}
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Rol</label>
                                <select
                                    name="role"
                                    value={editForm.role}
                                    onChange={handleEditInputChange}
                                    className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                >
                                    <option value="ADMIN">Admin</option>
                                    <option value="EMPLOYEE">Employee</option>
                                    <option value="INSTRUCTOR">Instructor</option>
                                    <option value="STUDENT">Student</option>
                                </select>
                            </div>
                            {editForm.role === "EMPLOYEE" && (
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Departman</label>
                                    <input
                                        type="text"
                                        name="department"
                                        value={editForm.department}
                                        onChange={handleEditInputChange}
                                        className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                        required
                                    />
                                </div>
                            )}
                            {editForm.role === "INSTRUCTOR" && (
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Sertifika No</label>
                                    <input
                                        type="text"
                                        name="certificationNo"
                                        value={editForm.certificationNo}
                                        onChange={handleEditInputChange}
                                        className="mt-1 block w-full border border-gray-300 rounded-md p-2"
                                        required
                                    />
                                </div>
                            )}
                            {editError && <div className="text-red-600 text-sm">{editError}</div>}
                            <button
                                type="submit"
                                disabled={editLoading}
                                className="w-full bg-blue-600 text-white py-2 rounded-md font-medium hover:bg-blue-700 transition-colors disabled:opacity-50"
                            >
                                {editLoading ? "Güncelleniyor..." : "Kaydet"}
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}
