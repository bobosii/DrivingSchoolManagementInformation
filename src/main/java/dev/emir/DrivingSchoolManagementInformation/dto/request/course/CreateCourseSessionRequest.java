package dev.emir.DrivingSchoolManagementInformation.dto.request.course;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateCourseSessionRequest {
    @NotNull(message = "Kurs ID boş olamaz")
    private Long courseId;

    @NotNull(message = "Başlangıç zamanı boş olamaz")
    private LocalDateTime startTime;

    @NotNull(message = "Bitiş zamanı boş olamaz")
    private LocalDateTime endTime;

    @NotNull(message = "Sınıf ID boş olamaz")
    private Long classroomId;

    @NotNull(message = "Eğitmen ID boş olamaz")
    private Long instructorId;

    @NotNull(message = "Maksimum öğrenci sayısı boş olamaz")
    @Min(value = 1, message = "Maksimum öğrenci sayısı en az 1 olmalıdır")
    private Integer maxStudents;

    public CreateCourseSessionRequest() {}

    public CreateCourseSessionRequest(Long courseId, LocalDateTime startTime, LocalDateTime endTime,
                                    Long classroomId, Long instructorId, Integer maxStudents) {
        this.courseId = courseId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classroomId = classroomId;
        this.instructorId = instructorId;
        this.maxStudents = maxStudents;
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

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Long classroomId) {
        this.classroomId = classroomId;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }
}
