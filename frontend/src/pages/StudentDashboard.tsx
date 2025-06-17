import { useEffect, useState } from 'react';
import { studentService } from '../services/studentService';
import type { StudentDashboard } from '../services/studentService';
import { Calendar, FileText, BookOpen } from 'lucide-react';

export default function StudentDashboard() {
    const [dashboardData, setDashboardData] = useState<StudentDashboard | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchDashboardData = async () => {
            try {
                const data = await studentService.getStudentDashboard();
                setDashboardData(data);
                setError(null);
            } catch (err) {
                setError('Öğrenci paneli bilgileri alınırken bir hata oluştu');
            } finally {
                setLoading(false);
            }
        };

        fetchDashboardData();
    }, []);

    if (loading) {
        return (
            <div className="p-6">
                <div className="animate-pulse">
                    <div className="h-8 bg-gray-200 rounded w-1/4 mb-4"></div>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        {[1, 2, 3].map((i) => (
                            <div key={i} className="h-32 bg-gray-200 rounded"></div>
                        ))}
                    </div>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="p-6">
                <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                    {error}
                </div>
            </div>
        );
    }

    if (!dashboardData) {
        return null;
    }

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-6">Öğrenci Paneli</h1>
            
            {/* Student Info */}
            <div className="bg-white rounded-lg shadow-sm p-6 mb-6">
                <h2 className="text-lg font-semibold mb-4">Kişisel Bilgiler</h2>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                        <p className="text-gray-600">Ad Soyad</p>
                        <p className="font-medium">{`${dashboardData.firstName} ${dashboardData.lastName}`}</p>
                    </div>
                    <div>
                        <p className="text-gray-600">E-posta</p>
                        <p className="font-medium">{dashboardData.email}</p>
                    </div>
                    <div>
                        <p className="text-gray-600">Doğum Tarihi</p>
                        <p className="font-medium">{new Date(dashboardData.birthDate).toLocaleDateString('tr-TR')}</p>
                    </div>
                    <div>
                        <p className="text-gray-600">Dönem</p>
                        <p className="font-medium">{dashboardData.termName || 'Dönem atanmamış'}</p>
                    </div>
                </div>
            </div>

            {/* Statistics */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                {/* Appointments Card */}
                <div className="bg-white rounded-lg shadow-sm p-6">
                    <div className="flex items-center justify-between mb-4">
                        <h3 className="text-lg font-semibold">Randevular</h3>
                        <Calendar className="w-6 h-6 text-red-500" />
                    </div>
                    <p className="text-3xl font-bold">{dashboardData.totalAppointments}</p>
                    <p className="text-gray-600 mt-2">Toplam Randevu</p>
                </div>

                {/* Course Sessions Card */}
                <div className="bg-white rounded-lg shadow-sm p-6">
                    <div className="flex items-center justify-between mb-4">
                        <h3 className="text-lg font-semibold">Kurs Oturumları</h3>
                        <BookOpen className="w-6 h-6 text-blue-500" />
                    </div>
                    <p className="text-3xl font-bold">{dashboardData.totalCourseSessions}</p>
                    <p className="text-gray-600 mt-2">Toplam Oturum</p>
                </div>

                {/* Documents Card */}
                <div className="bg-white rounded-lg shadow-sm p-6">
                    <div className="flex items-center justify-between mb-4">
                        <h3 className="text-lg font-semibold">Belgeler</h3>
                        <FileText className="w-6 h-6 text-green-500" />
                    </div>
                    <p className="text-3xl font-bold">{dashboardData.totalDocuments}</p>
                    <p className="text-gray-600 mt-2">Toplam Belge</p>
                </div>
            </div>
        </div>
    );
}
