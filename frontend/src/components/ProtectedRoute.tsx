import { Navigate } from "react-router-dom";
import type { ReactNode } from "react";

interface ProtectedRouteProps {
    children: ReactNode | ((props: { role: string }) => ReactNode);
    allowedRoles: string[];
}

const ProtectedRoute = ({ children, allowedRoles }: ProtectedRouteProps) => {
    const token = localStorage.getItem("token");

    if (!token) {
        return <Navigate to={"/login"} replace />;
    }

    try {
        const decodedToken = JSON.parse(atob(token.split(".")[1]));
        const userRole = decodedToken.role;

        if (!allowedRoles.includes(userRole)) {
            return <Navigate to={"/unauthorized"} replace />;
        }

        if (typeof children === "function") {
            return <>{children({ role: userRole })}</>;
        }

        return <>{children}</>;
    } catch (err) {
        console.log(err);
        localStorage.removeItem("token");
        return <Navigate to={"/login"} replace />;
    }
};

export default ProtectedRoute;
