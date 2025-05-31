import { createBrowserRouter } from 'react-router-dom';
import AppLayout from '../layouts/Applayout';
import Home from '../pages/Home';
import Login from '../pages/Login';
import Unauthorized from '../pages/UnAuthorized';
import ProtectedRoute from '../components/ProtectedRoute';
import AdminDashboard from '../pages/AdminDashboard';
import InstructorDashboard from '../pages/InstructorDashboard';
import StudentDashboard from '../pages/StudentDashboard';
import EmployeeDashboard from '../pages/EmployeeDashboard';
import AppointmentsPage from '../pages/appointmentPage/AppointmentsPage';

export const router = createBrowserRouter([
    {
        path: '/',
        element: <AppLayout />,
        children: [
            { path: '', element: <Home /> },
            { path: 'login', element: <Login /> },
            {
                path: '/unauthorized',
                element: <Unauthorized />
            },
            {
                path: '/admin',
                element: (
                    <ProtectedRoute allowedRoles={["ADMIN"]}>
                        <AdminDashboard />
                    </ProtectedRoute>
                )
            },
            {
                path: '/employee',
                element: (
                    <ProtectedRoute allowedRoles={["EMPLOYEE"]}>
                        <EmployeeDashboard />
                    </ProtectedRoute>
                )
            },
            {
                path: '/instructor',
                element: (
                    <ProtectedRoute allowedRoles={["INSTRUCTOR"]}>
                        <InstructorDashboard />
                    </ProtectedRoute>
                )
            },
            {
                path: '/student',
                element: (
                    <ProtectedRoute allowedRoles={["STUDENT"]}>
                        <StudentDashboard />
                    </ProtectedRoute>
                )
            },
            {
                path: '/appointments',
                element: (
                    <ProtectedRoute allowedRoles={["ADMIN", "EMPLOYEE"]}>
                        <AppointmentsPage />
                    </ProtectedRoute>
                )
            }
        ],
    },
]);

