package dev.emir.DrivingSchoolManagementInformation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class DocumentCreateRequest {
    @NotBlank(message = "Belge tipi boş olamaz")
    private String type;

    @NotNull(message = "Dosya boş olamaz")
    private MultipartFile file;

    @NotNull(message = "Öğrenci ID boş olamaz")
    private Long studentId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
} 