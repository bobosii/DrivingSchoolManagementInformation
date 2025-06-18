import React, { useEffect, useState } from 'react';
import { getInstructorCourseSessions } from '../services/courseSessionService';
import type { CourseSession } from '../services/courseSessionService';
import { format } from 'date-fns';
import { tr } from 'date-fns/locale';
import { Calendar, Clock, Users, MapPin, BookOpen } from 'lucide-react';
import { toast } from 'react-hot-toast';

const InstructorSessionsPage: React.FC = () => {
    const [sessions, setSessions] = useState<CourseSession[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchSessions = async () => {
            try {
                setLoading(true);
                const data = await getInstructorCourseSessions();
                setSessions(data);
                setError(null);
            } catch (err: any) {
                console.error('Error fetching sessions:', err);
                setError(err.message || 'Kurs oturumları yüklenirken bir hata oluştu');
                toast.error(err.message || 'Kurs oturumları yüklenirken bir hata oluştu');
            } finally {
                setLoading(false);
            }
        };

        fetchSessions();
    }, []);

    const getStatusColor = (active: boolean) => {
        return active ? 'text-green-600 bg-green-100' : 'text-red-600 bg-red-100';
    };

    const getStatusText = (active: boolean) => {
        return active ? 'Aktif' : 'Pasif';
    };

    if (loading) {
        return (
            <div className="flex justify-center items-center h-64">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
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
            <div className="mb-6">
                <h1 className="text-2xl font-bold text-gray-900">Kurs Oturumlarım</h1>
                <p className="text-gray-600 mt-2">Size atanan kurs oturumlarını görüntüleyin</p>
            </div>

            {sessions.length === 0 ? (
                <div className="text-center py-12">
                    <BookOpen className="mx-auto h-12 w-12 text-gray-400" />
                    <h3 className="mt-2 text-sm font-medium text-gray-900">Henüz kurs oturumu yok</h3>
                    <p className="mt-1 text-sm text-gray-500">
                        Size henüz bir kurs oturumu atanmamış.
                    </p>
                </div>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {sessions.map((session) => (
                        <div key={session.id} className="bg-white rounded-lg shadow-md border border-gray-200 overflow-hidden">
                            <div className="p-6">
                                <div className="flex items-center justify-between mb-4">
                                    <h3 className="text-lg font-semibold text-gray-900 truncate">
                                        {session.courseName}
                                    </h3>
                                    <span className={`px-2 py-1 text-xs font-medium rounded-full ${getStatusColor(session.active)}`}>
                                        {getStatusText(session.active)}
                                    </span>
                                </div>

                                <div className="space-y-3">
                                    <div className="flex items-center text-sm text-gray-600">
                                        <Calendar className="h-4 w-4 mr-2 text-gray-400" />
                                        <span>
                                            {format(new Date(session.startTime), 'dd MMMM yyyy', { locale: tr })}
                                        </span>
                                    </div>

                                    <div className="flex items-center text-sm text-gray-600">
                                        <Clock className="h-4 w-4 mr-2 text-gray-400" />
                                        <span>
                                            {format(new Date(session.startTime), 'HH:mm', { locale: tr })} - {format(new Date(session.endTime), 'HH:mm', { locale: tr })}
                                        </span>
                                    </div>

                                    <div className="flex items-center text-sm text-gray-600">
                                        <MapPin className="h-4 w-4 mr-2 text-gray-400" />
                                        <span>{session.classroomName}</span>
                                    </div>

                                    <div className="flex items-center text-sm text-gray-600">
                                        <Users className="h-4 w-4 mr-2 text-gray-400" />
                                        <span>
                                            {session.students?.length || 0} / {session.maxStudents} öğrenci
                                        </span>
                                    </div>
                                </div>

                                {session.students && session.students.length > 0 && (
                                    <div className="mt-4 pt-4 border-t border-gray-200">
                                        <h4 className="text-sm font-medium text-gray-900 mb-2">Atanan Öğrenciler:</h4>
                                        <div className="space-y-1">
                                            {session.students.map((student) => (
                                                <div key={student.id} className="text-sm text-gray-600">
                                                    • {student.fullName}
                                                </div>
                                            ))}
                                        </div>
                                    </div>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default InstructorSessionsPage; 