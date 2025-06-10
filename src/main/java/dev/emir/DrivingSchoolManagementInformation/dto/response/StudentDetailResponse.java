package dev.emir.DrivingSchoolManagementInformation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import dev.emir.DrivingSchoolManagementInformation.dto.response.AppointmentInfo;

public class StudentDetailResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime birthDate;
    private Long termId;
    private String termName;
    private List<AppointmentInfo> appointments;

    public StudentDetailResponse(Long id, String firstName, String lastName, String email, 
                               LocalDateTime birthDate, String termName, List<AppointmentInfo> appointments) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.termName = termName;
        this.appointments = appointments;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public Long getTermId() {
        return termId;
    }

    public void setTermId(Long termId) {
        this.termId = termId;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public List<AppointmentInfo> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentInfo> appointments) {
        this.appointments = appointments;
    }
} 