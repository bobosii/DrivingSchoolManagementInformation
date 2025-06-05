import axios from "axios";

export interface Course {
    id: number;
    name: string;
    courseType: string;
}

export interface CourseSession {
    id: number;
    courseName: string;
    instructorName: string;
    classroomName: string;
    startTime: string;
    endTime: string;
}

export const getAllCourseSessions = async (): Promise<CourseSession[]> => {
    const token = localStorage.getItem('token');
    if (!token) {
        throw new Error('No token found');
    }

    const response = await fetch('http://localhost:8080/api/course-sessions', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    });

    if (!response.ok) {
        throw new Error('Failed to fetch course sessions');
    }

    const data = await response.json();
    console.log('Course sessions data:', data);
    return data.data;
}; 