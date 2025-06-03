package dev.emir.DrivingSchoolManagementInformation.dto.response;

import java.time.LocalDateTime;

public class AppointmentInfo {
    private Long id;
    private LocalDateTime appointmentTime;
    private String status;
    private String instructorName;
    private String courseName;
    private String appointmentTypeName;

    public AppointmentInfo(Long id, LocalDateTime appointmentTime, String status, String instructorName, String courseName, String appointmentTypeName) {
        this.id = id;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.instructorName = instructorName;
        this.courseName = courseName;
        this.appointmentTypeName = appointmentTypeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getAppointmentTypeName() {
        return appointmentTypeName;
    }

    public void setAppointmentTypeName(String appointmentTypeName) {
        this.appointmentTypeName = appointmentTypeName;
    }
} 