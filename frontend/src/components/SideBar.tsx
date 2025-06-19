import { Link, useNavigate, useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import {
    Home,
    Calendar,
    Users,
    Car,
    FileText,
    BookOpen,
    Settings,
    LogOut,
    User,
    ClipboardList,
    Clock,
    GraduationCap,
    BookOpenCheck
} from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { FaUsers, FaCalendarAlt, FaCar, FaFileAlt, FaChalkboardTeacher, FaGraduationCap, FaBuilding } from 'react-icons/fa';

interface MenuItem {
    icon: React.ComponentType<any>;
    label: string;
    path: string;
    roles: Array<'ADMIN' | 'STUDENT' | 'INSTRUCTOR' | 'EMPLOYEE'>;
}

const menuItems: MenuItem[] = [
    {
        icon: Home,
        label: 'Dashboard',
        path: '/admin',
        roles: ['ADMIN'],
    },
    {
        icon: Home,
        label: 'Dashboard',
        path: '/student',
        roles: ['STUDENT'],
    },
    {
        icon: Calendar,
        label: 'Randevular',
        path: '/appointments',
        roles: ['ADMIN', 'STUDENT', 'INSTRUCTOR', 'EMPLOYEE'],
    },
    {
        icon: Users,
        label: 'Kullanıcılar',
        path: '/users',
        roles: ['ADMIN','EMPLOYEE'],
    },
    {
        icon: Car,
        label: 'Araçlar',
        path: '/vehicles',
        roles: ['ADMIN', 'INSTRUCTOR'],
    },
    {
        icon: FileText,
        label: 'Belgeler',
        path: '/documents',
        roles: ['ADMIN', 'EMPLOYEE'],
    },
    {
        icon: ClipboardList,
        label: 'Sınavlar',
        path: '/exams',
        roles: ['ADMIN', 'STUDENT', 'EMPLOYEE'],
    },
    {
        icon: Clock,
        label: 'Dönemler',
        path: '/terms',
        roles: ['ADMIN', 'EMPLOYEE'],
    },
    {
        icon: FaBuilding,
        label: 'Sınıflar',
        path: '/classrooms',
        roles: ['ADMIN', 'EMPLOYEE'],
    },
    {
        icon: GraduationCap,
        label: 'Kurslar',
        path: '/courses',
        roles: ['ADMIN', 'EMPLOYEE'],
    },
    {
        icon: BookOpenCheck,
        label: 'Kurs Oturumları',
        path: '/course-sessions',
        roles: ['ADMIN', 'EMPLOYEE'],
    },
    {
        icon: BookOpenCheck,
        label: 'Kurs Oturumlarım',
        path: '/student/course-sessions',
        roles: ['STUDENT'],
    },
    {
        icon: BookOpenCheck,
        label: 'Kurs Oturumlarım',
        path: '/instructor/sessions',
        roles: ['INSTRUCTOR'],
    },
    {
        icon: FileText,
        label: 'Belgelerim',
        path: '/student/documents',
        roles: ['STUDENT'],
    },
];

const Sidebar = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [userRole, setUserRole] = useState<string | null>(null);
    const [userName, setUserName] = useState<string>('');
    const { logout } = useAuth();

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            try {
                const decoded = JSON.parse(atob(token.split(".")[1]));
                setUserRole(decoded.role);
                setUserName(decoded.name || '');
            } catch {
                localStorage.removeItem("token");
            }
        }
    }, []);

    const handleLogout = () => {
        logout();
    };

    const filteredMenuItems = menuItems.filter((item) =>
        userRole ? item.roles.includes(userRole as any) : false
    );

    return (
        <div className="h-full bg-gradient-to-b from-slate-900 to-slate-800 text-white w-64 flex flex-col">
            {/* Header */}
            <div className="p-6 border-b border-slate-700">
                <h1 className="text-xl font-bold text-center">
                    Sürücü Kursu
                </h1>
                <p className="text-slate-300 text-sm text-center mt-1">
                    Bilgi Yönetim Sistemi
                </p>
            </div>

            {/* User Info */}
            <div className="p-4 border-b border-slate-700">
                <div className="flex items-center space-x-3">
                    <div className="w-10 h-10 bg-gradient-to-r from-blue-400 to-blue-600 rounded-full flex items-center justify-center">
                        <User size={20} />
                    </div>
                    <div>
                        <p className="font-medium text-sm">
                            {userName}
                        </p>
                        <p className="text-slate-400 text-xs">
                            {userRole}
                        </p>
                    </div>
                </div>
            </div>

            {/* Navigation */}
            <nav className="flex-1 p-4 space-y-1">
                {filteredMenuItems.map((item) => {
                    const Icon = item.icon;
                    return (
                        <Link
                            key={item.path}
                            to={item.path}
                            className={`flex items-center space-x-3 px-3 py-2 rounded-lg transition-colors duration-200 ${
                                location.pathname === item.path
                                    ? 'bg-blue-600 text-white'
                                    : 'text-slate-300 hover:bg-slate-700 hover:text-white'
                            }`}
                        >
                            <Icon size={18} />
                            <span className="text-sm font-medium">{item.label}</span>
                        </Link>
                    );
                })}
            </nav>

            {/* Footer */}
            <div className="p-4 border-t border-slate-700 space-y-1">
                <button
                    onClick={handleLogout}
                    className="w-full flex items-center space-x-3 px-3 py-2 rounded-lg text-slate-300 hover:bg-red-600 hover:text-white transition-colors duration-200"
                >
                    <LogOut size={18} />
                    <span className="text-sm font-medium">Çıkış Yap</span>
                </button>
            </div>
        </div>
    );
};

export default Sidebar;
