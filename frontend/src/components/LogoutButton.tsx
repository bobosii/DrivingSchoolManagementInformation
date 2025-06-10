import { useNavigate } from "react-router-dom";
import axiosInstance from "../api/axios";

export default function LogoutButton() {
    const navigate = useNavigate();

    const handleLogout = () => {
        // Clear axios default headers
        delete axiosInstance.defaults.headers.common['Authorization'];
        
        // Clear local storage
        localStorage.removeItem("token");
        localStorage.removeItem("linkedEntityId");
        
        // Navigate to login page
        navigate("/login");
    };

    return (
        <button
            onClick={handleLogout}
            className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded"
        >
            Logout
        </button>
    );
}
