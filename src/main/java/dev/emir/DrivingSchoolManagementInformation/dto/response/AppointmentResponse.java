package dev.emir.DrivingSchoolManagementInformation.dto.response;

import dev.emir.DrivingSchoolManagementInformation.models.enums.AppointmentStatus;

import java.time.LocalDateTime;

public class AppointmentResponse {
    private String studentName;
    private String instructorName;
    private Long courseSessionId;
    private Long appointmentTypeId;
    private AppointmentStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime appointmentTime;

    public AppointmentResponse(){}

    public AppointmentResponse(String studentName, String instructorName, Long courseSessionId, Long appointmentTypeId, AppointmentStatus status, LocalDateTime requestedAt, LocalDateTime approvedAt, LocalDateTime appointmentTime) {
        this.studentName = studentName;
        this.instructorName = instructorName;
        this.courseSessionId = courseSessionId;
        this.appointmentTypeId = appointmentTypeId;
        this.status = status;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.appointmentTime = appointmentTime;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public Long getCourseSessionId() {
        return courseSessionId;
    }

    public void setCourseSessionId(Long courseSessionId) {
        this.courseSessionId = courseSessionId;
    }

    public Long getAppointmentTypeId() {
        return appointmentTypeId;
    }

    public void setAppointmentTypeId(Long appointmentTypeId) {
        this.appointmentTypeId = appointmentTypeId;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
