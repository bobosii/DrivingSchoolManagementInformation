package dev.emir.DrivingSchoolManagementInformation.dto.response;

import java.time.LocalDate;

public class StudentDashboardResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String termName;
    private Long termId;
    private int totalAppointments;
    private int totalCourseSessions;
    private int totalDocuments;

    public StudentDashboardResponse() {}

    public StudentDashboardResponse(Long id, String firstName, String lastName, String email, 
                                  LocalDate birthDate, String termName, Long termId,
                                  int totalAppointments, int totalCourseSessions, int totalDocuments) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.termName = termName;
        this.termId = termId;
        this.totalAppointments = totalAppointments;
        this.totalCourseSessions = totalCourseSessions;
        this.totalDocuments = totalDocuments;
    }

    // Getters and Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public Long getTermId() {
        return termId;
    }

    public void setTermId(Long termId) {
        this.termId = termId;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(int totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public int getTotalCourseSessions() {
        return totalCourseSessions;
    }

    public void setTotalCourseSessions(int totalCourseSessions) {
        this.totalCourseSessions = totalCourseSessions;
    }

    public int getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(int totalDocuments) {
        this.totalDocuments = totalDocuments;
    }
} 