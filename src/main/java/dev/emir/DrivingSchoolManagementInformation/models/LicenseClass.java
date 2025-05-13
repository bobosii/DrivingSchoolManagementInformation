package dev.emir.DrivingSchoolManagementInformation.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "license_class")
public class LicenseClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String description;

    @ManyToMany(mappedBy = "licenseClasses")
    private List<Vehicle> vehicles;

    @ManyToMany(mappedBy = "requestedLicenseClasses")
    private List<Student> students;

    public LicenseClass(){}

    public LicenseClass(Long id, String code, String description, List<Vehicle> vehicles, List<Student> students) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.vehicles = vehicles;
        this.students = students;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
