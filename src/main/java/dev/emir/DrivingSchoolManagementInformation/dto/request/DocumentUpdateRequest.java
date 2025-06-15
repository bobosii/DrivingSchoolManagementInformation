package dev.emir.DrivingSchoolManagementInformation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DocumentUpdateRequest {
    @NotBlank(message = "Belge tipi zorunludur")
    private String type;

    @NotNull(message = "Öğrenci ID zorunludur")
    private Long studentId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
} 