package dev.emir.DrivingSchoolManagementInformation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

public class VehicleCreateRequest {
    @NotBlank(message = "Plaka alanı boş olamaz")
    @Pattern(regexp = "^[0-9]{2}[A-Z]{1,3}[0-9]{2,4}$", message = "Geçersiz plaka formatı")
    private String plate;

    @NotNull(message = "Otomatik alanı boş olamaz")
    private boolean automatic;

    @NotNull(message = "Muayene tarihi boş olamaz")
    private LocalDate inspectionDate;

    @NotNull(message = "Sigorta tarihi boş olamaz")
    private LocalDate insuranceDate;

    @NotNull(message = "Araç tipi boş olamaz")
    private Long vehicleTypeId;

    @NotNull(message = "Ehliyet sınıfları boş olamaz")
    private List<Long> licenseClassIds;

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