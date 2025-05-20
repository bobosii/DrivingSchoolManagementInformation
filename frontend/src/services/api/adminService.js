import api from '../api';
import axios from 'axios';

const getProfile = async () => {
  const response = await api.get('/admin/dashboard');
  return response.data;
};

const getStudents = async () => {
  const response = await api.get('/admin/students');
  return response.data;
};

const getInstructors = async () => {
  const response = await api.get('/admin/instructors');
  return response.data;
};

const getEmployees = async () => {
  const response = await api.get('/admin/employees');
  return response.data;
};

const getTerms = async () => {
  const response = await api.get('/term/all');
  return response.data;
};

const getCourses = async () => {
  const response = await api.get('/course/all');
  return response.data;
};

const getClassrooms = async () => {
  const response = await api.get('/classroom/all');
  return response.data;
};

const API_URL = 'http://localhost:8080/api/admin';

const adminService = {
  getProfile: () => {
    return axios.get(`${API_URL}/profile`);
  },

  addStudent: (studentData) => {
    return axios.post(`${API_URL}/students`, studentData);
  },

  addInstructor: (instructorData) => {
    return axios.post(`${API_URL}/instructors`, instructorData);
  },

  addEmployee: (employeeData) => {
    return axios.post(`${API_URL}/employees`, employeeData);
  },

  getTerms: () => {
    return axios.get(`${API_URL}/terms`);
  },

  getStudents: () => {
    return axios.get(`${API_URL}/students`);
  },

  getInstructors: () => {
    return axios.get(`${API_URL}/instructors`);
  },

  getEmployees: () => {
    return axios.get(`${API_URL}/employees`);
  },

  getCourses: () => {
    return axios.get(`${API_URL}/courses`);
  },

  getClassrooms: () => {
    return axios.get(`${API_URL}/classrooms`);
  },
};

export default adminService; 