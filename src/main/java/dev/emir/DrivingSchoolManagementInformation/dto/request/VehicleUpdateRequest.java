package dev.emir.DrivingSchoolManagementInformation.dto.request;

import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

public class VehicleUpdateRequest {
    @Pattern(regexp = "^[0-9]{2}[A-Z]{1,3}[0-9]{2,4}$", message = "Geçersiz plaka formatı")
    private String plate;
    private Boolean automatic;
    private LocalDate inspectionDate;
    private LocalDate insuranceDate;
    private Long vehicleTypeId;
    private List<Long> licenseClassIds;

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Boolean getAutomatic() {
        return automatic;
    }

    public void setAutomatic(Boolean automatic) {
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

    public Long getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(Long vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public List<Long> getLicenseClassIds() {
        return licenseClassIds;
    }

    public void setLicenseClassIds(List<Long> licenseClassIds) {
        this.licenseClassIds = licenseClassIds;
    }
} 