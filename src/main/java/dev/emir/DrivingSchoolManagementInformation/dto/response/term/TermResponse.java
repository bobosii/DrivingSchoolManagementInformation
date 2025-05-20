package dev.emir.DrivingSchoolManagementInformation.dto.response.term;

import java.time.LocalDate;

public class TermResponse {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int quota;
    private int studentCount;

    public TermResponse() {
    }

    public TermResponse(Long id, String name, LocalDate startDate, LocalDate endDate, int quota, int studentCount) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quota = quota;
        this.studentCount = studentCount;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }
} 