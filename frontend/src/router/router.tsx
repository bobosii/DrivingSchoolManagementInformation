import { createBrowserRouter } from "react-router-dom";
import AppLayout from "../layouts/Applayout";
import Home from "../pages/Home";
import { Login } from "../pages/Login";
import Unauthorized from "../pages/UnAuthorized";
import ProtectedRoute from "../components/ProtectedRoute";
import AdminDashboard from "../pages/AdminDashboard";
import InstructorDashboard from "../pages/InstructorDashboard";
import StudentDashboard from "../pages/StudentDashboard";
import EmployeeDashboard from "../pages/EmployeeDashboard";
import AppointmentsPage from "../pages/appointmentPage/AppointmentsPage";
import EmployeeProfile from "../pages/profile/EmployeeProfile";
import UsersPage from "../pages/UsersPage";
import VehiclesPage from "../pages/VehiclesPage";
import DocumentsPage from "../pages/DocumentsPage";
import TermsPage from "../pages/TermsPage";
import ClassroomsPage from '../pages/ClassroomsPage';
import ExamsPage from '../pages/ExamsPage';
import CoursesPage from '../pages/CoursesPage';
import CourseSessionsPage from '../pages/CourseSessionsPage';
import StudentCourseSessionsPage from '../pages/StudentCourseSessionsPage';
import StudentDocumentsPage from '../pages/StudentDocumentsPage';
import InstructorSessionsPage from '../pages/InstructorSessionsPage';

export const router = createBrowserRouter([
  {
    path: "/",
    element: <Login />
  },
  {
    path: "/",
    element: <AppLayout />,
    children: [
      { path: "", element: <Home /> },
      { path: "login", element: <Login /> },
      {
        path: "/unauthorized",
        element: <Unauthorized />,
      },
      {
        path: "/dashboard",
        element: (
          <ProtectedRoute allowedRoles={["ADMIN", "EMPLOYEE", "INSTRUCTOR", "STUDENT"]}>
            {({ role }) => {
              switch (role) {
                case "ADMIN":
                  return <AdminDashboard />;
                case "EMPLOYEE":
                  return <EmployeeDashboard />;
                case "INSTRUCTOR":
                  return <InstructorDashboard />;
                case "STUDENT":
                  return <StudentDashboard />;
                default:
                  return <Unauthorized />;
              }
            }}
          </ProtectedRoute>
        ),
      },
      {
        path: "/admin",
        element: (
          <ProtectedRoute allowedRoles={["ADMIN"]}>
            <AdminDashboard />
          </ProtectedRoute>
        ),
      },
      {
        path: "/employee",
        element: (
          <ProtectedRoute allowedRoles={["EMPLOYEE"]}>
            <EmployeeDashboard />
          </ProtectedRoute>
        ),
      },
      {
        path: "/instructor",
        element: (
          <ProtectedRoute allowedRoles={["INSTRUCTOR"]}>
            <InstructorDashboard />
          </ProtectedRoute>
        ),
      },
      {
        path: "/student",
        element: (
          <ProtectedRoute allowedRoles={["STUDENT"]}>
            <StudentDashboard />
          </ProtectedRoute>
        ),
      },
      {
        path: "/employee/profile",
        element: (
          <ProtectedRoute allowedRoles={["EMPLOYEE"]}>
            <EmployeeProfile />
          </ProtectedRoute>
        ),
      },
      {
        path: "/appointments",
        element: (
          <ProtectedRoute allowedRoles={["ADMIN", "EMPLOYEE", "INSTRUCTOR", "STUDENT"]}>
            <AppointmentsPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "/users",
        element: (
          <ProtectedRoute allowedRoles={["ADMIN", "EMPLOYEE"]}>
            <UsersPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "/vehicles",
        element: (
          <ProtectedRoute allowedRoles={["ADMIN", "INSTRUCTOR"]}>
            <VehiclesPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "/documents",
        element: (
          <ProtectedRoute allowedRoles={["ADMIN", "EMPLOYEE"]}>
            <DocumentsPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "/lessons",
        element: (
          <ProtectedRoute allowedRoles={["STUDENT", "INSTRUCTOR"]}>
            <div>Lessons Page</div>
          </ProtectedRoute>
        ),
      },
      {
        path: "/exams",
        element: (
          <ProtectedRoute allowedRoles={["ADMIN", "EMPLOYEE", "INSTRUCTOR", "STUDENT"]}>
            <ExamsPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "/profile",
        element: (
          <ProtectedRoute allowedRoles={["INSTRUCTOR", "STUDENT"]}>
            <div>Profile Page</div>
          </ProtectedRoute>
        ),
      },
      {
        path: "/terms",
        element: (
          <ProtectedRoute allowedRoles={["ADMIN", "EMPLOYEE"]}>
            <TermsPage />
          </ProtectedRoute>
        )
      },
      {
        path: "/classrooms",
        element: (
          <ProtectedRoute allowedRoles={["ADMIN", "EMPLOYEE", "INSTRUCTOR", "STUDENT"]}>
            <ClassroomsPage />
          </ProtectedRoute>
        )
      },
      {
        path: "/courses",
        element:(
        <ProtectedRoute allowedRoles={["ADMIN", "EMPLOYEE"]} >
             <CoursesPage />
             </ProtectedRoute>
        )
      },
      {
        path: "/course-sessions",
        element:(
        <ProtectedRoute allowedRoles={["ADMIN", "EMPLOYEE", "STUDENT"]} >
             <CourseSessionsPage />
             </ProtectedRoute>
        )
      },
      {
        path: "/student/course-sessions",
        element: (
          <ProtectedRoute allowedRoles={['STUDENT']}>
            <StudentCourseSessionsPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "/student/documents",
        element: (
          <ProtectedRoute allowedRoles={['STUDENT']}>
            <StudentDocumentsPage />
          </ProtectedRoute>
        ),
      },
      {
        path: "/instructor/sessions",
        element: (
          <ProtectedRoute allowedRoles={["INSTRUCTOR"]}>
            <InstructorSessionsPage />
          </ProtectedRoute>
        ),
      },
    ],
  },
]);
