package dev.emir.DrivingSchoolManagementInformation.models;

import dev.emir.DrivingSchoolManagementInformation.models.enums.CourseType;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private CourseType courseType;

    @OneToMany(mappedBy = "course")
    private List<CourseSession> sessions;

    public Course(){}


    public Course(Long id, String name, List<CourseSession> sessions, CourseType courseType) {
        this.id = id;
        this.name = name;
        this.sessions = sessions;
        this.courseType = courseType;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<CourseSession> sessions) {
        this.sessions = sessions;
    }
}
