import { Link } from "react-router-dom";

export default function Unauthorized() {
    return (
        <div className="min-h-screen flex items-center justify-center">
            <div className="bg-white p-6 rounded shadow-md text-center">
                <h1 className="text-2xl font-bold text-red-600 mb-4">403 - Unauthorized</h1>
                <p className="mb-4">You do not have permission to access this page.</p>
                <Link to="/" className="text-blue-500 hover:underline">
                    Return to Home
                </Link>
            </div>
        </div>
    );
};
