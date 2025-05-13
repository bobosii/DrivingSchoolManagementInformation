package dev.emir.DrivingSchoolManagementInformation.dto;

import java.time.LocalDate;

public class StudentProfileDTO {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String termName;

    public StudentProfileDTO(){}

    public StudentProfileDTO(String firstName, String lastName, String email, LocalDate birthDate, String termName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.termName = termName;
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
}
