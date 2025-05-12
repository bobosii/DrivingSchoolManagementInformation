package dev.emir.DrivingSchoolManagementInformation.models;

import dev.emir.DrivingSchoolManagementInformation.models.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    private long id;

    private String userName;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user")
    private Student student;

    @OneToOne
    private Instructor instructor;

    @OneToOne
    private Employee employee;

    public User(){}

    public User(long id, String userName, String password, Role role, Student student, Instructor instructor, Employee employee) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.student = student;
        this.instructor = instructor;
        this.employee = employee;
    }
}
