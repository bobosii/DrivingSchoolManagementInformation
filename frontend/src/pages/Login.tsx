import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axios';
import { LogIn, Mail, Lock } from 'lucide-react';

export const Login: React.FC = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            const response = await axiosInstance.post("/auth/login", {
                username,
                password
            });

            const { token, linkedEntityId } = response.data;
            localStorage.setItem("token", token);
            localStorage.setItem("linkedEntityId", linkedEntityId);

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
            if (err.response?.status === 401) {
                setError("Kullanıcı adı veya şifre hatalı.");
            } else {
                setError("Beklenmeyen bir hata oluştu. Lütfen tekrar deneyin.");
            }
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-50 p-4">
            <div className="w-full max-w-md">
                <div className="bg-white rounded-xl shadow-2xl p-8 border border-gray-100">
                    {/* Header */}
                    <div className="text-center mb-8">
                        <div className="mx-auto w-16 h-16 bg-red-500 rounded-full flex items-center justify-center mb-4">
                            <LogIn className="w-8 h-8 text-white" />
                        </div>
                        <h1 className="text-2xl font-bold text-gray-900">
                            Sürücü Kursu Sistemi
                        </h1>
                        <p className="text-gray-600 mt-2">Hesabınıza giriş yapın</p>
                    </div>

                    {/* Error Message */}
                    {error && (
                        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg">
                            <p className="text-red-800 text-sm">{error}</p>
                        </div>
                    )}

                    {/* Form */}
                    <form onSubmit={handleSubmit} className="space-y-6">
                        <div>
                            <label htmlFor="username" className="block text-sm font-medium text-gray-700 mb-2">
                                Kullanıcı Adı
                            </label>
                            <div className="relative">
                                <Mail className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                                <input
                                    id="username"
                                    type="text"
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                    className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-red-500 transition-colors"
                                    placeholder="ornekkullanici"
                                    required
                                />
                            </div>
                        </div>

                        <div>
                            <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
                                Şifre
                            </label>
                            <div className="relative">
                                <Lock className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                                <input
                                    id="password"
                                    type="password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-red-500 transition-colors"
                                    placeholder="••••••••"
                                    required
                                />
                            </div>
                        </div>

                        <button
                            type="submit"
                            disabled={loading}
                            className="w-full bg-red-500 text-white py-3 px-4 rounded-lg font-medium hover:bg-red-600 focus:ring-2 focus:ring-red-500 focus:ring-offset-2 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                            {loading ? (
                                <div className="flex items-center justify-center">
                                    <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin mr-2"></div>
                                    Giriş Yapılıyor...
                                </div>
                            ) : (
                                'Giriş Yap'
                            )}
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};
