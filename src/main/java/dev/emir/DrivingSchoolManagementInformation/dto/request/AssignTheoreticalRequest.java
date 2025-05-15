package dev.emir.DrivingSchoolManagementInformation.dto.request;

public class AssignTheoreticalRequest {
    private Long studentId;
    private Long courseSessionId;
    private Long assignerId;

    public AssignTheoreticalRequest(){}

    public AssignTheoreticalRequest(Long studentId, Long courseSessionId, Long assignerId) {
        this.studentId = studentId;
        this.courseSessionId = courseSessionId;
        this.assignerId = assignerId;
    }

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

    public Long getAssignerId() {
        return assignerId;
    }

    public void setAssignerId(Long assignerId) {
        this.assignerId = assignerId;
    }
}
