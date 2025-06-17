import React, { useEffect, useState } from 'react';
import { getMyCourseSessions } from '../services/courseSessionService';
import type { CourseSession } from '../services/courseSessionService';
import { useSearch } from '../context/SearchContext';

const StudentCourseSessionsPage = () => {
    const [sessions, setSessions] = useState<CourseSession[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const { searchTerm } = useSearch();

    useEffect(() => {
        fetchSessions();
    }, []);

    const fetchSessions = async () => {
        try {
            setLoading(true);
            const data = await getMyCourseSessions();
            setSessions(data);
            setError(null);
        } catch (err) {
            setError('Kurs oturumları yüklenirken bir hata oluştu.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const formatDateTime = (dateTimeStr: string) => {
        return new Date(dateTimeStr).toLocaleString('tr-TR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    // Filtreleme fonksiyonu
    const filteredSessions = sessions.filter(session => {
        if (!searchTerm) return true;

        const searchLower = searchTerm.toLowerCase();
        return (
            session.courseName?.toLowerCase().includes(searchLower) ||
            session.instructorFullName?.toLowerCase().includes(searchLower) ||
            session.classroomName?.toLowerCase().includes(searchLower) ||
            formatDateTime(session.startTime).toLowerCase().includes(searchLower) ||
            formatDateTime(session.endTime).toLowerCase().includes(searchLower)
        );
    });

    if (loading) {
        return (
            <div className="flex justify-center items-center h-screen">
                <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="flex justify-center items-center h-screen">
                <div className="text-red-500">{error}</div>
            </div>
        );
    }

    return (
        <div className="container mx-auto px-4 py-8">
            <h1 className="text-2xl font-bold mb-6">Kurs Oturumlarım</h1>
            
            <div className="bg-white rounded-lg shadow overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Kurs
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Eğitmen
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Sınıf
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Başlangıç
                            </th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Bitiş
                            </th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {filteredSessions.map((session) => (
                            <tr key={session.id} className="hover:bg-gray-50">
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {session.courseName}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {session.instructorFullName}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {session.classroomName}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {formatDateTime(session.startTime)}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                                    {formatDateTime(session.endTime)}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default StudentCourseSessionsPage; 