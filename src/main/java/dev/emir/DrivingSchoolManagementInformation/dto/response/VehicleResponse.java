package dev.emir.DrivingSchoolManagementInformation.dto.response;

import java.time.LocalDate;
import java.util.List;

public class VehicleResponse {
    private Long id;
    private String plate;
    private boolean automatic;
    private LocalDate inspectionDate;
    private LocalDate insuranceDate;
    private VehicleTypeResponse vehicleType;
    private List<LicenseClassResponse> licenseClasses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }

    public LocalDate getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(LocalDate inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public LocalDate getInsuranceDate() {
        return insuranceDate;
    }

    public void setInsuranceDate(LocalDate insuranceDate) {
        this.insuranceDate = insuranceDate;
    }

    public VehicleTypeResponse getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleTypeResponse vehicleType) {
        this.vehicleType = vehicleType;
    }

    public List<LicenseClassResponse> getLicenseClasses() {
        return licenseClasses;
    }

    public void setLicenseClasses(List<LicenseClassResponse> licenseClasses) {
        this.licenseClasses = licenseClasses;
    }
} 