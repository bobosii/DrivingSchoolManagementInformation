import axios from '../api/axios';

export interface Document {
    id: number;
    fileName: string;
    fileType: string;
    uploadDate: string;
    documentType: string;
    studentId: number;
    studentName: string;
}

export interface DocumentCreateRequest {
    file: File;
    type: string;
    studentId: number;
}

export interface DocumentUpdateRequest {
    type: string;
    studentId: number;
}

export const getAllDocuments = async (): Promise<Document[]> => {
    try {
        const response = await axios.get('/documents');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching documents:', error);
        throw new Error('Belgeler alınırken bir hata oluştu');
    }
};

export const getDocumentsByStudentId = async (studentId: number): Promise<Document[]> => {
    try {
        const response = await axios.get(`/documents/student/${studentId}`);
        return response.data.data;
    } catch (error) {
        console.error('Error fetching student documents:', error);
        throw new Error('Öğrenci belgeleri alınırken bir hata oluştu');
    }
};

export const getDocumentById = async (id: number): Promise<Document> => {
    try {
        const response = await axios.get(`/documents/${id}`);
        return response.data.data;
    } catch (error) {
        console.error('Error fetching document:', error);
        throw new Error('Belge alınırken bir hata oluştu');
    }
};

export const createDocument = async (request: DocumentCreateRequest): Promise<Document> => {
    try {
        const formData = new FormData();
        formData.append('file', request.file);
        formData.append('type', request.type);
        formData.append('studentId', request.studentId.toString());

        const response = await axios.post('/documents', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        return response.data.data;
    } catch (error) {
        console.error('Error creating document:', error);
        throw new Error('Belge oluşturulurken bir hata oluştu');
    }
};

export const updateDocument = async (id: number, request: DocumentUpdateRequest): Promise<Document> => {
    try {
        const response = await axios.put(`/documents/${id}`, request);
        return response.data.data;
    } catch (error) {
        console.error('Error updating document:', error);
        throw new Error('Belge güncellenirken bir hata oluştu');
    }
};

export const deleteDocument = async (id: number): Promise<void> => {
    try {
        await axios.delete(`/documents/${id}`);
    } catch (error) {
        console.error('Error deleting document:', error);
        throw new Error('Belge silinirken bir hata oluştu');
    }
};

export const downloadDocument = async (id: number): Promise<void> => {
    try {
        const response = await axios.get(`/documents/download/${id}`, {
            responseType: 'blob',
            headers: {
                'Accept': 'application/octet-stream'
            }
        });

        // Get filename from Content-Disposition header
        const contentDisposition = response.headers['content-disposition'];
        let filename = 'document';

        if (contentDisposition) {
            // Extract filename from Content-Disposition header
            const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
            const matches = filenameRegex.exec(contentDisposition);

            if (matches != null && matches[1]) {
                // Remove quotes and decode the filename
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
        console.error('Error downloading document:', error);
        throw new Error('Belge indirilirken bir hata oluştu');
    }
};

export const getMyDocuments = async (): Promise<Document[]> => {
    try {
        const response = await axios.get('/documents/my-documents');
        return response.data.data;
    } catch (error) {
        console.error('Error fetching my documents:', error);
        throw error;
    }
};
