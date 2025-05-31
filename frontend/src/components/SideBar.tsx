import { Link, useNavigate, useLocation } from "react-router-dom";
import { useEffect, useState } from "react";

const Sidebar = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const [userRole, setUserRole] = useState<string | null>(null);

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (token) {
            try {
                const decoded = JSON.parse(atob(token.split(".")[1]));
                setUserRole(decoded.role);
            } catch {
                localStorage.removeItem("token");
            }
        }
    }, []);

    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/login");
    };

    const getLinkClass = (path: string) =>
        `block px-4 py-3 rounded hover:bg-blue-100 ${location.pathname === path ? "bg-blue-200 font-semibold" : ""
        }`;

    return (
        <div className="fixed top-0 left-0 h-screen w-64 bg-white border-r shadow-lg flex flex-col justify-between z-50">
            {/* Üst Logo ve Menü */}
            <div>
                <div className="text-2xl font-bold text-blue-600 p-6 border-b">Driving School</div>
                <nav className="flex flex-col gap-1 px-4 py-6">
                    {userRole === "ADMIN" && (
                        <>
                            <Link to="/admin" className={getLinkClass("/admin")}>Dashboard</Link>
                            <Link to="/appointments" className={getLinkClass("/appointments")}>Appointments</Link>
                        </>
                    )}
                    {userRole === "EMPLOYEE" && (
                        <>
                            <Link to="/employee" className={getLinkClass("/employee")}>Dashboard</Link>
                            <Link to="/appointments" className={getLinkClass("/appointments")}>Appointments</Link>
                        </>
                    )}
                    {userRole === "INSTRUCTOR" && (
                        <Link to="/instructor" className={getLinkClass("/instructor")}>Dashboard</Link>
                    )}
                    {userRole === "STUDENT" && (
                        <Link to="/student" className={getLinkClass("/student")}>Dashboard</Link>
                    )}
                </nav>
            </div>

            {/* Alt Logout Butonu */}
            <div className="p-4 border-t">
                <button
                    onClick={handleLogout}
                    className="w-full bg-red-500 text-white py-2 rounded hover:bg-red-600"
                >
                    Logout
                </button>
            </div>
        </div>
    );
};

export default Sidebar;
