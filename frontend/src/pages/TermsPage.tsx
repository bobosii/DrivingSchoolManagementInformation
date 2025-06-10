import React, { useEffect, useState } from 'react';
import { getAllTerms, createTerm, updateTerm, deleteTerm, assignStudentToTerm, removeStudentFromTerm, getStudentsInTerm, getUnassignedStudents } from '../services/termService';
import type { Term, StudentInTerm } from '../services/termService';
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
        quota: 30
    });
    const [unassignedStudents, setUnassignedStudents] = useState<StudentInTerm[]>([]);

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
            [name]: name === 'year' || name === 'quota' ? parseInt(value) : value
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
                quota: 30
            });
            fetchData();
        } catch (error) {
            console.error('Error saving term:', error);
            alert('Dönem kaydedilirken bir hata oluştu');
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
        try {
            const unassigned = await getUnassignedStudents();
            setUnassignedStudents(unassigned);
            setIsStudentModalOpen(true);
        } catch (error) {
            console.error('Error fetching unassigned students:', error);
            alert('Atanmamış öğrenciler alınırken bir hata oluştu');
        }
    };

    const handleAssignStudent = async (termId: number, studentId: number) => {
        try {
            await assignStudentToTerm(termId, studentId);
            const updatedStudents = await getStudentsInTerm(termId);
            setSelectedTerm(prev => prev ? { ...prev, students: updatedStudents } : null);
            const updatedTerms = await getAllTerms();
            setTerms(updatedTerms);
            const unassigned = await getUnassignedStudents();
            setUnassignedStudents(unassigned);
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
            const unassigned = await getUnassignedStudents();
            setUnassignedStudents(unassigned);
        } catch (error) {
            console.error('Error removing student:', error);
            alert('Öğrenci çıkarılırken bir hata oluştu');
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
                            quota: 30
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
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Kota</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Öğrenci Sayısı</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">İşlemler</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {terms.map((term) => (
                            <tr key={term.id}>
                                <td className="px-6 py-4 whitespace-nowrap">{term.name}</td>
                                <td className="px-6 py-4 whitespace-nowrap">{term.quota}</td>
                                <td className="px-6 py-4 whitespace-nowrap">{term.students?.length || 0}</td>
                                <td className="px-6 py-4 whitespace-nowrap">
                                    <button
                                        onClick={() => handleViewStudents(term)}
                                        className="text-blue-600 hover:text-blue-900 mr-3"
                                    >
                                        <Users className="w-5 h-5" />
                                    </button>
                                    <button
                                        onClick={() => handleEdit(term)}
                                        className="text-blue-600 hover:text-blue-900 mr-3"
                                    >
                                        <Edit className="w-5 h-5" />
                                    </button>
                                    <button
                                        onClick={() => handleDelete(term.id)}
                                        className="text-red-600 hover:text-red-900"
                                    >
                                        <Trash2 className="w-5 h-5" />
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>

            {isModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <h2 className="text-xl font-bold mb-4">
                            {editingTerm ? 'Dönemi Düzenle' : 'Yeni Dönem Ekle'}
                        </h2>
                        <form onSubmit={handleSubmit}>
                            <div className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Ay</label>
                                    <select
                                        name="month"
                                        value={formData.month}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        required
                                    >
                                        <option value="">Ay Seçin</option>
                                        {MONTHS.map((month) => (
                                            <option key={month.value} value={month.value}>
                                                {month.label}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Yıl</label>
                                    <input
                                        type="number"
                                        name="year"
                                        value={formData.year}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Kota</label>
                                    <input
                                        type="number"
                                        name="quota"
                                        value={formData.quota}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        required
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
                                    {editingTerm ? 'Güncelle' : 'Ekle'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}

            {isStudentModalOpen && selectedTerm && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded-lg p-6 w-full max-w-4xl">
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
                                    {unassignedStudents.map(student => (
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