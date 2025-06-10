import React, { useEffect, useState } from 'react';
import { getAllCourseSessions, createCourseSession, updateCourseSession, deleteCourseSession, activateCourseSession, deactivateCourseSession } from '../services/courseSessionService';
import type { CourseSession, CreateCourseSessionRequest } from '../services/courseSessionService';
import { getAllCourses } from '../services/courseService';
import type { Course } from '../services/courseService';
import { getAllInstructors } from '../services/instructorService';
import type { Instructor } from '../services/instructorService';
import { getAllClassrooms } from '../services/classroomService';
import type { Classroom } from '../services/classroomService';
import { Plus, Edit, Trash2, CheckCircle, XCircle } from 'lucide-react';
import { toast } from 'react-hot-toast';
import { format } from 'date-fns';
import { tr } from 'date-fns/locale';

const CourseSessionsPage = () => {
    const [sessions, setSessions] = useState<CourseSession[]>([]);
    const [courses, setCourses] = useState<Course[]>([]);
    const [instructors, setInstructors] = useState<Instructor[]>([]);
    const [classrooms, setClassrooms] = useState<Classroom[]>([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingSession, setEditingSession] = useState<CourseSession | null>(null);
    const [formData, setFormData] = useState<CreateCourseSessionRequest>({
        courseId: 0,
        instructorId: 0,
        classroomId: 0,
        startTime: '',
        endTime: '',
        maxStudents: 1
    });
    const [errors, setErrors] = useState<{ [key: string]: string }>({});
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            setIsLoading(true);
            const [sessionsData, coursesData, instructorsData, classroomsData] = await Promise.all([
                getAllCourseSessions(),
                getAllCourses(),
                getAllInstructors(),
                getAllClassrooms()
            ]);
            setSessions(sessionsData);
            setCourses(coursesData);
            setInstructors(instructorsData);
            setClassrooms(classroomsData);
        } catch (error: any) {
            console.error('Error fetching data:', error);
            toast.error(error.message || 'Veriler yüklenirken bir hata oluştu');
        } finally {
            setIsLoading(false);
        }
    };

    const validateForm = (): boolean => {
        const newErrors: { [key: string]: string } = {};
        if (!formData.courseId) newErrors.courseId = 'Kurs seçimi zorunludur';
        if (!formData.instructorId) newErrors.instructorId = 'Eğitmen seçimi zorunludur';
        if (!formData.classroomId) newErrors.classroomId = 'Sınıf seçimi zorunludur';
        if (!formData.startTime) newErrors.startTime = 'Başlangıç zamanı zorunludur';
        if (!formData.endTime) newErrors.endTime = 'Bitiş zamanı zorunludur';
        if (!formData.maxStudents || formData.maxStudents < 1) {
            newErrors.maxStudents = 'Maksimum öğrenci sayısı en az 1 olmalıdır';
        }

        const start = new Date(formData.startTime);
        const end = new Date(formData.endTime);
        if (start >= end) {
            newErrors.endTime = 'Bitiş zamanı başlangıç zamanından sonra olmalıdır';
        }

        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        const numericFields = ['courseId', 'instructorId', 'classroomId', 'maxStudents'];
        
        if (numericFields.includes(name)) {
            const numValue = value === '' ? 0 : Number(value);
            setFormData(prev => ({
                ...prev,
                [name]: numValue
            }));
        } else {
            setFormData(prev => ({
                ...prev,
                [name]: value
            }));
        }

        // Clear error when user starts typing
        if (errors[name]) {
            setErrors(prev => ({
                ...prev,
                [name]: ''
            }));
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateForm()) return;

        try {
            setIsLoading(true);
            if (editingSession) {
                await updateCourseSession(editingSession.id, formData);
                toast.success('Kurs oturumu başarıyla güncellendi');
            } else {
                await createCourseSession(formData);
                toast.success('Kurs oturumu başarıyla oluşturuldu');
            }
            setIsModalOpen(false);
            setEditingSession(null);
            setFormData({
                courseId: 0,
                instructorId: 0,
                classroomId: 0,
                startTime: '',
                endTime: '',
                maxStudents: 1
            });
            fetchData();
        } catch (error: any) {
            toast.error(error.message || 'Bir hata oluştu');
        } finally {
            setIsLoading(false);
        }
    };

    const handleEdit = (session: CourseSession) => {
        setEditingSession(session);
        setFormData({
            courseId: session.courseId,
            instructorId: session.instructorId,
            classroomId: session.classroomId,
            startTime: session.startTime,
            endTime: session.endTime,
            maxStudents: session.maxStudents
        });
        setIsModalOpen(true);
    };

    const handleDelete = async (id: number) => {
        if (window.confirm('Bu kurs oturumunu silmek istediğinizden emin misiniz?')) {
            try {
                await deleteCourseSession(id);
                toast.success('Kurs oturumu başarıyla silindi');
                fetchData();
            } catch (error) {
                console.error('Error deleting course session:', error);
                toast.error('Kurs oturumu silinirken bir hata oluştu');
            }
        }
    };

    const handleToggleStatus = async (session: CourseSession) => {
        try {
            if (session.isActive) {
                await deactivateCourseSession(session.id);
            } else {
                await activateCourseSession(session.id);
            }
            fetchData();
        } catch (error) {
            console.error('Error toggling session status:', error);
        }
    };

    const formatDateTime = (dateTimeStr: string) => {
        const date = new Date(dateTimeStr);
        return date.toLocaleString('tr-TR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const getSessionStatus = (session: CourseSession) => {
        const now = new Date();
        const startTime = new Date(session.startTime);
        return startTime > now;
    };

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Kurs Oturumları</h1>
                <button
                    onClick={() => {
                        setEditingSession(null);
                        setFormData({
                            courseId: 0,
                            instructorId: 0,
                            classroomId: 0,
                            startTime: '',
                            endTime: '',
                            maxStudents: 1
                        });
                        setErrors({});
                        setIsModalOpen(true);
                    }}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 flex items-center"
                    disabled={isLoading}
                >
                    <Plus className="w-5 h-5 mr-2" />
                    Yeni Oturum Ekle
                </button>
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
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Kurs</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Eğitmen</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Sınıf</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Başlangıç</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Bitiş</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Maksimum Öğrenci</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Durum</th>
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">İşlemler</th>
                            </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200">
                            {sessions.map((session) => (
                                <tr key={session.id}>
                                    <td className="px-6 py-4 whitespace-nowrap">{session.courseName}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">{session.instructorFullName}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">{session.classroomName}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        {format(new Date(session.startTime), 'dd MMMM yyyy HH:mm', { locale: tr })}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        {format(new Date(session.endTime), 'dd MMMM yyyy HH:mm', { locale: tr })}
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap">{session.maxStudents}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${
                                            getSessionStatus(session) ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                                        }`}>
                                            {getSessionStatus(session) ? 'Aktif' : 'Pasif'}
                                        </span>
                                    </td>
                                    <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                        <button
                                            onClick={() => handleEdit(session)}
                                            className="text-indigo-600 hover:text-indigo-900 mr-4"
                                        >
                                            Düzenle
                                        </button>
                                        <button
                                            onClick={() => handleDelete(session.id)}
                                            className="text-red-600 hover:text-red-900"
                                        >
                                            Sil
                                        </button>
                                    </td>
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
                            {editingSession ? 'Oturumu Düzenle' : 'Yeni Oturum Ekle'}
                        </h2>
                        <form onSubmit={handleSubmit}>
                            <div className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Kurs</label>
                                    <select
                                        name="courseId"
                                        value={formData.courseId}
                                        onChange={handleInputChange}
                                        className={`mt-1 block w-full rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 ${
                                            errors.courseId ? 'border-red-300' : 'border-gray-300'
                                        }`}
                                    >
                                        <option value="">Seçiniz</option>
                                        {courses.map((course) => (
                                            <option key={course.id} value={course.id}>
                                                {course.name}
                                            </option>
                                        ))}
                                    </select>
                                    {errors.courseId && (
                                        <p className="mt-1 text-sm text-red-600">{errors.courseId}</p>
                                    )}
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Eğitmen</label>
                                    <select
                                        name="instructorId"
                                        value={formData.instructorId}
                                        onChange={handleInputChange}
                                        className={`mt-1 block w-full rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 ${
                                            errors.instructorId ? 'border-red-300' : 'border-gray-300'
                                        }`}
                                    >
                                        <option value="">Seçiniz</option>
                                        {instructors.map((instructor) => (
                                            <option key={instructor.id} value={instructor.id}>
                                                {instructor.firstName} {instructor.lastName}
                                            </option>
                                        ))}
                                    </select>
                                    {errors.instructorId && (
                                        <p className="mt-1 text-sm text-red-600">{errors.instructorId}</p>
                                    )}
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Sınıf</label>
                                    <select
                                        name="classroomId"
                                        value={formData.classroomId}
                                        onChange={handleInputChange}
                                        className={`mt-1 block w-full rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 ${
                                            errors.classroomId ? 'border-red-300' : 'border-gray-300'
                                        }`}
                                    >
                                        <option value="">Seçiniz</option>
                                        {classrooms.map((classroom) => (
                                            <option key={classroom.id} value={classroom.id}>
                                                {classroom.name}
                                            </option>
                                        ))}
                                    </select>
                                    {errors.classroomId && (
                                        <p className="mt-1 text-sm text-red-600">{errors.classroomId}</p>
                                    )}
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Başlangıç Zamanı</label>
                                    <input
                                        type="datetime-local"
                                        name="startTime"
                                        value={formData.startTime}
                                        onChange={handleInputChange}
                                        className={`mt-1 block w-full rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 ${
                                            errors.startTime ? 'border-red-300' : 'border-gray-300'
                                        }`}
                                    />
                                    {errors.startTime && (
                                        <p className="mt-1 text-sm text-red-600">{errors.startTime}</p>
                                    )}
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Bitiş Zamanı</label>
                                    <input
                                        type="datetime-local"
                                        name="endTime"
                                        value={formData.endTime}
                                        onChange={handleInputChange}
                                        className={`mt-1 block w-full rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 ${
                                            errors.endTime ? 'border-red-300' : 'border-gray-300'
                                        }`}
                                    />
                                    {errors.endTime && (
                                        <p className="mt-1 text-sm text-red-600">{errors.endTime}</p>
                                    )}
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Maksimum Öğrenci Sayısı</label>
                                    <input
                                        type="number"
                                        name="maxStudents"
                                        value={formData.maxStudents}
                                        onChange={handleInputChange}
                                        min="1"
                                        className={`mt-1 block w-full rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 ${
                                            errors.maxStudents ? 'border-red-300' : 'border-gray-300'
                                        }`}
                                    />
                                    {errors.maxStudents && (
                                        <p className="mt-1 text-sm text-red-600">{errors.maxStudents}</p>
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
                                        editingSession ? 'Güncelle' : 'Ekle'
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

export default CourseSessionsPage; 