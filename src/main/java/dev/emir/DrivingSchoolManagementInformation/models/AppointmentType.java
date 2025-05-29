package dev.emir.DrivingSchoolManagementInformation.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "appointment_type")
public class AppointmentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name; //Simulator, Practical

    @OneToMany(mappedBy = "type")
    private List<Appointment> appointments;

    public AppointmentType(){}

    public AppointmentType(Long id, String name, List<Appointment> appointments) {
        this.id = id;
        this.name = name;
        this.appointments = appointments;
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

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
