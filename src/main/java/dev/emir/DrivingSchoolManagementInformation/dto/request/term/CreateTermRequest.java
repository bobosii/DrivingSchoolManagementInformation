package dev.emir.DrivingSchoolManagementInformation.dto.request.term;

import java.time.LocalDate;

public class CreateTermRequest {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int quota;

    public CreateTermRequest() {}

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
}
