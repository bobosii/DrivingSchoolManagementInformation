package dev.emir.DrivingSchoolManagementInformation.dto.request.appointment;

import dev.emir.DrivingSchoolManagementInformation.models.enums.AppointmentStatus;
import java.time.LocalDateTime;

public class AppointmentRequest {
    private Long studentId;
    private Long instructorId;
    private Long appointmentTypeId;
    private Long courseSessionId;
    private LocalDateTime appointmentTime;
    private AppointmentStatus status;

    public AppointmentRequest() {}

    public AppointmentRequest(Long studentId, Long instructorId, Long appointmentTypeId, Long courseSessionId, LocalDateTime appointmentTime, AppointmentStatus status) {
        this.studentId = studentId;
        this.instructorId = instructorId;
        this.appointmentTypeId = appointmentTypeId;
        this.courseSessionId = courseSessionId;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public Long getAppointmentTypeId() {
        return appointmentTypeId;
    }

    public void setAppointmentTypeId(Long appointmentTypeId) {
        this.appointmentTypeId = appointmentTypeId;
    }

    public Long getCourseSessionId() {
        return courseSessionId;
    }

    public void setCourseSessionId(Long courseSessionId) {
        this.courseSessionId = courseSessionId;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}
