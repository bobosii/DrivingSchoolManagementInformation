import React, { useEffect, useState } from 'react';
import { getAllTerms, createTerm, updateTerm, deleteTerm, assignStudentToTerm, removeStudentFromTerm, getStudentsInTerm } from '../services/termService';
import type { Term } from '../services/termService';
import { getAllStudents } from '../services/studentService';
import type { Student } from '../services/studentService';
import { Plus, Edit, Trash2, Users } from 'lucide-react';

const MONTHS = [
    { value: 'OCAK', label: 'Ocak' },
    { value: 'ŞUBAT', label: 'Şubat' },
    { value: 'MART', label: 'Mart' },
    { value: 'NİSAN', label: 'Nisan' },
    { value: 'MAYIS', label: 'Mayıs' },
    { value: 'HAZİRAN', label: 'Haziran' },
    { value: 'TEMMUZ', label: 'Temmuz' },
    { value: 'AĞUSTOS', label: 'Ağustos' },
    { value: 'EYLÜL', label: 'Eylül' },
    { value: 'EKİM', label: 'Ekim' },
    { value: 'KASIM', label: 'Kasım' },
    { value: 'ARALIK', label: 'Aralık' }
];

const TermsPage = () => {
    const [terms, setTerms] = useState<Term[]>([]);
    const [students, setStudents] = useState<Student[]>([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isStudentModalOpen, setIsStudentModalOpen] = useState(false);
    const [editingTerm, setEditingTerm] = useState<Term | null>(null);
    const [selectedTerm, setSelectedTerm] = useState<Term | null>(null);
    const [formData, setFormData] = useState({
        month: '',
        year: new Date().getFullYear(),
        quota: 0
    });

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const [termsData, studentsData] = await Promise.all([
                getAllTerms(),
                getAllStudents()
            ]);
            setTerms(termsData);
            setStudents(studentsData);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: name === 'quota' || name === 'year' ? parseInt(value) : value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            if (editingTerm) {
                await updateTerm(editingTerm.id, formData);
            } else {
                await createTerm(formData);
            }
            setIsModalOpen(false);
            setEditingTerm(null);
            setFormData({
                month: '',
                year: new Date().getFullYear(),
                quota: 0
            });
            fetchData();
        } catch (error) {
            console.error('Error saving term:', error);
        }
    };

    const handleEdit = (term: Term) => {
        setEditingTerm(term);
        setFormData({
            month: term.month,
            year: term.year,
            quota: term.quota
        });
        setIsModalOpen(true);
    };

    const handleDelete = async (id: number) => {
        if (window.confirm('Bu dönemi silmek istediğinizden emin misiniz? Bu işlem geri alınamaz.')) {
            try {
                await deleteTerm(id);
                fetchData();
            } catch (error) {
                console.error('Error deleting term:', error);
                alert(error instanceof Error ? error.message : 'Dönem silinirken bir hata oluştu');
            }
        }
    };

    const handleViewStudents = async (term: Term) => {
        setSelectedTerm(term);
        setIsStudentModalOpen(true);
    };

    const handleAssignStudent = async (termId: number, studentId: number) => {
        try {
            await assignStudentToTerm(termId, studentId);
            const updatedStudents = await getStudentsInTerm(termId);
            setSelectedTerm(prev => prev ? { ...prev, students: updatedStudents } : null);
            const updatedTerms = await getAllTerms();
            setTerms(updatedTerms);
            setIsStudentModalOpen(false);
        } catch (error) {
            console.error('Error assigning student:', error);
            alert('Öğrenci atanırken bir hata oluştu');
        }
    };

    const handleRemoveStudent = async (termId: number, studentId: number) => {
        try {
            await removeStudentFromTerm(termId, studentId);
            const updatedStudents = await getStudentsInTerm(termId);
            setSelectedTerm(prev => prev ? { ...prev, students: updatedStudents } : null);
            const updatedTerms = await getAllTerms();
            setTerms(updatedTerms);
        } catch (error) {
            console.error('Error removing student:', error);
            alert('Öğrenci kaldırılırken bir hata oluştu');
        }
    };

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Dönemler</h1>
                <button
                    onClick={() => {
                        setEditingTerm(null);
                        setFormData({
                            month: '',
                            year: new Date().getFullYear(),
                            quota: 0
                        });
                        setIsModalOpen(true);
                    }}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 flex items-center"
                >
                    <Plus className="w-5 h-5 mr-2" />
                    Yeni Dönem Ekle
                </button>
            </div>

            <div className="bg-white rounded-lg shadow overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Dönem</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Kontenjan</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Öğrenci Sayısı</th>
                            <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">İşlemler</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {terms.map((term) => (
                            <tr key={term.id}>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    {term.name}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    {term.quota}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    {term.students?.length || 0}
                                </td>
                                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                    <button
                                        onClick={() => handleViewStudents(term)}
                                        className="text-green-600 hover:text-green-900 mr-4"
                                    >
                                        <Users size={20} />
                                    </button>
                                    <button
                                        onClick={() => handleEdit(term)}
                                        className="text-indigo-600 hover:text-indigo-900 mr-4"
                                    >
                                        <Edit size={20} />
                                    </button>
                                    <button
                                        onClick={() => handleDelete(term.id)}
                                        className="text-red-600 hover:text-red-900"
                                    >
                                        <Trash2 size={20} />
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {isModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white p-6 rounded-lg w-96">
                        <h2 className="text-xl font-bold mb-4">
                            {editingTerm ? 'Dönem Düzenle' : 'Yeni Dönem'}
                        </h2>
                        <form onSubmit={handleSubmit}>
                            <div className="mb-4">
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    Ay
                                </label>
                                <select
                                    name="month"
                                    value={formData.month}
                                    onChange={handleInputChange}
                                    className="w-full p-2 border rounded"
                                    required
                                >
                                    <option value="">Ay Seçin</option>
                                    {MONTHS.map(month => (
                                        <option key={month.value} value={month.value}>
                                            {month.label}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className="mb-4">
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    Yıl
                                </label>
                                <input
                                    type="number"
                                    name="year"
                                    value={formData.year}
                                    onChange={handleInputChange}
                                    className="w-full p-2 border rounded"
                                    required
                                />
                            </div>
                            <div className="mb-4">
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    Kontenjan
                                </label>
                                <input
                                    type="number"
                                    name="quota"
                                    value={formData.quota}
                                    onChange={handleInputChange}
                                    className="w-full p-2 border rounded"
                                    required
                                />
                            </div>
                            <div className="flex justify-end gap-2">
                                <button
                                    type="button"
                                    onClick={() => setIsModalOpen(false)}
                                    className="px-4 py-2 text-gray-600 hover:text-gray-800"
                                >
                                    İptal
                                </button>
                                <button
                                    type="submit"
                                    className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
                                >
                                    {editingTerm ? 'Güncelle' : 'Oluştur'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {isStudentModalOpen && selectedTerm && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white p-6 rounded-lg w-[800px] max-h-[80vh] overflow-y-auto">
                        <div className="flex justify-between items-center mb-4">
                            <h2 className="text-xl font-bold">
                                {selectedTerm.name} - Öğrenciler
                            </h2>
                            <button
                                onClick={() => setIsStudentModalOpen(false)}
                                className="text-gray-500 hover:text-gray-700"
                            >
                                ✕
                            </button>
                        </div>

                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <h3 className="font-medium mb-2">Dönemdeki Öğrenciler</h3>
                                <div className="space-y-2">
                                    {selectedTerm.students?.map(student => (
                                        <div key={student.id} className="flex justify-between items-center p-2 bg-gray-50 rounded">
                                            <span>{student.fullName}</span>
                                            <button
                                                onClick={() => handleRemoveStudent(selectedTerm.id, student.id)}
                                                className="text-red-600 hover:text-red-900"
                                            >
                                                <Trash2 size={16} />
                                            </button>
                                        </div>
                                    ))}
                                </div>
                            </div>

                            <div>
                                <h3 className="font-medium mb-2">Atanabilir Öğrenciler</h3>
                                <div className="space-y-2">
                                    {students
                                        .filter(student => !selectedTerm.students?.some(s => s.id === student.id))
                                        .map(student => (
                                            <div key={student.id} className="flex justify-between items-center p-2 bg-gray-50 rounded">
                                                <span>{student.fullName}</span>
                                                <button
                                                    onClick={() => handleAssignStudent(selectedTerm.id, student.id)}
                                                    className="text-green-600 hover:text-green-900"
                                                >
                                                    <Plus size={16} />
                                                </button>
                                            </div>
                                        ))}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default TermsPage; 