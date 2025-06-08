import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import type { Classroom, CreateClassroomRequest } from '../services/classroomService';
import { createClassroom, deleteClassroom, getAllClassrooms, updateClassroom } from '../services/classroomService';
import { Plus, Edit, Trash2, Users, MapPin } from 'lucide-react';

const ClassroomsPage: React.FC = () => {
    const { userRole } = useAuth();
    const [classrooms, setClassrooms] = useState<Classroom[]>([]);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [editingClassroom, setEditingClassroom] = useState<Classroom | null>(null);
    const [formData, setFormData] = useState<CreateClassroomRequest>({
        name: '',
        capacity: 0,
        location: ''
    });

    const isAdminOrEmployee = userRole === 'ADMIN' || userRole === 'EMPLOYEE';

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const data = await getAllClassrooms();
            setClassrooms(data);
        } catch (error) {
            console.error('Sınıflar yüklenirken bir hata oluştu:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'capacity' ? parseInt(value) || 0 : value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            if (editingClassroom) {
                await updateClassroom(editingClassroom.id, formData);
                console.log('Sınıf başarıyla güncellendi');
            } else {
                await createClassroom(formData);
                console.log('Sınıf başarıyla oluşturuldu');
            }
            setShowModal(false);
            setEditingClassroom(null);
            setFormData({ name: '', capacity: 0, location: '' });
            fetchData();
        } catch (error) {
            console.error('İşlem sırasında bir hata oluştu:', error);
        }
    };

    const handleEdit = (classroom: Classroom) => {
        setEditingClassroom(classroom);
        setFormData({
            name: classroom.name,
            capacity: classroom.capacity,
            location: classroom.location
        });
        setShowModal(true);
    };

    const handleDelete = async (id: number) => {
        if (window.confirm('Bu sınıfı silmek istediğinizden emin misiniz?')) {
            try {
                await deleteClassroom(id);
                console.log('Sınıf başarıyla silindi');
                fetchData();
            } catch (error: any) {
                const errorMessage = error.response?.data?.message || 'Sınıf silinirken bir hata oluştu';
                console.error('Sınıf silinirken bir hata oluştu:', errorMessage);
                alert(errorMessage);
            }
        }
    };

    const handleCloseModal = () => {
        setShowModal(false);
        setEditingClassroom(null);
        setFormData({ name: '', capacity: 0, location: '' });
    };

    if (loading) {
        return (
            <div className="p-6">
                <div className="flex justify-center items-center h-64">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-red-500"></div>
                </div>
            </div>
        );
    }

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold text-gray-800">Sınıflar</h1>
                {isAdminOrEmployee && (
                    <button
                        onClick={() => setShowModal(true)}
                        className="bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition-colors flex items-center gap-2"
                    >
                        <Plus className="w-5 h-5" />
                        Yeni Sınıf Ekle
                    </button>
                )}
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {classrooms.map((classroom) => (
                    <div key={classroom.id} className="bg-white rounded-xl shadow-md p-6 border border-gray-100 hover:shadow-lg transition-shadow">
                        <h2 className="text-xl font-semibold text-gray-800 mb-4">{classroom.name}</h2>
                        <div className="space-y-3">
                            <div className="flex items-center text-gray-600">
                                <Users className="w-5 h-5 mr-2" />
                                <span>Kapasite: {classroom.capacity}</span>
                            </div>
                            <div className="flex items-center text-gray-600">
                                <MapPin className="w-5 h-5 mr-2" />
                                <span>Konum: {classroom.location}</span>
                            </div>
                        </div>
                        {isAdminOrEmployee && (
                            <div className="mt-6 flex justify-end space-x-3">
                                <button
                                    onClick={() => handleEdit(classroom)}
                                    className="inline-flex items-center px-3 py-2 text-sm font-medium text-white bg-blue-500 rounded-lg hover:bg-blue-600 focus:ring-4 focus:ring-blue-300 transition-colors"
                                >
                                    <Edit className="w-4 h-4 mr-2" />
                                    Düzenle
                                </button>
                                <button
                                    onClick={() => handleDelete(classroom.id)}
                                    className="inline-flex items-center px-3 py-2 text-sm font-medium text-white bg-red-500 rounded-lg hover:bg-red-600 focus:ring-4 focus:ring-red-300 transition-colors"
                                >
                                    <Trash2 className="w-4 h-4 mr-2" />
                                    Sil
                                </button>
                            </div>
                        )}
                    </div>
                ))}
            </div>

            {showModal && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-xl p-6 w-full max-w-md shadow-2xl">
                        <h2 className="text-xl font-bold text-gray-800 mb-6">
                            {editingClassroom ? 'Sınıfı Düzenle' : 'Yeni Sınıf Ekle'}
                        </h2>
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    Sınıf Adı
                                </label>
                                <input
                                    type="text"
                                    name="name"
                                    value={formData.name}
                                    onChange={handleInputChange}
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-red-500 transition-colors"
                                    required
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    Kapasite
                                </label>
                                <input
                                    type="number"
                                    name="capacity"
                                    value={formData.capacity}
                                    onChange={handleInputChange}
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-red-500 transition-colors"
                                    required
                                    min="1"
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    Konum
                                </label>
                                <input
                                    type="text"
                                    name="location"
                                    value={formData.location}
                                    onChange={handleInputChange}
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-red-500 transition-colors"
                                    required
                                />
                            </div>
                            <div className="flex justify-end space-x-3 mt-6">
                                <button
                                    type="button"
                                    onClick={handleCloseModal}
                                    className="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors"
                                >
                                    İptal
                                </button>
                                <button
                                    type="submit"
                                    className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors"
                                >
                                    {editingClassroom ? 'Güncelle' : 'Ekle'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ClassroomsPage; 