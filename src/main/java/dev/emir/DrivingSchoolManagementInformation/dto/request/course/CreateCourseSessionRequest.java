package dev.emir.DrivingSchoolManagementInformation.dto.request.course;

import java.time.LocalDateTime;

public class CreateCourseSessionRequest {
    private Long courseId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long classroomId;
    private Long instructorId;

    public CreateCourseSessionRequest(){
    }

    public CreateCourseSessionRequest(Long courseId, LocalDateTime startTime, LocalDateTime endTime, Long classroomId, Long instructorId) {
        this.courseId = courseId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classroomId = classroomId;
        this.instructorId = instructorId;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Long classRoomId) {
        this.classroomId = classRoomId;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
