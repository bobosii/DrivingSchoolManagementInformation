package dev.emir.DrivingSchoolManagementInformation.dto.request.appointment;

import java.time.LocalDateTime;

public class AppointmentRequest {
    private Long studentId;
    private Long instructorId;
    private Long courseSessionId;
    private Long appointmentTypeId;
    private LocalDateTime appointmentTime;

    public AppointmentRequest() {}

    public AppointmentRequest(Long studentId, Long instructorId, Long courseSessionId, Long appointmentTypeId, LocalDateTime appointmentTime) {
        this.studentId = studentId;
        this.instructorId = instructorId;
        this.courseSessionId = courseSessionId;
        this.appointmentTypeId = appointmentTypeId;
        this.appointmentTime = appointmentTime;
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

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
