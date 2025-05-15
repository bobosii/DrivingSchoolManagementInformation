package dev.emir.DrivingSchoolManagementInformation.dto.response.studentCourseSession;

import java.time.LocalDateTime;

public class StudentCourseSessionResponse {
    private Long id;
    private Long studentId;
    private Long courseSessionId;
    private boolean approved;
    private LocalDateTime assignedAt;
    private String assignedByUsername;
    private String courseName;

    public StudentCourseSessionResponse(){}

    public StudentCourseSessionResponse(Long id, Long studentId, Long courseSessionId, boolean approved, LocalDateTime assignedAt, String assignedByUsername, String courseName) {
        this.id = id;
        this.studentId = studentId;
        this.courseSessionId = courseSessionId;
        this.approved = approved;
        this.assignedAt = assignedAt;
        this.assignedByUsername = assignedByUsername;
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getAssignedByUsername() {
        return assignedByUsername;
    }

    public void setAssignedByUsername(String assignedByUsername) {
        this.assignedByUsername = assignedByUsername;
    }
}
