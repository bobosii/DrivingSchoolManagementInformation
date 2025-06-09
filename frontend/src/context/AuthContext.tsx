import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import axios from '../api/axios';

interface DecodedToken {
    sub: string;
    role: string;
    id: number;
    exp: number;
}

interface User {
    id: number;
    username: string;
    role: string;
}

interface AuthContextType {
    isAuthenticated: boolean;
    isAdmin: boolean;
    isEmployee: boolean;
    isInstructor: boolean;
    isStudent: boolean;
    userId: number | null;
    userRole: string | null;
    user: User | null;
    checkAuth: () => void;
    logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);
    const [isAdmin, setIsAdmin] = useState<boolean>(false);
    const [isEmployee, setIsEmployee] = useState<boolean>(false);
    const [isInstructor, setIsInstructor] = useState<boolean>(false);
    const [isStudent, setIsStudent] = useState<boolean>(false);
    const [userId, setUserId] = useState<number | null>(null);
    const [userRole, setUserRole] = useState<string | null>(null);
    const [user, setUser] = useState<User | null>(null);

    const checkAuth = () => {
        const token = localStorage.getItem("token");
        if (token) {
            try {
                const decoded = JSON.parse(atob(token.split(".")[1]));
                const currentTime = Date.now() / 1000;

                if (decoded.exp > currentTime) {
                    setIsAuthenticated(true);
                    setIsAdmin(decoded.role === "ADMIN");
                    setIsEmployee(decoded.role === "EMPLOYEE");
                    setIsInstructor(decoded.role === "INSTRUCTOR");
                    setIsStudent(decoded.role === "STUDENT");
                    setUserId(decoded.id);
                    setUserRole(decoded.role);
                    setUser({
                        id: decoded.id,
                        username: decoded.sub,
                        role: decoded.role
                    });
                } else {
                    localStorage.removeItem("token");
                    setIsAuthenticated(false);
                    setIsAdmin(false);
                    setIsEmployee(false);
                    setIsInstructor(false);
                    setIsStudent(false);
                    setUserId(null);
                    setUserRole(null);
                    setUser(null);
                }
            } catch {
                localStorage.removeItem("token");
                setIsAuthenticated(false);
                setIsAdmin(false);
                setIsEmployee(false);
                setIsInstructor(false);
                setIsStudent(false);
                setUserId(null);
                setUserRole(null);
                setUser(null);
            }
        } else {
            setIsAuthenticated(false);
            setIsAdmin(false);
            setIsEmployee(false);
            setIsInstructor(false);
            setIsStudent(false);
            setUserId(null);
            setUserRole(null);
            setUser(null);
        }
    };

    useEffect(() => {
        checkAuth();
    }, []);

    const logout = () => {
        localStorage.removeItem("token");
        setIsAuthenticated(false);
        setIsAdmin(false);
        setIsEmployee(false);
        setIsInstructor(false);
        setIsStudent(false);
        setUserId(null);
        setUserRole(null);
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ isAuthenticated, isAdmin, isEmployee, isInstructor, isStudent, userId, userRole, user, checkAuth, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
}; 