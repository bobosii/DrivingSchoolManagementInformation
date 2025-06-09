import axios from '../api/axios';

export interface Exam {
    id: number;
    title: string;
    description: string;
    examDate: string;
    fileName: string;
    fileType: string;
    fileSize: number;
}

export interface CreateExamRequest {
    title: string;
    description: string;
    examDate: string;
    file: File;
}

export const getAllExams = async (): Promise<Exam[]> => {
    try {
        const response = await axios.get('/exams');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching exams:', error);
        throw new Error('Sınavlar alınırken bir hata oluştu');
    }
};

export const getExamById = async (id: number): Promise<Exam> => {
    try {
        const response = await axios.get(`/exams/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Error fetching exam:', error);
        throw new Error('Sınav alınırken bir hata oluştu');
    }
};

export const createExam = async (request: CreateExamRequest): Promise<Exam> => {
    try {
        const formData = new FormData();
        formData.append('title', request.title);
        formData.append('description', request.description);
        formData.append('examDate', request.examDate);
        formData.append('file', request.file);

        const response = await axios.post('/exams', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return response.data.data;
    } catch (error) {
        console.error('Error creating exam:', error);
        throw new Error('Sınav oluşturulurken bir hata oluştu');
    }
};

export const updateExam = async (id: number, request: CreateExamRequest): Promise<Exam> => {
    try {
        const formData = new FormData();
        formData.append('title', request.title);
        formData.append('description', request.description);
        formData.append('examDate', request.examDate);
        if (request.file) {
            formData.append('file', request.file);
        }

        const response = await axios.put(`/exams/${id}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return response.data.data;
    } catch (error) {
        console.error('Error updating exam:', error);
        throw new Error('Sınav güncellenirken bir hata oluştu');
    }
};

export const deleteExam = async (id: number): Promise<void> => {
    try {
        await axios.delete(`/exams/${id}`);
    } catch (error) {
        console.error('Error deleting exam:', error);
        throw new Error('Sınav silinirken bir hata oluştu');
    }
};

export const downloadExam = async (id: number): Promise<void> => {
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            throw new Error('No token found');
        }

        const response = await axios.get(`/exams/${id}/download`, {
            responseType: 'blob',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/octet-stream'
            }
        });

        // Get filename from Content-Disposition header
        const contentDisposition = response.headers['content-disposition'];
        let filename = 'exam.pdf';
        
        if (contentDisposition) {
            const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
            const matches = filenameRegex.exec(contentDisposition);
            
            if (matches != null && matches[1]) {
                filename = decodeURIComponent(matches[1].replace(/['"]/g, ''));
            }
        }

        // Create blob URL and trigger download
        const blob = new Blob([response.data], { type: response.headers['content-type'] });
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
    } catch (error) {
        console.error('Error downloading exam:', error);
        throw new Error('Sınav indirilirken bir hata oluştu');
    }
}; 