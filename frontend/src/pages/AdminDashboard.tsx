import React, { useEffect, useState } from 'react';
import axios from '../api/axios';

interface DashboardStats {
    totalStudents: number;
    activeTerms: number;
    totalInstructors: number;
}

const AdminDashboard: React.FC = () => {
    const [stats, setStats] = useState<DashboardStats>({
        totalStudents: 0,
        activeTerms: 0,
        totalInstructors: 0
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const response = await axios.get('/admin/dashboard/stats');
                setStats(response.data.data);
                setLoading(false);
            } catch (error) {
                console.error('Error fetching dashboard stats:', error);
                setError('İstatistikler alınırken bir hata oluştu');
                setLoading(false);
            }
        };

        fetchStats();
    }, []);

    if (loading) {
        return (
            <div className="p-6">
                <div className="flex justify-center items-center h-64">
                    <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="p-6">
                <div className="bg-red-50 border border-red-200 text-red-800 rounded-lg p-4">
                    {error}
                </div>
            </div>
        );
    }

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Admin Dashboard</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                <div className="bg-white p-6 rounded-lg shadow">
                    <h2 className="text-lg font-semibold mb-2">Toplam Öğrenci</h2>
                    <p className="text-3xl font-bold text-blue-600">{stats.totalStudents}</p>
                </div>
                <div className="bg-white p-6 rounded-lg shadow">
                    <h2 className="text-lg font-semibold mb-2">Aktif Dönemler</h2>
                    <p className="text-3xl font-bold text-green-600">{stats.activeTerms}</p>
                </div>
                <div className="bg-white p-6 rounded-lg shadow">
                    <h2 className="text-lg font-semibold mb-2">Toplam Eğitmen</h2>
                    <p className="text-3xl font-bold text-purple-600">{stats.totalInstructors}</p>
                </div>
            </div>
        </div>
    );
};

export default AdminDashboard; 