package dev.emir.DrivingSchoolManagementInformation.dto.response;

import dev.emir.DrivingSchoolManagementInformation.models.enums.AppointmentStatus;

import java.time.LocalDateTime;

public class AppointmentResponse {
    private Long studentId;
    private String studentName;
    private Long instructorId;
    private String instructorName;
    private Long courseSessionId;
    private String courseName;
    private Long appointmentTypeId;
    private String appointmentTypeName;
    private AppointmentStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime appointmentTime;

    public AppointmentResponse(){}

    public AppointmentResponse(Long studentId, String studentName, Long instructorId, String instructorName, Long courseSessionId, String courseName, Long appointmentTypeId, String appointmentTypeName, AppointmentStatus status, LocalDateTime requestedAt, LocalDateTime approvedAt, LocalDateTime appointmentTime) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.courseSessionId = courseSessionId;
        this.courseName = courseName;
        this.appointmentTypeId = appointmentTypeId;
        this.appointmentTypeName = appointmentTypeName;
        this.status = status;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.appointmentTime = appointmentTime;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Long getAppointmentTypeId() {
        return appointmentTypeId;
    }

    public void setAppointmentTypeId(Long appointmentTypeId) {
        this.appointmentTypeId = appointmentTypeId;
    }

    public String getAppointmentTypeName() {
        return appointmentTypeName;
    }

    public void setAppointmentTypeName(String appointmentTypeName) {
        this.appointmentTypeName = appointmentTypeName;
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
