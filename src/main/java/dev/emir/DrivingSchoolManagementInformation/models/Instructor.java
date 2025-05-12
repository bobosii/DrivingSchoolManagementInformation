package dev.emir.DrivingSchoolManagementInformation.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "instructor")
public class Instructor {
    @Id
    private Long id;
    private String name;
    private String certificationNo;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "instructor")
    private List<CourseSession> sessions;

    public Instructor(){}

    public Instructor(Long id, String name, String certificationNo, User user, List<CourseSession> sessions) {
        this.id = id;
        this.name = name;
        this.certificationNo = certificationNo;
        this.user = user;
        this.sessions = sessions;
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

    public String getCertificationNo() {
        return certificationNo;
    }

    public void setCertificationNo(String certificationNo) {
        this.certificationNo = certificationNo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CourseSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<CourseSession> sessions) {
        this.sessions = sessions;
    }
}
