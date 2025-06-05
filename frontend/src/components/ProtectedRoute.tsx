import { Navigate } from "react-router-dom";
import type { ReactNode } from "react";
import { useAuth } from "../context/AuthContext";

interface ProtectedRouteProps {
    children: ReactNode | ((props: { role: string }) => ReactNode);
    allowedRoles: string[];
}

const ProtectedRoute = ({ children, allowedRoles }: ProtectedRouteProps) => {
    const { isAuthenticated, userRole } = useAuth();

    if (!isAuthenticated) {
        return <Navigate to={"/login"} replace />;
    }

    if (!userRole || !allowedRoles.includes(userRole)) {
        return <Navigate to={"/unauthorized"} replace />;
    }

    if (typeof children === "function") {
        return <>{children({ role: userRole })}</>;
    }

    return <>{children}</>;
};

export default ProtectedRoute;
