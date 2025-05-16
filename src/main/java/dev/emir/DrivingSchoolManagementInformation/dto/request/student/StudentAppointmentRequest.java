package dev.emir.DrivingSchoolManagementInformation.dto.request.student;

public class StudentAppointmentRequest {
    private Long studentId;
    private Long courseSessionId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseSessionId() {
        return courseSessionId;
    }

    public void setCourseSessionId(Long courseSessionId) {
        this.courseSessionId = courseSessionId;
    }
}
