package dev.emir.DrivingSchoolManagementInformation.models;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    private Long id;
    private String name;
    private String department;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Employee(){}

    public Employee(Long id, String name, String department, User user) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.user = user;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
