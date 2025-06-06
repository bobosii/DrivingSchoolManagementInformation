package dev.emir.DrivingSchoolManagementInformation.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @ManyToOne
    private Term term;

    @ManyToMany
    @JoinTable(
            name = "student_license_class_request",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "license_class_id")
    )
    private List<LicenseClass> requestedLicenseClasses;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<Document> documents;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private List<StudentExam> exams;

    @ManyToMany
    @JsonIgnore
    private List<CourseSession> courseSessions;

    @ManyToMany
    @JsonIgnore
    private List<SimulatorSession> simulatorSessions;

    public Student(){}

    public Student(Long id, String firstName, String lastName, LocalDate birthDate, String email, User user, Term term, List<LicenseClass> requestedLicenseClasses, List<Document> documents, List<Appointment> appointments, List<StudentExam> exams, List<CourseSession> courseSessions, List<SimulatorSession> simulatorSessions) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.user = user;
        this.term = term;
        this.requestedLicenseClasses = requestedLicenseClasses;
        this.documents = documents;
        this.appointments = appointments;
        this.exams = exams;
        this.courseSessions = courseSessions;
        this.simulatorSessions = simulatorSessions;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public List<LicenseClass> getRequestedLicenseClasses() {
        return requestedLicenseClasses;
    }

    public void setRequestedLicenseClasses(List<LicenseClass> requestedLicenseClasses) {
        this.requestedLicenseClasses = requestedLicenseClasses;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<StudentExam> getExams() {
        return exams;
    }

    public void setExams(List<StudentExam> exams) {
        this.exams = exams;
    }

    public List<CourseSession> getCourseSessions() {
        return courseSessions;
    }

    public void setCourseSessions(List<CourseSession> courseSessions) {
        this.courseSessions = courseSessions;
    }

    public List<SimulatorSession> getSimulatorSessions() {
        return simulatorSessions;
    }

    public void setSimulatorSessions(List<SimulatorSession> simulatorSessions) {
        this.simulatorSessions = simulatorSessions;
    }
}
