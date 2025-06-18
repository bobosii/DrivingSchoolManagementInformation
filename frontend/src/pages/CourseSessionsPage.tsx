import React, { useEffect, useState } from 'react';
import { getAllCourseSessions, createCourseSession, updateCourseSession, deleteCourseSession, assignStudentToSession, removeStudentFromSession, getUnassignedStudents } from '../services/courseSessionService';
import type { CourseSession, CreateCourseSessionRequest } from '../services/courseSessionService';
import { getAllCourses } from '../services/courseService';
import type { Course } from '../services/courseService';
import { getAllInstructors } from '../services/instructorService';
import type { Instructor } from '../services/instructorService';
import { getAllClassrooms } from '../services/classroomService';
import type { Classroom } from '../services/classroomService';
import { getAllStudents } from '../services/studentService';
import type { Student } from '../services/studentService';
import { getAllTerms } from '../services/termService';
import type { Term } from '../services/termService';
import { Plus, Edit, Trash2, XCircle, Users } from 'lucide-react';
import { toast } from 'react-hot-toast';
import { format } from 'date-fns';
import { tr } from 'date-fns/locale';
import { useSearch } from '../context/SearchContext';

const CourseSessionsPage: React.FC = () => {
    const [sessions, setSessions] = useState<CourseSession[]>([]);
    const [courses, setCourses] = useState<Course[]>([]);
    const [instructors, setInstructors] = useState<Instructor[]>([]);
    const [classrooms, setClassrooms] = useState<Classroom[]>([]);
    const [terms, setTerms] = useState<Term[]>([]);
    const [students, setStudents] = useState<Student[]>([]);
    const [selectedTerm, setSelectedTerm] = useState<number | null>(null);
    const [searchTerm, setSearchTerm] = useState<string>('');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isStudentModalOpen, setIsStudentModalOpen] = useState(false);
    const [selectedSession, setSelectedSession] = useState<CourseSession | null>(null);
    const [editingSession, setEditingSession] = useState<CourseSession | null>(null);
    const [formData, setFormData] = useState<CreateCourseSessionRequest>({
        courseId: 0,
        instructorId: 0,
        classroomId: 0,
        startTime: '',
        endTime: '',
        maxStudents: 1,
    });
    const [errors, setErrors] = useState<Record<string, string>>({});
    const [isLoading, setIsLoading] = useState(false);
    const { searchTerm: globalSearch } = useSearch();

    useEffect(() => {
        fetchData();
        fetchTerms();
        fetchStudents();
    }, []);

    const fetchData = async () => {
        try {
            setIsLoading(true);
            const [sessionsData, coursesData, instructorsData, classroomsData] = await Promise.all([
                getAllCourseSessions(),
                getAllCourses(),
                getAllInstructors(),
                getAllClassrooms(),
            ]);
            setSessions(sessionsData);
            setCourses(coursesData);
            setInstructors(instructorsData);
            setClassrooms(classroomsData);
        } catch (e: any) {
            toast.error(e.message || 'Veriler yüklenirken hata oluştu');
        } finally {
            setIsLoading(false);
        }
    };

    const fetchTerms = async () => {
        try {
            setTerms(await getAllTerms());
        } catch {
            console.error('Term fetch error');
        }
    };

    const fetchStudents = async () => {
        try {
            setStudents(await getAllStudents());
        } catch {
            console.error('Students fetch error');
        }
    };

    const validateForm = (): boolean => {
        const errs: Record<string, string> = {};
        if (!formData.courseId) errs.courseId = 'Kurs seçimi zorunludur';
        if (!formData.instructorId) errs.instructorId = 'Eğitmen seçimi zorunludur';
        if (!formData.classroomId) errs.classroomId = 'Sınıf seçimi zorunludur';
        if (!formData.startTime) errs.startTime = 'Başlangıç zamanı zorunludur';
        if (!formData.endTime) errs.endTime = 'Bitiş zamanı zorunludur';
        if (formData.maxStudents < 1) errs.maxStudents = 'En az 1 öğrenci olmalıdır';
        setErrors(errs);
        return Object.keys(errs).length === 0;
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: ['courseId', 'instructorId', 'classroomId', 'maxStudents'].includes(name) ? Number(value) : value,
        }));
        if (errors[name]) setErrors(prev => ({ ...prev, [name]: '' }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!validateForm()) return;
        try {
            setIsLoading(true);
            if (editingSession) {
                await updateCourseSession(editingSession.id, formData);
                toast.success('Oturum güncellendi');
            } else {
                await createCourseSession(formData);
                toast.success('Oturum oluşturuldu');
            }
            await fetchData();
            setIsModalOpen(false);
        } catch (e: any) {
            toast.error(e.message);
        } finally {
            setIsLoading(false);
        }
    };

    const handleViewStudents = async (session: CourseSession) => {
        setSelectedSession(session);
        setIsStudentModalOpen(true);
        await Promise.all([fetchStudents(), fetchData()]);
    };

    const handleAssignStudent = async (sessionId: number, studentId: number) => {
        try {
            await assignStudentToSession(sessionId, studentId);
            toast.success('Öğrenci atandı');
            await Promise.all([fetchData(), fetchStudents()]);
            const updated = (await getAllCourseSessions()).find(s => s.id === sessionId) || null;
            setSelectedSession(updated);
        } catch (e: any) {
            toast.error(e.message);
        }
    };

    const handleRemoveStudent = async (sessionId: number, studentId: number) => {
        try {
            await removeStudentFromSession(sessionId, studentId);
            toast.success('Öğrenci kaldırıldı');
            await Promise.all([fetchData(), fetchStudents()]);
            const updated = (await getAllCourseSessions()).find(s => s.id === sessionId) || null;
            setSelectedSession(updated);
        } catch (e: any) {
            toast.error(e.message);
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
            maxStudents: session.maxStudents,
        });
        setIsModalOpen(true);
    };

    const handleDelete = async (id: number) => {
        if (!window.confirm('Bu oturumu silmek istediğinizden emin misiniz?')) return;
        try {
            await deleteCourseSession(id);
            toast.success('Oturum silindi');
            await fetchData();
        } catch (e: any) {
            toast.error(e.message);
        }
    };

    const filteredSessions = sessions.filter(s =>
        (!globalSearch || s.courseName.toLowerCase().includes(globalSearch.toLowerCase()) ||
            s.instructorFullName.toLowerCase().includes(globalSearch.toLowerCase()) ||
            s.classroomName.toLowerCase().includes(globalSearch.toLowerCase()))
    );

    const filteredUnassigned = students.filter(st =>
        (!searchTerm || st.fullName.toLowerCase().includes(searchTerm.toLowerCase())) &&
        (!selectedTerm || st.termId === selectedTerm) &&
        !selectedSession?.students?.some(s => s.id === st.id)
    );

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Kurs Oturumları</h1>
                <button
                    onClick={() => {
                        setEditingSession(null);
                        setFormData({ courseId: 0, instructorId: 0, classroomId: 0, startTime: '', endTime: '', maxStudents: 1 });
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

            <div className="bg-white rounded-lg shadow overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Kurs</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Eğitmen</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Sınıf</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Başlangıç</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Bitiş</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Öğrenci Sayısı</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">İşlemler</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {filteredSessions.map((session) => (
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
                                <td className="px-6 py-4 whitespace-nowrap">
                                    {session.students?.length || 0} / {session.maxStudents}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap space-x-2">
                                    <button
                                        onClick={() => handleViewStudents(session)}
                                        className="text-blue-600 hover:text-blue-900"
                                        title="Öğrencileri Yönet"
                                    >
                                        <Users className="w-5 h-5" />
                                    </button>
                                    <button
                                        onClick={() => handleEdit(session)}
                                        className="text-yellow-600 hover:text-yellow-900"
                                        title="Düzenle"
                                    >
                                        <Edit className="w-5 h-5" />
                                    </button>
                                    <button
                                        onClick={() => handleDelete(session.id)}
                                        className="text-red-600 hover:text-red-900"
                                        title="Sil"
                                    >
                                        <Trash2 className="w-5 h-5" />
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {isModalOpen && (
                <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded p-6 w-full max-w-2xl">
                        <div className="flex justify-between items-center mb-4">
                            <h2 className="text-xl font-semibold">
                                {editingSession ? 'Oturumu Düzenle' : 'Yeni Oturum Ekle'}
                            </h2>
                            <button onClick={() => setIsModalOpen(false)}><XCircle /></button>
                        </div>
                        <form onSubmit={handleSubmit} className="space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700">Kurs</label>
                                <select
                                    name="courseId"
                                    value={formData.courseId}
                                    onChange={handleInputChange}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                >
                                    <option value="">Seçiniz</option>
                                    {courses.map(course => (
                                        <option key={course.id} value={course.id}>{course.name}</option>
                                    ))}
                                </select>
                                {errors.courseId && <p className="mt-1 text-sm text-red-600">{errors.courseId}</p>}
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700">Eğitmen</label>
                                <select
                                    name="instructorId"
                                    value={formData.instructorId}
                                    onChange={handleInputChange}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                >
                                    <option value="">Seçiniz</option>
                                    {instructors.map(instructor => (
                                        <option key={instructor.id} value={instructor.id}>
                                            {`${instructor.firstName} ${instructor.lastName}`}
                                        </option>
                                    ))}
                                </select>
                                {errors.instructorId && <p className="mt-1 text-sm text-red-600">{errors.instructorId}</p>}
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700">Sınıf</label>
                                <select
                                    name="classroomId"
                                    value={formData.classroomId}
                                    onChange={handleInputChange}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                >
                                    <option value="">Seçiniz</option>
                                    {classrooms.map(classroom => (
                                        <option key={classroom.id} value={classroom.id}>{classroom.name}</option>
                                    ))}
                                </select>
                                {errors.classroomId && <p className="mt-1 text-sm text-red-600">{errors.classroomId}</p>}
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700">Başlangıç Zamanı</label>
                                <input
                                    type="datetime-local"
                                    name="startTime"
                                    value={formData.startTime}
                                    onChange={handleInputChange}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                />
                                {errors.startTime && <p className="mt-1 text-sm text-red-600">{errors.startTime}</p>}
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700">Bitiş Zamanı</label>
                                <input
                                    type="datetime-local"
                                    name="endTime"
                                    value={formData.endTime}
                                    onChange={handleInputChange}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                />
                                {errors.endTime && <p className="mt-1 text-sm text-red-600">{errors.endTime}</p>}
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700">Maksimum Öğrenci Sayısı</label>
                                <input
                                    type="number"
                                    name="maxStudents"
                                    value={formData.maxStudents}
                                    onChange={handleInputChange}
                                    min="1"
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                />
                                {errors.maxStudents && <p className="mt-1 text-sm text-red-600">{errors.maxStudents}</p>}
                            </div>

                            <div className="flex justify-end space-x-3">
                                <button
                                    type="button"
                                    onClick={() => setIsModalOpen(false)}
                                    className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                                >
                                    İptal
                                </button>
                                <button
                                    type="submit"
                                    className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
                                    disabled={isLoading}
                                >
                                    {isLoading ? 'Kaydediliyor...' : 'Kaydet'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {isStudentModalOpen && selectedSession && (
                <div className="fixed inset-0 bg-gray-600 bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded p-6 w-full max-w-4xl">
                        <div className="flex justify-between items-center mb-4">
                            <h2 className="text-xl font-semibold">Öğrenci Yönetimi - {selectedSession.courseName}</h2>
                            <button onClick={() => setIsStudentModalOpen(false)}><XCircle /></button>
                        </div>
                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <h3 className="font-semibold mb-2">Atanmış Öğrenciler</h3>
                                <div className="h-64 overflow-y-auto bg-gray-50 p-2 rounded">
                                    {selectedSession.students?.map(st => (
                                        <div key={st.id} className="flex justify-between items-center bg-white p-2 mb-2 rounded shadow-sm">
                                            <span>{st.fullName}</span>
                                            <button onClick={() => handleRemoveStudent(selectedSession.id, st.id)}><XCircle /></button>
                                        </div>
                                    )) || <p className="text-center text-gray-500">Henüz öğrenci yok</p>}
                                </div>
                            </div>
                            <div>
                                <h3 className="font-semibold mb-2">Atanabilecek Öğrenciler</h3>
                                <input
                                    type="text"
                                    placeholder="Öğrenci ara..."
                                    value={searchTerm}
                                    onChange={e => setSearchTerm(e.target.value)}
                                    className="w-full mb-2 p-2 border rounded"
                                />
                                <select
                                    value={selectedTerm || ''}
                                    onChange={e => setSelectedTerm(e.target.value ? Number(e.target.value) : null)}
                                    className="w-full mb-2 p-2 border rounded"
                                >
                                    <option value="">Tüm Dönemler</option>
                                    {terms.map(t => <option key={t.id} value={t.id}>{t.name}</option>)}
                                </select>
                                <div className="h-64 overflow-y-auto bg-gray-50 p-2 rounded">
                                    {filteredUnassigned.map(st => (
                                        <div key={st.id} className="flex justify-between items-center bg-white p-2 mb-2 rounded shadow-sm">
                                            <span>{st.fullName}</span>
                                            <button onClick={() => handleAssignStudent(selectedSession.id, st.id)}><Plus /></button>
                                        </div>
                                    )) || <p className="text-center text-gray-500">Atanacak öğrenci yok</p>}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CourseSessionsPage; 