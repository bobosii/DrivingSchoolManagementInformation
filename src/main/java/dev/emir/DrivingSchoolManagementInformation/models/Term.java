package dev.emir.DrivingSchoolManagementInformation.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.emir.DrivingSchoolManagementInformation.models.enums.Month;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "term")
public class Term {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Month month;
    
    private int year;
    private int quota;

    @OneToMany(mappedBy = "term")
    @JsonManagedReference
    private List<Student> students;

    public Term() {}

    public Term(Long id, Month month, int year, int quota, List<Student> students) {
        this.id = id;
        this.month = month;
        this.year = year;
        this.quota = quota;
        this.students = students;
    }

    public String getName() {
        return year + " " + month.getDisplayName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
