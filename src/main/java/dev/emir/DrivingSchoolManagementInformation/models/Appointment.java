package dev.emir.DrivingSchoolManagementInformation.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "appointment")
public class Appointment {
    @Id
    private Long id;
    private LocalDate date;
    private String status;

    @ManyToOne
    private Student student;

    @ManyToOne
    private AppointmentType type;

    public Appointment(){}

    public Appointment(Long id, LocalDate date, String status, Student student, AppointmentType type) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.student = student;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public AppointmentType getType() {
        return type;
    }

    public void setType(AppointmentType type) {
        this.type = type;
    }
}
