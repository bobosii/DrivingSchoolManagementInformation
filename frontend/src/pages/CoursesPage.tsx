import React, { useEffect, useState } from 'react';
import { getAllCourses, createCourse, updateCourse, deleteCourse } from '../services/courseService';
import type { Course, CreateCourseRequest } from '../services/courseService';
import { useAuth } from '../context/AuthContext';
import { Plus, Edit, Trash2 } from 'lucide-react';
import { toast } from 'react-hot-toast';
import { useSearch } from '../context/SearchContext';

const CoursesPage: React.FC = () => {
    const [courses, setCourses] = useState<Course[]>([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingCourse, setEditingCourse] = useState<Course | null>(null);
    const [formData, setFormData] = useState<CreateCourseRequest>({
        name: '',
        courseType: ''
    });
    const [errors, setErrors] = useState<Partial<CreateCourseRequest>>({});
    const [isLoading, setIsLoading] = useState(false);
    const { userRole } = useAuth();
    const { searchTerm } = useSearch();

    useEffect(() => {
        fetchCourses();
    }, []);

    const fetchCourses = async () => {
        try {
            setIsLoading(true);
            const data = await getAllCourses();
            setCourses(data);
        } catch (error: any) {
            toast.error(error.message);
        } finally {
            setIsLoading(false);
        }
    };

    const validateForm = (): boolean => {
        const newErrors: Partial<CreateCourseRequest> = {};
        if (!formData.name.trim()) {
            newErrors.name = 'Kurs adı zorunludur';
        }
        if (!formData.courseType) {
            newErrors.courseType = 'Kurs tipi zorunludur';
        }
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
        // Clear error when user starts typing
        if (errors[name as keyof CreateCourseRequest]) {
            setErrors(prev => ({
                ...prev,
                [name]: undefined
            }));
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateForm()) return;

        try {
            setIsLoading(true);
            if (editingCourse) {
                await updateCourse(editingCourse.id, formData);
                toast.success('Kurs başarıyla güncellendi');
            } else {
                await createCourse(formData);
                toast.success('Kurs başarıyla oluşturuldu');
            }
            setIsModalOpen(false);
            setEditingCourse(null);
            setFormData({ name: '', courseType: '' });
            fetchCourses();
        } catch (error: any) {
            toast.error(error.message);
        } finally {
            setIsLoading(false);
        }
    };

    const handleEdit = (course: Course) => {
        setEditingCourse(course);
        setFormData({
            name: course.name,
            courseType: course.courseType
        });
        setIsModalOpen(true);
    };

    const handleDelete = async (id: number) => {
        if (window.confirm('Bu kursu silmek istediğinizden emin misiniz?')) {
            try {
                setIsLoading(true);
                await deleteCourse(id);
                toast.success('Kurs başarıyla silindi');
                fetchCourses();
            } catch (error: any) {
                toast.error(error.message);
            } finally {
                setIsLoading(false);
            }
        }
    };

    const canEdit = userRole === 'ADMIN' || userRole === 'EMPLOYEE';

    // Kursları filtrele
    const filteredCourses = courses.filter(course =>
        !searchTerm ||
        course.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        course.courseType?.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Kurslar</h1>
                {canEdit && (
                    <button
                        onClick={() => {
                            setEditingCourse(null);
                            setFormData({ name: '', courseType: '' });
                            setErrors({});
                            setIsModalOpen(true);
                        }}
                        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 flex items-center"
                        disabled={isLoading}
                    >
                        <Plus className="w-5 h-5 mr-2" />
                        Yeni Kurs Ekle
                    </button>
                )}
            </div>

            {isLoading && !isModalOpen ? (
                <div className="flex justify-center items-center h-64">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
                </div>
            ) : (
                <div className="bg-white rounded-lg shadow overflow-hidden">
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                            <tr>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Kurs Adı</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Kurs Tipi</th>
                                {canEdit && (
                                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">İşlemler</th>
                                )}
                            </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                            {filteredCourses.map((course) => (
                                <tr key={course.id}>
                                    <td className="px-6 py-4 whitespace-nowrap">{course.name}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">{course.courseType}</td>
                                    {canEdit && (
                                        <td className="px-6 py-4 whitespace-nowrap">
                                            <button
                                                onClick={() => handleEdit(course)}
                                                className="text-blue-600 hover:text-blue-900 mr-3"
                                                disabled={isLoading}
                                            >
                                                <Edit className="w-5 h-5" />
                                            </button>
                                            <button
                                                onClick={() => handleDelete(course.id)}
                                                className="text-red-600 hover:text-red-900"
                                                disabled={isLoading}
                                            >
                                                <Trash2 className="w-5 h-5" />
                                            </button>
                                        </td>
                                    )}
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}

            {isModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <h2 className="text-xl font-bold mb-4">
                            {editingCourse ? 'Kursu Düzenle' : 'Yeni Kurs Ekle'}
                        </h2>
                        <form onSubmit={handleSubmit}>
                            <div className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Kurs Adı</label>
                                    <input
                                        type="text"
                                        name="name"
                                        value={formData.name}
                                        onChange={handleInputChange}
                                        className={`mt-1 block w-full rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 ${
                                            errors.name ? 'border-red-300' : 'border-gray-300'
                                        }`}
                                    />
                                    {errors.name && (
                                        <p className="mt-1 text-sm text-red-600">{errors.name}</p>
                                    )}
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Kurs Tipi</label>
                                    <select
                                        name="courseType"
                                        value={formData.courseType}
                                        onChange={handleInputChange}
                                        className={`mt-1 block w-full rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 ${
                                            errors.courseType ? 'border-red-300' : 'border-gray-300'
                                        }`}
                                    >
                                        <option value="">Seçiniz</option>
                                        <option value="THEORETICAL">Teorik</option>
                                        <option value="SIMULATION">Simülasyon</option>
                                        <option value="DRIVING">Direksiyon</option>
                                    </select>
                                    {errors.courseType && (
                                        <p className="mt-1 text-sm text-red-600">{errors.courseType}</p>
                                    )}
                                </div>
                            </div>
                            <div className="mt-6 flex justify-end space-x-3">
                                <button
                                    type="button"
                                    onClick={() => setIsModalOpen(false)}
                                    className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                                    disabled={isLoading}
                                >
                                    İptal
                                </button>
                                <button
                                    type="submit"
                                    className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 disabled:opacity-50"
                                    disabled={isLoading}
                                >
                                    {isLoading ? (
                                        <span className="flex items-center">
                                            <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                                                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                                                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                                            </svg>
                                            İşleniyor...
                                        </span>
                                    ) : (
                                        editingCourse ? 'Güncelle' : 'Ekle'
                                    )}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CoursesPage; 