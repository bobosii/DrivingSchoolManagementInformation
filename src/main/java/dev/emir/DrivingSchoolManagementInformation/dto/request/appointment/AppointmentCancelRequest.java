package dev.emir.DrivingSchoolManagementInformation.dto.request.appointment;

public class AppointmentCancelRequest {
    private Long appointmentId;
    private Long studentId;

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
