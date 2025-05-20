package dev.emir.DrivingSchoolManagementInformation.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "course_session")
public class CourseSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int capacity;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Classroom classroom;

    @ManyToOne
    private Instructor instructor;

    @ManyToMany(mappedBy = "courseSessions")
    private List<Student> enrolledStudents;

    public CourseSession(){}

    public CourseSession(Long id, LocalDateTime startTime, LocalDateTime endTime, int capacity, Course course, Classroom classroom, Instructor instructor, List<Student> enrolledStudents) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.course = course;
        this.classroom = classroom;
        this.instructor = instructor;
        this.enrolledStudents = enrolledStudents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }
}
