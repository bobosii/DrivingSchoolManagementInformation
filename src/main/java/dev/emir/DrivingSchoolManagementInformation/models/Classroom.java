package dev.emir.DrivingSchoolManagementInformation.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "classroom")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int capacity;
    private String location;

    @OneToMany(mappedBy = "classroom")
    @JsonManagedReference
    private List<CourseSession> courseSessions;

    public Classroom() {}

    public Classroom(Long id, String name, int capacity, String location, List<CourseSession> courseSessions) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.location = location;
        this.courseSessions = courseSessions;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<CourseSession> getCourseSessions() {
        return courseSessions;
    }

    public void setCourseSessions(List<CourseSession> courseSessions) {
        this.courseSessions = courseSessions;
    }
}
