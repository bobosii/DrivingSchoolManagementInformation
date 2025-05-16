package dev.emir.DrivingSchoolManagementInformation.dto.request;

public class ApproveAppointmentRequest {
    private Long appointmentId;
    private Long approverId;

    public ApproveAppointmentRequest(){}

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }
}
