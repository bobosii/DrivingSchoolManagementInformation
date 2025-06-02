import React from 'react';
import { Bell, Search, User } from 'lucide-react';

export const Navbar: React.FC = () => {
    return (
        <div className="bg-white border-b border-gray-200">
            <div className="px-8 py-4">
                <div className="flex items-center justify-between">
                    {/* Search Bar */}
                    <div className="flex-1 max-w-xl">
                        <div className="relative">
                            <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
                            <input
                                type="text"
                                placeholder="Ara..."
                                className="w-full pl-12 pr-4 py-3 border border-gray-200 rounded-lg focus:ring-2 focus:ring-red-500 focus:border-red-500 transition-colors"
                            />
                        </div>
                    </div>

                    {/* Right Side */}
                    <div className="flex items-center space-x-8 ml-8">
                        {/* Notifications */}
                        <button className="relative p-2 text-gray-600 hover:text-red-600 transition-colors">
                            <Bell className="w-6 h-6" />
                            <span className="absolute top-0 right-0 w-2 h-2 bg-red-500 rounded-full"></span>
                        </button>

                        {/* User Menu */}
                        <div className="flex items-center space-x-4">
                            <div className="w-10 h-10 bg-gradient-to-r from-red-400 to-red-600 rounded-full flex items-center justify-center">
                                <User className="w-6 h-6 text-white" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};
