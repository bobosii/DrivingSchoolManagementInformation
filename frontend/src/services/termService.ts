import axios from '../api/axios';
import type { Student } from './studentService';

export interface StudentInTerm {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    birthDate: string;
    fullName: string;
}

export interface Term {
    id: number;
    month: string;
    year: number;
    quota: number;
    name: string;
    students: StudentInTerm[];
}

export interface CreateTermRequest {
    month: string;
    year: number;
    quota: number;
}

export const getAllTerms = async (): Promise<Term[]> => {
    try {
        const response = await axios.get('/term');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching terms:', error);
        throw error;
    }
};

export const createTerm = async (request: CreateTermRequest): Promise<Term> => {
    try {
        const response = await axios.post('/term/create', request);
        return response.data.data;
    } catch (error) {
        console.error('Error creating term:', error);
        throw error;
    }
};

export const updateTerm = async (id: number, request: CreateTermRequest): Promise<Term> => {
    try {
        const response = await axios.put(`/term/${id}`, request);
        return response.data.data;
    } catch (error) {
        console.error('Error updating term:', error);
        throw error;
    }
};

export const deleteTerm = async (id: number): Promise<void> => {
    try {
        await axios.delete(`/term/${id}`);
    } catch (error) {
        console.error('Error deleting term:', error);
        throw error;
    }
};

export const getUnassignedStudents = async (): Promise<StudentInTerm[]> => {
    try {
        const response = await axios.get('/term/unassigned-students');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching unassigned students:', error);
        throw error;
    }
};

export const assignStudentToTerm = async (termId: number, studentId: number): Promise<Term> => {
    try {
        const response = await axios.post(`/term/${termId}/students/${studentId}`);
        return response.data.data;
    } catch (error) {
        console.error('Error assigning student to term:', error);
        throw error;
    }
};

export const removeStudentFromTerm = async (termId: number, studentId: number): Promise<Term> => {
    try {
        const response = await axios.delete(`/term/${termId}/students/${studentId}`);
        return response.data.data;
    } catch (error) {
        console.error('Error removing student from term:', error);
        throw error;
    }
};

export const getStudentsInTerm = async (termId: number): Promise<StudentInTerm[]> => {
    try {
        const response = await axios.get(`/term/${termId}/students`);
        return response.data.data;
    } catch (error) {
        console.error('Error fetching students in term:', error);
        throw error;
    }
}; 