import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add a request interceptor to add the auth token to requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add a response interceptor to handle token expiration
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authService = {
  login: async (credentials) => {
    try {
      const response = await api.post('/auth/login', credentials);
      console.log('Login response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Login error:', error.response?.data);
      throw error.response?.data || error.message;
    }
  },

  register: async (userData) => {
    try {
      const response = await api.post('/student/register', userData);
      return response.data;
    } catch (error) {
      throw error.response?.data || error.message;
    }
  },

  logout: () => {
    localStorage.removeItem('token');
  },
};

export const studentService = {
  getProfile: async () => {
    const response = await api.get('/student/profile');
    return response.data;
  },

  getAppointments: async () => {
    const response = await api.get('/student/appointments');
    return response.data;
  },

  bookAppointment: async (appointmentData) => {
    const response = await api.post('/student/appointment', appointmentData);
    return response.data;
  },
};

export const instructorService = {
  getProfile: async () => {
    const response = await api.get('/instructor/profile');
    return response.data;
  },

  getSchedule: async () => {
    const response = await api.get('/instructor/schedule');
    return response.data;
  },
};

export const adminService = {
  getProfile: async () => {
    try {
      console.log('=== Admin Dashboard API Call ===');
      console.log('Token from localStorage:', localStorage.getItem('token'));
      
      const response = await api.get('/admin/dashboard');
      console.log('Dashboard API Response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Dashboard API Error:', {
        message: error.message,
        status: error.response?.status,
        data: error.response?.data,
        headers: error.config?.headers
      });
      throw error;
    }
  },

  getStudents: async () => {
    const response = await api.get('/admin/students');
    return response.data;
  },

  getInstructors: async () => {
    const response = await api.get('/admin/instructors');
    return response.data;
  },

  registerInstructor: async (instructorData) => {
    const response = await api.post('/admin/register/instructor', instructorData);
    return response.data;
  },

  registerEmployee: async (employeeData) => {
    const response = await api.post('/admin/register/employee', employeeData);
    return response.data;
  },

  createCourse: async (courseData) => {
    const response = await api.post('/admin/course/create', courseData);
    return response.data;
  },

  createTerm: async (termData) => {
    const response = await api.post('/term/create', termData);
    return response.data;
  },

  createClassroom: async (classroomData) => {
    const response = await api.post('/classroom/create', classroomData);
    return response.data;
  },

  createCourseSession: async (sessionData) => {
    const response = await api.post('/course-session/create', sessionData);
    return response.data;
  },

  assignTheoretical: async (theoreticalData) => {
    const response = await api.post('/theoretical/assign', theoreticalData);
    return response.data;
  },

  approveAppointment: async (appointmentData) => {
    const response = await api.put('/appointment/approve', appointmentData);
    return response.data;
  }
};

export default api; 