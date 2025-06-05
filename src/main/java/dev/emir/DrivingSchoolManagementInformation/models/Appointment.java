package dev.emir.DrivingSchoolManagementInformation.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dev.emir.DrivingSchoolManagementInformation.models.enums.AppointmentStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    @JsonBackReference
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "instructor_id")
    @JsonBackReference
    private Instructor instructor;

    @ManyToOne()
    @JoinColumn(name = "appointment_type_id")
    @JsonBackReference
    private AppointmentType appointmentType;

    @ManyToOne()
    @JoinColumn(name = "course_session_id", nullable = true)
    @JsonBackReference
    private CourseSession courseSession;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.PENDING;

    private LocalDateTime requestedAt = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    private LocalDateTime approvedAt;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    @JsonBackReference
    private User approvedBy;

    public Appointment() {}

    public Appointment(Long id, Student student, Instructor instructor, AppointmentType appointmentType, CourseSession courseSession, AppointmentStatus status, LocalDateTime requestedAt, LocalDateTime appointmentTime, LocalDateTime approvedAt, User approvedBy) {
        this.id = id;
        this.student = student;
        this.instructor = instructor;
        this.appointmentType = appointmentType;
        this.courseSession = courseSession;
        this.status = status;
        this.requestedAt = requestedAt;
        this.appointmentTime = appointmentTime;
        this.approvedAt = approvedAt;
        this.approvedBy = approvedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public CourseSession getCourseSession() {
        return courseSession;
    }

    public void setCourseSession(CourseSession courseSession) {
        this.courseSession = courseSession;
    }
}
