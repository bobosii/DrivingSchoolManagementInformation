import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import type { LoginRequest } from "../types/LoginRequest";

export default function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError("");

        const requestData: LoginRequest = { username, password };

        try {
            const response = await axios.post("http://localhost:8080/api/auth/login", requestData);

            const { token } = response.data;
            localStorage.setItem("token", token);

            const decodedToken = JSON.parse(atob(token.split(".")[1]));
            const role = decodedToken.role;

            switch (role) {
                case "ADMIN":
                    navigate("/admin", { replace: true });
                    break;
                case "EMPLOYEE":
                    navigate("/employee", { replace: true });
                    break;
                case "INSTRUCTOR":
                    navigate("/instructor", { replace: true });
                    break;
                case "STUDENT":
                default:
                    navigate("/student", { replace: true });
                    break;
            }
        } catch (err: any) {
            if (err.response && err.response.status === 401) {
                setError("Invalid username or password.");
            } else {
                setError("An unexpected error occurred. Please try again later.");
            }
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center">
            <form onSubmit={handleSubmit} className="bg-white p-6 rounded shadow-md w-80">
                <h2 className="text-2xl font-bold mb-4">Login</h2>
                {error && <p className="text-red-500 text-sm mb-2">{error}</p>}
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    className="w-full mb-3 p-2 border rounded"
                    required
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="w-full mb-3 p-2 border rounded"
                    required
                />
                <button
                    type="submit"
                    className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600"
                >
                    Login
                </button>
            </form>
        </div>
    );
}

