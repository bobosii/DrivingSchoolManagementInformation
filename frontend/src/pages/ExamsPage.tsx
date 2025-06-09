import React, { useEffect, useState } from 'react';
import { getAllExams, createExam, updateExam, deleteExam, downloadExam } from '../services/examService';
import type { Exam, CreateExamRequest } from '../services/examService';
import { useAuth } from '../context/AuthContext';
import { Plus, Edit, Trash2, Download } from 'lucide-react';

const ExamsPage: React.FC = () => {
    const [exams, setExams] = useState<Exam[]>([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingExam, setEditingExam] = useState<Exam | null>(null);
    const [formData, setFormData] = useState<CreateExamRequest>({
        title: '',
        description: '',
        examDate: '',
        file: null as unknown as File
    });
    const { userRole } = useAuth();

    useEffect(() => {
        fetchExams();
    }, []);

    const fetchExams = async () => {
        try {
            const data = await getAllExams();
            setExams(data);
        } catch (error) {
            console.error('Error fetching exams:', error);
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
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
        try {
            if (editingExam) {
                await updateExam(editingExam.id, formData);
            } else {
                await createExam(formData);
            }
            setIsModalOpen(false);
            setEditingExam(null);
            setFormData({
                title: '',
                description: '',
                examDate: '',
                file: null as unknown as File
            });
            fetchExams();
        } catch (error) {
            console.error('Error saving exam:', error);
        }
    };

    const handleEdit = (exam: Exam) => {
        setEditingExam(exam);
        setFormData({
            title: exam.title,
            description: exam.description,
            examDate: exam.examDate,
            file: null as unknown as File
        });
        setIsModalOpen(true);
    };

    const handleDelete = async (id: number) => {
        if (window.confirm('Bu sınavı silmek istediğinizden emin misiniz?')) {
            try {
                await deleteExam(id);
                fetchExams();
            } catch (error) {
                console.error('Error deleting exam:', error);
            }
        }
    };

    const handleDownload = async (id: number) => {
        try {
            await downloadExam(id);
        } catch (error) {
            console.error('Error downloading exam:', error);
        }
    };

    const canEdit = userRole === 'ADMIN' || userRole === 'EMPLOYEE' || userRole === 'INSTRUCTOR';

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Sınavlar</h1>
                {canEdit && (
                    <button
                        onClick={() => {
                            setEditingExam(null);
                            setFormData({
                                title: '',
                                description: '',
                                examDate: '',
                                file: null as unknown as File
                            });
                            setIsModalOpen(true);
                        }}
                        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 flex items-center"
                    >
                        <Plus className="w-5 h-5 mr-2" />
                        Yeni Sınav Ekle
                    </button>
                )}
            </div>

            <div className="bg-white rounded-lg shadow overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Başlık</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Açıklama</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tarih</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Dosya</th>
                            {canEdit && (
                                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">İşlemler</th>
                            )}
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {exams.map((exam) => (
                            <tr key={exam.id}>
                                <td className="px-6 py-4 whitespace-nowrap">{exam.title}</td>
                                <td className="px-6 py-4">{exam.description}</td>
                                <td className="px-6 py-4 whitespace-nowrap">{new Date(exam.examDate).toLocaleDateString()}</td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    <button
                                        onClick={() => handleDownload(exam.id)}
                                        className="text-blue-600 hover:text-blue-900"
                                    >
                                        <Download className="w-5 h-5" />
                                    </button>
                                </td>
                                {canEdit && (
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <button
                                            onClick={() => handleEdit(exam)}
                                            className="text-blue-600 hover:text-blue-900 mr-3"
                                        >
                                            <Edit className="w-5 h-5" />
                                        </button>
                                        <button
                                            onClick={() => handleDelete(exam.id)}
                                            className="text-red-600 hover:text-red-900"
                                        >
                                            <Trash2 className="w-5 h-5" />
                                        </button>
                                    </td>
                                )}
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {isModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <h2 className="text-xl font-bold mb-4">
                            {editingExam ? 'Sınavı Düzenle' : 'Yeni Sınav Ekle'}
                        </h2>
                        <form onSubmit={handleSubmit}>
                            <div className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Başlık</label>
                                    <input
                                        type="text"
                                        name="title"
                                        value={formData.title}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Açıklama</label>
                                    <textarea
                                        name="description"
                                        value={formData.description}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Sınav Tarihi</label>
                                    <input
                                        type="date"
                                        name="examDate"
                                        value={formData.examDate}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Sınav Dosyası</label>
                                    <input
                                        type="file"
                                        onChange={handleFileChange}
                                        className="mt-1 block w-full"
                                        required={!editingExam}
                                    />
                                </div>
                            </div>
                            <div className="mt-6 flex justify-end space-x-3">
                                <button
                                    type="button"
                                    onClick={() => setIsModalOpen(false)}
                                    className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
                                >
                                    İptal
                                </button>
                                <button
                                    type="submit"
                                    className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
                                >
                                    {editingExam ? 'Güncelle' : 'Ekle'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default ExamsPage; 