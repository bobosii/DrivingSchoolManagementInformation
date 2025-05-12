package dev.emir.DrivingSchoolManagementInformation.models;

import jakarta.persistence.*;

@Entity
@Table(name = "student_exam")
public class StudentExam {
    @Id
    private Long id;
    private int score;
    private boolean passed;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Exam exam;

    public StudentExam(){}

    public StudentExam(Long id, int score, boolean passed, Student student, Exam exam) {
        this.id = id;
        this.score = score;
        this.passed = passed;
        this.student = student;
        this.exam = exam;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }
}
