package dev.emir.DrivingSchoolManagementInformation.service;

import dev.emir.DrivingSchoolManagementInformation.dao.LicenseClassRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.VehicleRepository;
import dev.emir.DrivingSchoolManagementInformation.dao.VehicleTypeRepository;
import dev.emir.DrivingSchoolManagementInformation.dto.request.VehicleCreateRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.request.VehicleUpdateRequest;
import dev.emir.DrivingSchoolManagementInformation.dto.response.LicenseClassResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.VehicleResponse;
import dev.emir.DrivingSchoolManagementInformation.dto.response.VehicleTypeResponse;
import dev.emir.DrivingSchoolManagementInformation.models.LicenseClass;
import dev.emir.DrivingSchoolManagementInformation.models.Vehicle;
import dev.emir.DrivingSchoolManagementInformation.models.VehicleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
// Design Pattern: Service Layer Pattern
@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    private LicenseClassRepository licenseClassRepository;

    public List<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public VehicleResponse getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        return convertToResponse(vehicle);
    }

    @Transactional
    public VehicleResponse createVehicle(VehicleCreateRequest request) {
        if (vehicleRepository.existsByPlate(request.getPlate())) {
            throw new RuntimeException("Vehicle with this plate already exists");
        }

        VehicleType vehicleType = vehicleTypeRepository.findById(request.getVehicleTypeId())
                .orElseThrow(() -> new RuntimeException("Vehicle type not found"));

        List<LicenseClass> licenseClasses = licenseClassRepository.findAllById(request.getLicenseClassIds());
        if (licenseClasses.size() != request.getLicenseClassIds().size()) {
            throw new RuntimeException("One or more license classes not found");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(request.getPlate());
        vehicle.setAutomatic(request.isAutomatic());
        vehicle.setInspectionDate(request.getInspectionDate());
        vehicle.setInsuranceDate(request.getInsuranceDate());
        vehicle.setVehicleType(vehicleType);
        vehicle.setLicenseClasses(licenseClasses);

        return convertToResponse(vehicleRepository.save(vehicle));
    }

    @Transactional
    public VehicleResponse updateVehicle(Long id, VehicleUpdateRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (request.getPlate() != null && !request.getPlate().equals(vehicle.getPlate())) {
            if (vehicleRepository.existsByPlate(request.getPlate())) {
                throw new RuntimeException("Vehicle with this plate already exists");
            }
            vehicle.setPlate(request.getPlate());
        }

        if (request.getAutomatic() != null) {
            vehicle.setAutomatic(request.getAutomatic());
        }

        if (request.getInspectionDate() != null) {
            vehicle.setInspectionDate(request.getInspectionDate());
        }

        if (request.getInsuranceDate() != null) {
            vehicle.setInsuranceDate(request.getInsuranceDate());
        }

        if (request.getVehicleTypeId() != null) {
            VehicleType vehicleType = vehicleTypeRepository.findById(request.getVehicleTypeId())
                    .orElseThrow(() -> new RuntimeException("Vehicle type not found"));
            vehicle.setVehicleType(vehicleType);
        }

        if (request.getLicenseClassIds() != null) {
            List<LicenseClass> licenseClasses = licenseClassRepository.findAllById(request.getLicenseClassIds());
            if (licenseClasses.size() != request.getLicenseClassIds().size()) {
                throw new RuntimeException("One or more license classes not found");
            }
            vehicle.setLicenseClasses(licenseClasses);
        }

        return convertToResponse(vehicleRepository.save(vehicle));
    }

    @Transactional
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehicle not found");
        }
        vehicleRepository.deleteById(id);
    }

    private VehicleResponse convertToResponse(Vehicle vehicle) {
        VehicleResponse response = new VehicleResponse();
        response.setId(vehicle.getId());
        response.setPlate(vehicle.getPlate());
        response.setAutomatic(vehicle.isAutomatic());
        response.setInspectionDate(vehicle.getInspectionDate());
        response.setInsuranceDate(vehicle.getInsuranceDate());

        VehicleTypeResponse vehicleTypeResponse = new VehicleTypeResponse();
        vehicleTypeResponse.setId(vehicle.getVehicleType().getId());
        vehicleTypeResponse.setName(vehicle.getVehicleType().getName());
        response.setVehicleType(vehicleTypeResponse);

        List<LicenseClassResponse> licenseClassResponses = vehicle.getLicenseClasses().stream()
                .map(licenseClass -> {
                    LicenseClassResponse licenseClassResponse = new LicenseClassResponse();
                    licenseClassResponse.setId(licenseClass.getId());
                    licenseClassResponse.setCode(licenseClass.getCode());
                    licenseClassResponse.setDescription(licenseClass.getDescription());
                    return licenseClassResponse;
                })
                .collect(Collectors.toList());
        response.setLicenseClasses(licenseClassResponses);

        return response;
    }
} 