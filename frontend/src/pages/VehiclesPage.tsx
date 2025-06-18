import React, { useEffect, useState } from 'react';
import { getAllVehicles, createVehicle, updateVehicle, deleteVehicle } from '../services/vehicleService';
import type { Vehicle } from '../services/vehicleService';
import { getAllVehicleTypes } from '../services/vehicleTypeService';
import type { VehicleType } from '../services/vehicleTypeService';
import { getAllLicenseClasses } from '../services/licenseClassService';
import type { LicenseClass } from '../services/licenseClassService';
import { Plus, Edit, Trash2 } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const VehiclesPage: React.FC = () => {
    const { isInstructor } = useAuth();
    const [vehicles, setVehicles] = useState<Vehicle[]>([]);
    const [vehicleTypes, setVehicleTypes] = useState<VehicleType[]>([]);
    const [licenseClasses, setLicenseClasses] = useState<LicenseClass[]>([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingVehicle, setEditingVehicle] = useState<Vehicle | null>(null);
    const [formData, setFormData] = useState({
        plate: '',
        brand: '',
        automatic: false,
        inspectionDate: '',
        insuranceDate: '',
        vehicleTypeId: '',
        licenseClassIds: [] as number[]
    });

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const [vehiclesData, typesData, licenseClassesData] = await Promise.all([
                getAllVehicles(),
                getAllVehicleTypes(),
                getAllLicenseClasses()
            ]);
            setVehicles(vehiclesData);
            setVehicleTypes(typesData);
            setLicenseClasses(licenseClassesData);
        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const { name, value, type } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: type === 'checkbox' ? (e.target as HTMLInputElement).checked : value
        }));
    };

    const handleLicenseClassChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const selectedOptions = Array.from(e.target.selectedOptions, option => parseInt(option.value));
        setFormData(prev => ({
            ...prev,
            licenseClassIds: selectedOptions
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const vehicleData = {
                plate: formData.plate,
                brand: formData.brand,
                automatic: formData.automatic,
                inspectionDate: formData.inspectionDate,
                insuranceDate: formData.insuranceDate,
                vehicleTypeId: parseInt(formData.vehicleTypeId),
                licenseClassIds: formData.licenseClassIds
            };

            if (editingVehicle) {
                await updateVehicle(editingVehicle.id, vehicleData);
            } else {
                await createVehicle(vehicleData);
            }

            setIsModalOpen(false);
            setEditingVehicle(null);
            setFormData({
                plate: '',
                brand: '',
                automatic: false,
                inspectionDate: '',
                insuranceDate: '',
                vehicleTypeId: '',
                licenseClassIds: []
            });
            fetchData();
        } catch (error) {
            console.error('Error saving vehicle:', error);
        }
    };

    const handleEdit = (vehicle: Vehicle) => {
        setEditingVehicle(vehicle);
        setFormData({
            plate: vehicle.plate,
            brand: vehicle.brand,
            automatic: vehicle.automatic,
            inspectionDate: vehicle.inspectionDate,
            insuranceDate: vehicle.insuranceDate,
            vehicleTypeId: vehicle.vehicleType.id.toString(),
            licenseClassIds: vehicle.licenseClasses.map(lc => lc.id)
        });
        setIsModalOpen(true);
    };

    const handleDelete = async (id: number) => {
        if (window.confirm('Bu aracı silmek istediğinizden emin misiniz?')) {
            try {
                await deleteVehicle(id);
                fetchData();
            } catch (error) {
                console.error('Error deleting vehicle:', error);
            }
        }
    };

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Araçlar</h1>
                {!isInstructor && (
                    <button
                        onClick={() => {
                            setEditingVehicle(null);
                            setFormData({
                                plate: '',
                                brand: '',
                                automatic: false,
                                inspectionDate: '',
                                insuranceDate: '',
                                vehicleTypeId: '',
                                licenseClassIds: []
                            });
                            setIsModalOpen(true);
                        }}
                        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 flex items-center"
                    >
                        <Plus className="w-5 h-5 mr-2" />
                        Yeni Araç Ekle
                    </button>
                )}
            </div>

            <div className="bg-white rounded-lg shadow overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Plaka</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tip</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Otomatik</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Muayene Tarihi</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Sigorta Tarihi</th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">İşlemler</th>
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {vehicles.map(vehicle => (
                        <tr key={vehicle.id}>
                            <td className="px-6 py-4 whitespace-nowrap">{vehicle.plate}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{vehicle.vehicleType.name}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{vehicle.automatic ? 'Evet' : 'Hayır'}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{new Date(vehicle.inspectionDate).toLocaleDateString()}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{new Date(vehicle.insuranceDate).toLocaleDateString()}</td>
                            <td className="px-6 py-4 whitespace-nowrap">
                                {!isInstructor && (
                                    <>
                                        <button
                                            onClick={() => handleEdit(vehicle)}
                                            className="text-blue-600 hover:text-blue-900 mr-3"
                                        >
                                            <Edit className="w-5 h-5" />
                                        </button>
                                        <button
                                            onClick={() => handleDelete(vehicle.id)}
                                            className="text-red-600 hover:text-red-900"
                                        >
                                            <Trash2 className="w-5 h-5" />
                                        </button>
                                    </>
                                )}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            {!isInstructor && isModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white rounded-lg p-6 w-full max-w-md">
                        <h2 className="text-xl font-bold mb-4">
                            {editingVehicle ? 'Aracı Düzenle' : 'Yeni Araç Ekle'}
                        </h2>
                        <form onSubmit={handleSubmit}>
                            <div className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Plaka</label>
                                    <input
                                        type="text"
                                        name="plate"
                                        value={formData.plate}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Marka</label>
                                    <input
                                        type="text"
                                        name="brand"
                                        value={formData.brand}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Araç Tipi</label>
                                    <select
                                        name="vehicleTypeId"
                                        value={formData.vehicleTypeId}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        required
                                    >
                                        <option value="">Seçiniz</option>
                                        {vehicleTypes.map(type => (
                                            <option key={type.id} value={type.id}>
                                                {type.name}
                                            </option>
                                        ))}
                                    </select>
                                </div>
                                <div>
                                    <label className="flex items-center">
                                        <input
                                            type="checkbox"
                                            name="automatic"
                                            checked={formData.automatic}
                                            onChange={handleInputChange}
                                            className="rounded border-gray-300 text-blue-600 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        />
                                        <span className="ml-2 text-sm text-gray-700">Otomatik</span>
                                    </label>
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Muayene Tarihi</label>
                                    <input
                                        type="date"
                                        name="inspectionDate"
                                        value={formData.inspectionDate}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Sigorta Tarihi</label>
                                    <input
                                        type="date"
                                        name="insuranceDate"
                                        value={formData.insuranceDate}
                                        onChange={handleInputChange}
                                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        required
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-gray-700">Ehliyet Sınıfları</label>
                                    <select
                                        multiple
                                        name="licenseClassIds"
                                        value={formData.licenseClassIds.map(id => id.toString())}
                                        onChange={handleLicenseClassChange}
                                        className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                                        required
                                    >
                                        {licenseClasses.map(lc => (
                                            <option key={lc.id} value={lc.id}>
                                                {lc.code} - {lc.description}
                                            </option>
                                        ))}
                                    </select>
                                    <p className="mt-1 text-sm text-gray-500">
                                        Birden fazla seçim yapmak için Ctrl (Windows) veya Command (Mac) tuşuna basılı tutun
                                    </p>
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
                                    {editingVehicle ? 'Güncelle' : 'Ekle'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default VehiclesPage;
