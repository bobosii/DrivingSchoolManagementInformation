package dev.emir.DrivingSchoolManagementInformation.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    private Long id;
    private String plate;
    private boolean automatic;
    private LocalDate inspectionDate;
    private LocalDate insuranceDate;

    @ManyToOne
    private VehicleType vehicleType;

    @ManyToMany
    @JoinTable(
            name = "vehicle_license_class",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "license_class_id")
    )
    private List<LicenseClass> licenseClasses;

    public Vehicle(){}

    public Vehicle(Long id, String plate, boolean automatic, LocalDate inspectionDate, LocalDate insuranceDate, VehicleType vehicleType, List<LicenseClass> licenseClasses) {
        this.id = id;
        this.plate = plate;
        this.automatic = automatic;
        this.inspectionDate = inspectionDate;
        this.insuranceDate = insuranceDate;
        this.vehicleType = vehicleType;
        this.licenseClasses = licenseClasses;
    }

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

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public List<LicenseClass> getLicenseClasses() {
        return licenseClasses;
    }

    public void setLicenseClasses(List<LicenseClass> licenseClasses) {
        this.licenseClasses = licenseClasses;
    }
}
