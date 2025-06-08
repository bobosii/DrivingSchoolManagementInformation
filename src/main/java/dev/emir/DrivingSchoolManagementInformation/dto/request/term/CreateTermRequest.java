package dev.emir.DrivingSchoolManagementInformation.dto.request.term;

import dev.emir.DrivingSchoolManagementInformation.models.enums.Month;

public class CreateTermRequest {
    private Month month;
    private int year;
    private int quota;

    public CreateTermRequest() {}

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }
}
