package dev.emir.DrivingSchoolManagementInformation.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "simulator_session")
public class SimulatorSession {
    @Id
    private Long id;
    private LocalDateTime sessionDate;
    private int durationMinutes;

    @ManyToMany(mappedBy = "simulatorSessions")
    private List<Student> students;

    public SimulatorSession(){}

    public SimulatorSession(Long id, LocalDateTime sessionDate, int durationMinutes, List<Student> students) {
        this.id = id;
        this.sessionDate = sessionDate;
        this.durationMinutes = durationMinutes;
        this.students = students;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDateTime sessionDate) {
        this.sessionDate = sessionDate;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
