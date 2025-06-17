import React, { useEffect, useState } from 'react';
import { getAllCourses } from '../services/courseService';
import { getAllCourseSessions, type CourseSession } from '../services/courseSessionService';
import { getAllExams } from '../services/examService';
import { getAllClassrooms } from '../services/classroomService';
import { getAllInstructors } from '../services/instructorService';
import { getAllStudents } from '../services/studentService';
import { getAllEmployees } from '../services/userService';
import { Users, BookOpen, Calendar, Building2, GraduationCap, Briefcase, UserCheck } from 'lucide-react';

const AdminDashboard = () => {
    const [stats, setStats] = useState({
        activeCourses: 0,
        activeSessions: 0,
        totalExams: 0,
        totalClassrooms: 0,
        totalInstructors: 0,
        totalEmployees: 0,
        totalStudents: 0
    });

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const [courses, sessions, exams, classrooms, instructors, students, employees] = await Promise.all([
                    getAllCourses(),
                    getAllCourseSessions(),
                    getAllExams(),
                    getAllClassrooms(),
                    getAllInstructors(),
                    getAllStudents(),
                    getAllEmployees()
                ]);

                setStats({
                    activeCourses: courses.length,
                    activeSessions: sessions.filter((session: CourseSession) => session.active).length,
                    totalExams: exams.length,
                    totalClassrooms: classrooms.length,
                    totalInstructors: instructors.length,
                    totalEmployees: employees.length,
                    totalStudents: students.length
                });
            } catch (error) {
                console.error('Error fetching stats:', error);
            }
        };

        fetchStats();
    }, []);

    const StatCard = ({ title, value, icon: Icon, color }: { title: string; value: number; icon: any; color: string }) => (
        <div className="bg-white rounded-lg shadow-md p-6">
            <div className="flex items-center justify-between">
                <div>
                    <p className="text-gray-500 text-sm">{title}</p>
                    <p className="text-2xl font-semibold mt-1">{value}</p>
                </div>
                <div className={`p-3 rounded-full ${color}`}>
                    <Icon className="w-6 h-6 text-white" />
                </div>
            </div>
        </div>
    );

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-6">Admin Dashboard</h1>
            
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <StatCard
                    title="Aktif Kurslar"
                    value={stats.activeCourses}
                    icon={BookOpen}
                    color="bg-blue-500"
                />
                <StatCard
                    title="Aktif Kurs Oturumları"
                    value={stats.activeSessions}
                    icon={Calendar}
                    color="bg-green-500"
                />
                <StatCard
                    title="Toplam Sınav"
                    value={stats.totalExams}
                    icon={GraduationCap}
                    color="bg-purple-500"
                />
                <StatCard
                    title="Toplam Sınıf"
                    value={stats.totalClassrooms}
                    icon={Building2}
                    color="bg-orange-500"
                />
                <StatCard
                    title="Toplam Eğitmen"
                    value={stats.totalInstructors}
                    icon={Users}
                    color="bg-indigo-500"
                />
                <StatCard
                    title="Toplam Çalışan"
                    value={stats.totalEmployees}
                    icon={Briefcase}
                    color="bg-red-500"
                />
                <StatCard
                    title="Toplam Öğrenci"
                    value={stats.totalStudents}
                    icon={UserCheck}
                    color="bg-teal-500"
                />
            </div>
        </div>
    );
};

export default AdminDashboard;
