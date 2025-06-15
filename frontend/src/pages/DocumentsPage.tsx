import React, { useEffect, useState } from 'react';
import { getAllDocuments, createDocument, updateDocument, deleteDocument, downloadDocument, type Document, type DocumentCreateRequest, type DocumentUpdateRequest } from '../services/documentService';
import { getAllStudents } from '../services/studentService';
import type { Student } from '../services/studentService';
import { Plus, Edit, Trash2, Download, FileText } from 'lucide-react';
import { toast } from 'react-hot-toast';

interface StudentWithDocuments {
    student: Student;
    documents: Document[];
}

const DocumentsPage: React.FC = () => {
    const [studentDocuments, setStudentDocuments] = useState<StudentWithDocuments[]>([]);
    const [students, setStudents] = useState<Student[]>([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingDocument, setEditingDocument] = useState<Document | null>(null);
    const [formData, setFormData] = useState({
        type: '',
        studentId: '',
        file: null as File | null
    });

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const [documentsData, studentsData] = await Promise.all([
                getAllDocuments(),
                getAllStudents()
            ]);

            // Belgeleri öğrencilere göre grupla
            const groupedDocuments = documentsData.reduce((acc, doc) => {
                const student = studentsData.find(s => s.id === doc.studentId);
                if (student) {
                    if (!acc[student.id]) {
                        acc[student.id] = {
                            student,
                            documents: []
                        };
                    }
                    acc[student.id].documents.push(doc);
                }
                return acc;
            }, {} as Record<number, StudentWithDocuments>);

            // Sadece belgesi olan öğrencilerin kartlarını göster
            setStudentDocuments(Object.values(groupedDocuments));
            // Tüm öğrencileri state'e kaydet (modal için)
            setStudents(studentsData);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            setFormData(prev => ({
                ...prev,
                file: e.target.files![0]
            }));
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!formData.file && !editingDocument) return;

        try {
            if (editingDocument) {
                const updateRequest: DocumentUpdateRequest = {
                    type: formData.type,
                    studentId: Number(formData.studentId)
                };
                await updateDocument(editingDocument.id, updateRequest);
                toast.success('Belge başarıyla güncellendi');
            } else {
                if (!formData.file) return;
                const createRequest: DocumentCreateRequest = {
                    type: formData.type,
                    studentId: Number(formData.studentId),
                    file: formData.file
                };
                await createDocument(createRequest);
                toast.success('Belge başarıyla oluşturuldu');
            }
            setIsModalOpen(false);
            setEditingDocument(null);
            setFormData({
                type: '',
                studentId: '',
                file: null
            });
            fetchData();
        } catch (error: any) {
            toast.error(error.message || 'Belge kaydedilirken bir hata oluştu');
        }
    };

    const handleEdit = (document: Document) => {
        setEditingDocument(document);
        setFormData({
            type: document.documentType,
            studentId: document.studentId.toString(),
            file: null
        });
        setIsModalOpen(true);
    };

    const handleDelete = async (id: number) => {
        if (window.confirm('Bu belgeyi silmek istediğinizden emin misiniz?')) {
            try {
                await deleteDocument(id);
                fetchData();
            } catch (error) {
                console.error('Error deleting document:', error);
            }
        }
    };

    const handleDownload = async (id: number) => {
        try {
            await downloadDocument(id);
        } catch (error) {
            console.error('Error downloading document:', error);
        }
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
        setEditingDocument(null);
        setFormData({
            type: '',
            studentId: '',
            file: null
        });
    };

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Belgeler</h1>
                <button
                    onClick={() => {
                        setEditingDocument(null);
                        setFormData({
                            type: '',
                            studentId: '',
                            file: null
                        });
                        setIsModalOpen(true);
                    }}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 flex items-center"
                >
                    <Plus className="w-5 h-5 mr-2" />
                    Yeni Belge Ekle
                </button>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {studentDocuments.map(({ student, documents }) => (
                    <div key={student.id} className="bg-white rounded-lg shadow-lg overflow-hidden">
                        <div className="bg-gradient-to-r from-blue-500 to-blue-600 p-4">
                            <h2 className="text-xl font-semibold text-white">{student.fullName}</h2>
                        </div>
                        <div className="p-4">
                            <div className="space-y-3">
                                {documents.map((document) => (
                                    <div key={document.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                                        <div className="flex items-center space-x-3">
                                            <FileText className="w-5 h-5 text-blue-500" />
                                            <div>
                                                <p className="font-medium text-gray-900">
                                                    {document.fileName ? document.fileName : "Belge Adı Yok"}
                                                </p>
                                                {document.documentType && (
                                                    <p className="text-xs text-gray-400">{document.documentType}</p>
                                                )}
                                                {document.uploadDate && !isNaN(new Date(document.uploadDate).getTime()) && (
                                                    <p className="text-sm text-gray-500">{new Date(document.uploadDate).toLocaleDateString('tr-TR')}</p>
                                                )}
                                            </div>
                                        </div>
                                        <div className="flex items-center space-x-2">
                                            <button
                                                onClick={() => handleDownload(document.id)}
                                                className="text-blue-600 hover:text-blue-800"
                                                title="İndir"
                                            >
                                                <Download className="w-5 h-5" />
                                            </button>
                                            <button
                                                onClick={() => handleEdit(document)}
                                                className="text-yellow-600 hover:text-yellow-800"
                                                title="Düzenle"
                                            >
                                                <Edit className="w-5 h-5" />
                                            </button>
                                            <button
                                                onClick={() => handleDelete(document.id)}
                                                className="text-red-600 hover:text-red-800"
                                                title="Sil"
                                            >
                                                <Trash2 className="w-5 h-5" />
                                            </button>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            {isModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <h2 className="text-xl font-bold mb-4">
                            {editingDocument ? 'Belgeyi Düzenle' : 'Yeni Belge Ekle'}
                        </h2>
                        <form onSubmit={handleSubmit}>
                            <div className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Öğrenci</label>
                                    <select
                                        name="studentId"
                                        value={formData.studentId}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        required
                                        disabled={!!editingDocument}
                                    >
                                        <option value="">Seçiniz</option>
                                        {students.map((student) => (
                                            <option key={student.id} value={student.id}>
                                                {student.fullName}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Belge Tipi</label>
                                    <input
                                        type="text"
                                        name="type"
                                        value={formData.type}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        required
                                    />
                                </div>
                                {!editingDocument && (
                                    <div>
                                        <label className="block text-sm font-medium text-gray-700">Dosya</label>
                                        <input
                                            type="file"
                                            onChange={handleFileChange}
                                            className="mt-1 block w-full"
                                            required
                                        />
                                    </div>
                                )}
                            </div>
                            <div className="mt-6 flex justify-end space-x-3">
                                <button
                                    type="button"
                                    onClick={handleCloseModal}
                                    className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                                >
                                    İptal
                                </button>
                                <button
                                    type="submit"
                                    className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
                                >
                                    {editingDocument ? 'Güncelle' : 'Ekle'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default DocumentsPage; 