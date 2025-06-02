import { useEffect, useState } from "react";
import axios from "axios";

interface EmployeeProfile {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    department: string;
    role: string;
    username: string;
}

export default function EmployeeProfile() {
    const [profile, setProfile] = useState<EmployeeProfile | null>(null);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            setError("Not authenticated");
            return;
        }

        axios.get("http://localhost:8080/api/employee/profile", {
            headers: {
                Authorization: `Bearer ${token}`
            }
        })
        .then(res => {
            if (res.data.success) {
                setProfile(res.data.data);
            } else {
                setError("Failed to load profile");
            }
        })
        .catch(err => {
            console.error("Error fetching profile:", err);
            setError("Profile could not be loaded.");
        });
    }, []);

    if (error) return <div className="text-red-500 p-4">{error}</div>;
    if (!profile) return <div className="p-4">Loading...</div>;

    return (
        <div className="max-w-2xl mx-auto bg-white rounded-lg shadow-md p-6">
            <h1 className="text-2xl font-bold mb-6 text-gray-800">Employee Profile</h1>

            <div className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-600">First Name</label>
                        <p className="mt-1 text-gray-900">{profile.firstName}</p>
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-600">Last Name</label>
                        <p className="mt-1 text-gray-900">{profile.lastName}</p>
                    </div>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-600">Email</label>
                    <p className="mt-1 text-gray-900">{profile.email}</p>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-600">Department</label>
                    <p className="mt-1 text-gray-900">{profile.department}</p>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-600">Role</label>
                    <p className="mt-1 text-gray-900">{profile.role}</p>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-600">Username</label>
                    <p className="mt-1 text-gray-900">{profile.username}</p>
                </div>
            </div>
        </div>
    );
}
