import React, { createContext, useContext, useState, useEffect } from 'react';
import type { ReactNode } from 'react';
import { jwtDecode } from 'jwt-decode';

interface DecodedToken {
    sub: string;
    role: string;
    id: number;
    exp: number;
}

interface AuthContextType {
    isAuthenticated: boolean;
    isAdmin: boolean;
    userId: number | null;
    userRole: string | null;
    checkAuth: () => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [isAdmin, setIsAdmin] = useState<boolean>(false);
    const [userId, setUserId] = useState<number | null>(null);
    const [userRole, setUserRole] = useState<string | null>(null);

    const checkAuth = () => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                console.log("Raw token:", token); // Debug için
                const decodedToken = jwtDecode<DecodedToken>(token);
                console.log("Decoded token:", decodedToken); // Debug için
                
                setIsAuthenticated(true);
                
                // Admin rolü kontrolünü düzelt
                const isUserAdmin = decodedToken.role === 'ADMIN';
                console.log("Is user admin:", isUserAdmin); // Debug için
                setIsAdmin(isUserAdmin);
                
                // ID'yi doğrudan token'dan al
                if (decodedToken.id) {
                    console.log("Setting userId from token:", decodedToken.id); // Debug için
                    setUserId(decodedToken.id);
                } else {
                    console.log("No id found in token"); // Debug için
                    setUserId(null);
                }
                
                setUserRole(decodedToken.role);
            } catch (error) {
                console.error('Error decoding token:', error);
                logout();
            }
        } else {
            console.log("No token found"); // Debug için
            logout();
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        setIsAuthenticated(false);
        setIsAdmin(false);
        setUserId(null);
        setUserRole(null);
    };

    useEffect(() => {
        checkAuth();
    }, []);

    return (
        <AuthContext.Provider value={{ isAuthenticated, isAdmin, userId, userRole, checkAuth, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
}; 