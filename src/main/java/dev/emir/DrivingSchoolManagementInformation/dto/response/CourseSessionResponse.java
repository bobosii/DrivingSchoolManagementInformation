package dev.emir.DrivingSchoolManagementInformation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class CourseSessionResponse {
    private Long id;
    private String courseName;
    private String instructorFullName;
    private String classroomName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxStudents;
    private boolean isActive;
    private List<StudentResponse> students;

    public CourseSessionResponse() {
    }

    public CourseSessionResponse(Long id, String courseName, String instructorFullName, String classroomName, LocalDateTime startTime, LocalDateTime endTime, int maxStudents, boolean isActive) {
        this.id = id;
        this.courseName = courseName;
        this.instructorFullName = instructorFullName;
        this.classroomName = classroomName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxStudents = maxStudents;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructorFullName() {
        return instructorFullName;
    }

    public void setInstructorFullName(String instructorFullName) {
        this.instructorFullName = instructorFullName;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<StudentResponse> getStudents() {
        return students;
    }

    public void setStudents(List<StudentResponse> students) {
        this.students = students;
    }
}
