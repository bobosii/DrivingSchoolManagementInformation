package dev.emir.DrivingSchoolManagementInformation.dto.response.course;

import java.time.LocalDateTime;

public class CourseSessionResponse {
    private Long id;
    private CourseResponse course;
    private InstructorResponse instructor;
    private String classroomName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public CourseSessionResponse() {
    }

    public CourseSessionResponse(Long id, CourseResponse course, String instructorFirstName, String instructorLastName, String classroomName, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.course = course;
        this.instructor = new InstructorResponse(instructorFirstName, instructorLastName);
        this.classroomName = classroomName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CourseResponse getCourse() {
        return course;
    }

    public void setCourse(CourseResponse course) {
        this.course = course;
    }

    public InstructorResponse getInstructor() {
        return instructor;
    }

    public void setInstructor(InstructorResponse instructor) {
        this.instructor = instructor;
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

    public static class InstructorResponse {
        private String firstName;
        private String lastName;

        public InstructorResponse() {
        }

        public InstructorResponse(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }
} 