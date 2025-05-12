package dev.emir.DrivingSchoolManagementInformation.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "exam")
public class Exam {
    @Id
    private Long id;
    private String type;
    private LocalDate date;

    @OneToMany(mappedBy = "exam")
    private List<StudentExam> results;

    public Exam(){}

    public Exam(Long id, String type, LocalDate date, List<StudentExam> results) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.results = results;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<StudentExam> getResults() {
        return results;
    }

    public void setResults(List<StudentExam> results) {
        this.results = results;
    }
}
